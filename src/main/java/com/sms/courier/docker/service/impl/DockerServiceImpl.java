package com.sms.courier.docker.service.impl;

import static com.sms.courier.common.enums.OperationModule.CONTAINER_SETTING;
import static com.sms.courier.common.enums.OperationType.EDIT;
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
import com.github.dockerjava.api.model.ExposedPort;
import com.github.dockerjava.api.model.Frame;
import com.github.dockerjava.api.model.HealthCheck;
import com.github.dockerjava.api.model.Image;
import com.github.dockerjava.api.model.PortBinding;
import com.github.dockerjava.api.model.Ports.Binding;
import com.github.dockerjava.api.model.PullResponseItem;
import com.github.dockerjava.api.model.RestartPolicy;
import com.sms.courier.common.aspect.annotation.LogRecord;
import com.sms.courier.common.exception.ApiTestPlatformException;
import com.sms.courier.docker.entity.ContainerInfo;
import com.sms.courier.docker.entity.ContainerSettingEntity;
import com.sms.courier.docker.entity.PortMapping;
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
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import java.io.Closeable;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
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
    private static final List<String> HEALTH_CHECK_TEST = List.of("CMD", "nc", "-zv", "localhost", "5555");
    private static final int RETRIES = 10;
    private static final long INTERVAL = 5L;
    private static final long TIME_OUT = 5L;

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

            List<Image> imageList = client.listImagesCmd().withImageNameFilter(image).exec();

            if (CollectionUtils.isNotEmpty(imageList)) {
                initContainer(containerInfo, image, containerSetting.getNetWorkId());
                return;
            }

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
                        Payload.createPayload(true, String.format("Pull image: %s start!", image)));
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
                            Payload.createPayload(true, String.format("Pull image: %s complete!", image)));
                        initContainer(containerInfo, image, containerSetting.getNetWorkId());
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
    @LogRecord(operationType = EDIT, operationModule = CONTAINER_SETTING)
    public Boolean editContainerSetting(ContainerSettingRequest request) {
        ContainerSettingEntity containerSettingEntity = dockerContainerMapper.toEntity(request);
        containerSettingRepository.save(containerSettingEntity);
        return Boolean.TRUE;
    }

    private void initContainer(ContainerInfo containerInfo, String image, String netWorkId) {
        try {
            log.info("Create container:{}", containerInfo);

            messageService.dockerMessage(containerInfo.getDestination(),
                Payload.createPayload(true,
                    String.format("Starting: %s!", containerInfo.getContainerName())));

            HealthCheck healthCheck = new HealthCheck();

            healthCheck = healthCheck.withTest(HEALTH_CHECK_TEST)
                .withInterval(Duration.ofSeconds(INTERVAL).toNanos())
                .withRetries(RETRIES)
                .withTimeout(Duration.ofSeconds(TIME_OUT).toNanos());

            CreateContainerCmd createContainerCmd = client
                .createContainerCmd(image)
                .withName(containerInfo.getContainerName())
                .withLabels(containerInfo.getLabelType().createLabel())
                .withHealthcheck(healthCheck);

            // Add port bing
            addPortBinding(containerInfo.getPortMappings(), createContainerCmd);

            addHostConfig(createContainerCmd);

            // Add env
            addEnv(containerInfo, createContainerCmd);
            // Create container
            CreateContainerResponse ccr = createContainerCmd.exec();
            // Connect network
            client.connectToNetworkCmd().withContainerId(ccr.getId())
                .withNetworkId(netWorkId).exec();
            // Start container
            client.startContainerCmd(ccr.getId()).exec();
            messageService.dockerMessage(containerInfo.getDestination(),
                Payload.createPayload(true,
                    String.format("Started: %s success!", containerInfo.getContainerName()), "End"));
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
        }
    }

    @SuppressFBWarnings("NP_NULL_ON_SOME_PATH_FROM_RETURN_VALUE")
    private void addHostConfig(CreateContainerCmd createContainerCmd) {
        createContainerCmd.getHostConfig().withRestartPolicy(RestartPolicy.unlessStoppedRestart());
    }

    @SuppressFBWarnings("NP_NULL_ON_SOME_PATH_FROM_RETURN_VALUE")
    private void addPortBinding(List<PortMapping> portMappings, CreateContainerCmd createContainerCmd) {
        if (CollectionUtils.isNotEmpty(portMappings)) {
            List<PortBinding> portBindings = portMappings.stream().map((portMapping -> {
                return new PortBinding(Binding.bindPort(portMapping.getBindPort()), ExposedPort.tcp(
                    portMapping.getExposedPort()));
            })).collect(Collectors.toList());
            createContainerCmd.getHostConfig().withPortBindings(portBindings);
        }
    }

    private void addEnv(ContainerInfo containerInfo, CreateContainerCmd createContainerCmd) {
        Map<String, String> envVariable = containerInfo.getEnvVariable();
        List<String> env = new ArrayList<>();
        env.add(String.format(EVN, "CONTAINER_NAME", containerInfo.getContainerName()));
        if (MapUtils.isNotEmpty(envVariable)) {
            for (Entry<String, String> entry : envVariable.entrySet()) {
                env.add(String.format(EVN, entry.getKey(), entry.getValue()));
            }
        }
        createContainerCmd.withEnv(env);
    }

}
