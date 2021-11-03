package com.sms.courier.docker.service.impl;

import static com.sms.courier.common.exception.ErrorCode.DELETE_CONTAINER_ERROR;
import static com.sms.courier.common.exception.ErrorCode.GET_CONTAINER_SETTING_ERROR;
import static com.sms.courier.common.exception.ErrorCode.NO_SUCH_CONTAINER_ERROR;
import static com.sms.courier.common.exception.ErrorCode.NO_SUCH_IMAGE_ERROR;
import static com.sms.courier.common.exception.ErrorCode.PULL_IMAGE_ERROR;
import static com.sms.courier.common.exception.ErrorCode.QUERY_CONTAINER_LOG_ERROR;
import static com.sms.courier.common.exception.ErrorCode.RESTART_CONTAINER_ERROR;
import static com.sms.courier.common.exception.ErrorCode.THE_CONTAINER_ALREADY_EXISTED_ERROR;

import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.api.async.ResultCallback.Adapter;
import com.github.dockerjava.api.command.CreateContainerCmd;
import com.github.dockerjava.api.command.CreateContainerResponse;
import com.github.dockerjava.api.command.LogContainerCmd;
import com.github.dockerjava.api.command.PullImageCmd;
import com.github.dockerjava.api.exception.ConflictException;
import com.github.dockerjava.api.exception.NotFoundException;
import com.github.dockerjava.api.model.AuthConfig;
import com.github.dockerjava.api.model.Frame;
import com.github.dockerjava.api.model.PullResponseItem;
import com.sms.courier.common.exception.ApiTestPlatformException;
import com.sms.courier.docker.entity.ContainerInfo;
import com.sms.courier.docker.entity.ContainerSettingEntity;
import com.sms.courier.docker.service.DockerService;
import com.sms.courier.dto.request.ContainerSettingRequest;
import com.sms.courier.dto.request.DockerLogRequest;
import com.sms.courier.dto.response.ContainerSettingResponse;
import com.sms.courier.mapper.DockerContainerMapper;
import com.sms.courier.repository.ContainerSettingRepository;
import com.sms.courier.service.MessageService;
import com.sms.courier.utils.AesUtil;
import com.sms.courier.utils.ExceptionUtils;
import com.sms.courier.websocket.Payload;
import java.io.Closeable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.MapUtils;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class DockerServiceImpl implements DockerService {

    private final DockerClient client;
    private final MessageService messageService;
    private final ContainerSettingRepository containerSettingRepository;
    private final DockerContainerMapper dockerContainerMapper;
    private static final String EVN = "%s=%s";
    private static final String IMAGE = "%s:%s";
    private static final String CONTAINER_SETTING_ID = "6180d7090cce7b6d7ceca27a";

    public DockerServiceImpl(DockerClient client, MessageService messageService,
        ContainerSettingRepository containerSettingRepository,
        DockerContainerMapper dockerContainerMapper) {
        this.client = client;
        this.messageService = messageService;
        this.containerSettingRepository = containerSettingRepository;
        this.dockerContainerMapper = dockerContainerMapper;
    }

    @Override
    public void startContainer(ContainerInfo containerInfo) {
        String image = String.format(IMAGE, containerInfo.getImageName(), containerInfo.getVersion());
        try {
            ContainerSettingEntity containerSetting = containerSettingRepository
                .getFirstByOrderByModifyDateTimeDesc()
                .orElseThrow(() -> ExceptionUtils.mpe(GET_CONTAINER_SETTING_ERROR));
            PullImageCmd pullImageCmd = client.pullImageCmd(image);
            if (Objects.nonNull(containerSetting.getUsername()) && Objects.nonNull(containerSetting.getPassword())) {
                AuthConfig authConfig = new AuthConfig().withPassword(AesUtil.decrypt(containerSetting.getPassword()))
                    .withUsername(containerSetting.getUsername())
                    .withRegistryAddress(containerSetting.getRegistryAddress());
                pullImageCmd.withAuthConfig(authConfig);
            }
            pullImageCmd.exec(new Adapter<PullResponseItem>() {
                @Override
                public void onStart(Closeable stream) {
                    log.info("Pull image: {} start!", image);
                    messageService.dockerMessage(containerInfo.getDestination(),
                        Payload.createPayload(true, String.format("Pull image: %s start!", image), 1));
                    super.onStart(stream);
                }

                @Override
                public void onError(Throwable throwable) {
                    log.error("Pull image: {} error!", image, throwable);
                    messageService.dockerMessage(containerInfo.getDestination(),
                        Payload.fail(String.format("Pull image: %s error!", image)));
                    super.onError(throwable);
                }

                @Override
                public void onComplete() {
                    try {
                        log.info("Pull image: {} complete!", image);
                        messageService.dockerMessage(containerInfo.getDestination(),
                            Payload.createPayload(true, String.format("Pull image: %s complete!", image), 2));
                        log.info("Create engine:{}", containerInfo);
                        CreateContainerCmd createContainerCmd = client
                            .createContainerCmd(image)
                            .withName(containerInfo.getContainerName());
                        Map<String, String> envVariable = containerInfo.getEnvVariable();

                        // Add env
                        List<String> env = new ArrayList<>();
                        env.add(String.format(EVN, "CONTAINER_NAME", containerInfo.getContainerName()));
                        if (MapUtils.isNotEmpty(envVariable)) {
                            for (Entry<String, String> entry : envVariable.entrySet()) {
                                env.add(String.format(EVN, entry.getKey(), entry.getValue()));
                            }
                            createContainerCmd.withEnv(env);
                        }
                        // Create container
                        CreateContainerResponse ccr = createContainerCmd.exec();
                        // Connect network
                        client.connectToNetworkCmd().withContainerId(ccr.getId())
                            .withNetworkId(containerSetting.getNetWorkId()).exec();
                        // Start container
                        client.startContainerCmd(ccr.getId()).exec();
                        messageService.dockerMessage(containerInfo.getDestination(),
                            Payload.createPayload(true,
                                String.format("Starting: %s success!", containerInfo.getContainerName()),
                                3));
                    } catch (NotFoundException e) {
                        log.error("No such image", e);
                        messageService.dockerMessage(containerInfo.getDestination(),
                            Payload.fail(String.format(NO_SUCH_IMAGE_ERROR.getMessage(), image)));
                    } catch (ConflictException e) {
                        log.error("The container already existed!", e);
                        messageService.dockerMessage(containerInfo.getDestination(),
                            Payload.fail(String.format(THE_CONTAINER_ALREADY_EXISTED_ERROR.getMessage(),
                                containerInfo.getContainerName())));
                    } catch (Exception e) {
                        log.error("Create container error!", e);
                        messageService.dockerMessage(containerInfo.getDestination(),
                            Payload.fail(String.format("Starting: %s error!", containerInfo.getContainerName())));
                    } finally {
                        super.onComplete();
                    }
                }
            });
        } catch (ApiTestPlatformException e) {
            throw e;
        } catch (Exception e) {
            log.error("Pull image error!", e);
            throw ExceptionUtils.mpe(PULL_IMAGE_ERROR, image);
        }


    }

    @Override
    public void queryLog(DockerLogRequest request) {
        try {
            log.info("QueryLog engine: {}", request);
            int since = 0;
            if (Objects.nonNull(request.getSince())) {
                since = Integer.parseInt(String.valueOf(request.getSince().getTime() / 1000));
            }
            LogContainerCmd logContainerCmd = client.logContainerCmd(request.getName()).withTimestamps(true)
                .withSince(since)
                .withTail(request.getTail())
                .withStdOut(true).withStdErr(true);

            logContainerCmd.exec(new Adapter<Frame>() {
                @Override
                public void onNext(Frame frame) {
                    messageService.dockerLog(request.getName(), frame.toString());
                }
            });
        } catch (NotFoundException e) {
            log.error("No such container", e);
            throw ExceptionUtils.mpe(NO_SUCH_CONTAINER_ERROR, request.getName());
        } catch (Exception e) {
            log.error("Query log error!", e);
            throw ExceptionUtils.mpe(QUERY_CONTAINER_LOG_ERROR);
        }
    }

    @Override
    public void deleteContainer(String name) {
        try {
            log.info("Delete engine: {}", name);
            client.removeContainerCmd(name).withForce(true).exec();
        } catch (NotFoundException e) {
            log.error("No such container", e);
            throw ExceptionUtils.mpe(NO_SUCH_CONTAINER_ERROR, name);
        } catch (Exception e) {
            log.error("Delete container error!", e);
            throw ExceptionUtils.mpe(DELETE_CONTAINER_ERROR);
        }
    }

    @Override
    public void restartContainer(String name) {
        try {
            log.info("Restart engine: {}", name);
            client.restartContainerCmd(name).exec();
        } catch (NotFoundException e) {
            log.error("No such container", e);
            throw ExceptionUtils.mpe(NO_SUCH_CONTAINER_ERROR, name);
        } catch (Exception e) {
            log.error("Restart container error!", e);
            throw ExceptionUtils.mpe(RESTART_CONTAINER_ERROR);
        }
    }

    @Override
    public ContainerSettingResponse findOne() {
        return containerSettingRepository.getFirstByOrderByModifyDateTimeDesc().map(dockerContainerMapper::toResponse)
            .orElse(ContainerSettingResponse.builder().id(CONTAINER_SETTING_ID).build());
    }

    @Override
    public Boolean editContainerSetting(ContainerSettingRequest request) {
        ContainerSettingEntity containerSettingEntity = dockerContainerMapper.toEntity(request);
        containerSettingRepository.save(containerSettingEntity);
        return Boolean.TRUE;
    }
}
