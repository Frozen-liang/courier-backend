package com.sms.courier.engine.docker.service.impl;

import static com.sms.courier.common.exception.ErrorCode.CREATE_CONTAINER_ERROR;
import static com.sms.courier.common.exception.ErrorCode.DELETE_CONTAINER_ERROR;
import static com.sms.courier.common.exception.ErrorCode.NO_SUCH_CONTAINER_ERROR;
import static com.sms.courier.common.exception.ErrorCode.NO_SUCH_IMAGE_ERROR;
import static com.sms.courier.common.exception.ErrorCode.QUERY_CONTAINER_LOG_ERROR;
import static com.sms.courier.common.exception.ErrorCode.RESTART_CONTAINER_ERROR;
import static com.sms.courier.common.exception.ErrorCode.THE_CONTAINER_ALREADY_EXISTED_ERROR;

import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.api.async.ResultCallback.Adapter;
import com.github.dockerjava.api.command.CreateContainerCmd;
import com.github.dockerjava.api.command.CreateContainerResponse;
import com.github.dockerjava.api.command.LogContainerCmd;
import com.github.dockerjava.api.exception.ConflictException;
import com.github.dockerjava.api.exception.NotFoundException;
import com.github.dockerjava.api.model.Frame;
import com.sms.courier.dto.request.DockerLogRequest;
import com.sms.courier.dto.response.EngineSettingResponse;
import com.sms.courier.engine.docker.service.DockerService;
import com.sms.courier.service.MessageService;
import com.sms.courier.utils.ExceptionUtils;
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
    private static final String EVN = "%s=%s";
    private static final String IMAGE = "%s:%s";
    private static final int DEFAULT_TAIL = 100;

    public DockerServiceImpl(DockerClient client, MessageService messageService) {
        this.client = client;
        this.messageService = messageService;
    }

    @Override
    public void startContainer(EngineSettingResponse engineSetting) {
        try {
            log.info("Create engine:{}", engineSetting);
            CreateContainerCmd createContainerCmd = client
                .createContainerCmd(String.format(IMAGE, engineSetting.getImageName(), engineSetting.getVersion()))
                .withName(engineSetting.getContainerName());
            Map<String, String> envVariable = engineSetting.getEnvVariable();
            List<String> env = new ArrayList<>();
            env.add(String.format(EVN, "CONTAINER_NAME", engineSetting.getContainerName()));
            if (MapUtils.isNotEmpty(envVariable)) {
                for (Entry<String, String> entry : envVariable.entrySet()) {
                    env.add(String.format(EVN, entry.getKey(), entry.getValue()));
                }
                createContainerCmd.withEnv(env);
            }
            CreateContainerResponse ccr = createContainerCmd.exec();
            client.connectToNetworkCmd().withContainerId(ccr.getId())
                .withNetworkId(engineSetting.getNetWorkId()).exec();
            client.startContainerCmd(ccr.getId()).exec();
        } catch (NotFoundException e) {
            log.error("No such image", e);
            throw ExceptionUtils.mpe(NO_SUCH_IMAGE_ERROR,
                String.format(IMAGE, engineSetting.getImageName(), engineSetting.getVersion()));
        } catch (ConflictException e) {
            log.error("The container already existed!", e);
            throw ExceptionUtils.mpe(THE_CONTAINER_ALREADY_EXISTED_ERROR, engineSetting.getContainerName());
        } catch (Exception e) {
            log.error("Create container error!", e);
            throw ExceptionUtils.mpe(CREATE_CONTAINER_ERROR);
        }
    }

    @Override
    public void queryLog(DockerLogRequest request) {
        try {
            log.info("QueryLog engine: {}", request);
            LogContainerCmd logContainerCmd = client.logContainerCmd(request.getName()).withTimestamps(true)
                .withSince(request.getSince()).withTail(request.getTail())
                .withStdOut(true).withStdErr(true);
            if (Objects.isNull(request.getTail()) && Objects.isNull(request.getSince())) {
                logContainerCmd.withTail(DEFAULT_TAIL);
            }
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
}
