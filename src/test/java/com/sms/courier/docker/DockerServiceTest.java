package com.sms.courier.docker;

import static com.sms.courier.common.exception.ErrorCode.DELETE_CONTAINER_ERROR;
import static com.sms.courier.common.exception.ErrorCode.GET_CONTAINER_SETTING_ERROR;
import static com.sms.courier.common.exception.ErrorCode.NO_SUCH_CONTAINER_ERROR;
import static com.sms.courier.common.exception.ErrorCode.PULL_IMAGE_ERROR;
import static com.sms.courier.common.exception.ErrorCode.QUERY_CONTAINER_LOG_ERROR;
import static com.sms.courier.common.exception.ErrorCode.RESTART_CONTAINER_ERROR;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.api.async.ResultCallback.Adapter;
import com.github.dockerjava.api.command.ListImagesCmd;
import com.github.dockerjava.api.command.LogContainerCmd;
import com.github.dockerjava.api.command.PullImageCmd;
import com.github.dockerjava.api.command.RemoveContainerCmd;
import com.github.dockerjava.api.command.RestartContainerCmd;
import com.github.dockerjava.api.exception.NotFoundException;
import com.github.dockerjava.api.model.PullResponseItem;
import com.sms.courier.common.exception.ApiTestPlatformException;
import com.sms.courier.docker.entity.ContainerInfo;
import com.sms.courier.docker.entity.ContainerSettingEntity;
import com.sms.courier.docker.service.DockerService;
import com.sms.courier.docker.service.impl.DockerServiceImpl;
import com.sms.courier.dto.request.ContainerSettingRequest;
import com.sms.courier.dto.request.DockerLogRequest;
import com.sms.courier.dto.response.ContainerSettingResponse;
import com.sms.courier.mapper.DockerContainerMapper;
import com.sms.courier.mapper.DockerContainerMapperImpl;
import com.sms.courier.repository.ContainerSettingRepository;
import com.sms.courier.service.MessageService;
import java.util.Collections;
import java.util.Date;
import java.util.Map;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("Test for DockerService")
public class DockerServiceTest {

    private final DockerClient dockerClient = mock(DockerClient.class);
    private final MessageService messageService = mock(MessageService.class);
    private final ContainerSettingRepository containerSettingRepository = mock(ContainerSettingRepository.class);
    private final DockerContainerMapper dockerContainerMapper = new DockerContainerMapperImpl();
    private final ContainerInfo containerInfo = new ContainerInfo("netWorkId", "imageName", "containerName",
        "1.0.0", null, Map.of("key", "value"));
    private final DockerService dockerService = new DockerServiceImpl(dockerClient, messageService,
        containerSettingRepository, dockerContainerMapper);

    /*@DisplayName("Test the startContainer method in the docker service")
    @Test
    void startContainer_test() {
        Void v = mock(Void.class);
        when(containerSettingRepository.getFirstByOrderByModifyDateTimeDesc()).thenReturn(Optional
            .of(ContainerSettingEntity.builder().netWorkId("").username("username").password("p234f342fg").build()));
        PullImageCmd pullImageCmd = mock(PullImageCmd.class);
        when(dockerClient.pullImageCmd(anyString())).thenReturn(pullImageCmd);
        when(pullImageCmd.withAuthConfig(any())).thenReturn(pullImageCmd);
        Adapter<PullResponseItem> pullResponseItemAdapter = new Adapter<>();
        when(pullImageCmd.exec(any())).thenReturn(pullResponseItemAdapter);
        CreateContainerCmd createContainerCmd = mock(CreateContainerCmd.class);
        when(dockerClient.createContainerCmd(anyString())).thenReturn(createContainerCmd);
        when(createContainerCmd.withName(anyString())).thenReturn(createContainerCmd);
        when(createContainerCmd.withEnv(any(List.class))).thenReturn(createContainerCmd);
        CreateContainerResponse createContainerResponse = new CreateContainerResponse();
        createContainerResponse.setId("id");
        when(createContainerCmd.exec()).thenReturn(createContainerResponse);
        ConnectToNetworkCmd connectToNetworkCmd = mock(ConnectToNetworkCmd.class);
        when(dockerClient.connectToNetworkCmd()).thenReturn(connectToNetworkCmd);
        when(connectToNetworkCmd.withContainerId(anyString())).thenReturn(connectToNetworkCmd);
        when(connectToNetworkCmd.withNetworkId(anyString())).thenReturn(connectToNetworkCmd);
        doNothing().when(connectToNetworkCmd).exec();
        StartContainerCmd startContainerCmd = mock(StartContainerCmd.class);
        when(dockerClient.startContainerCmd(anyString())).thenReturn(startContainerCmd);
        doNothing().when(startContainerCmd).exec();
        dockerService.startContainer(containerInfo);
        verify(dockerClient, times(1)).startContainerCmd(anyString());
    }*/

    @DisplayName("Test the startContainer method in the docker service")
    @Test
    void startContainer_test() {
        when(containerSettingRepository.getFirstByOrderByModifyDateTimeDesc()).thenReturn(Optional
            .of(ContainerSettingEntity.builder().netWorkId("").username("username").password("DRKFZPy1E1M3zcZSFLVorg==")
                .build()));
        ListImagesCmd listImagesCmd = mock(ListImagesCmd.class);
        when(dockerClient.listImagesCmd()).thenReturn(listImagesCmd);
        when(listImagesCmd.withImageNameFilter(any())).thenReturn(listImagesCmd);
        when(listImagesCmd.exec()).thenReturn(Collections.emptyList());
        PullImageCmd pullImageCmd = mock(PullImageCmd.class);
        when(dockerClient.pullImageCmd(anyString())).thenReturn(pullImageCmd);
        when(pullImageCmd.withAuthConfig(any())).thenReturn(pullImageCmd);
        Adapter<PullResponseItem> pullResponseItemAdapter = new Adapter<>();
        when(pullImageCmd.exec(any())).thenReturn(pullResponseItemAdapter);
        pullResponseItemAdapter.onError(new NotFoundException(""));
        dockerService.startContainer(containerInfo);
        verify(pullImageCmd, times(1)).exec(any());
    }

   /* @DisplayName("An notFoundException occurred while start container")
    @Test
    void startContainer_notFoundException_test() {
        Void v = mock(Void.class);
        CreateContainerCmd createContainerCmd = mock(CreateContainerCmd.class);
        when(dockerClient.createContainerCmd(anyString())).thenReturn(createContainerCmd);
        when(createContainerCmd.withName(anyString())).thenReturn(createContainerCmd);
        when(createContainerCmd.withEnv(any(List.class))).thenReturn(createContainerCmd);
        when(createContainerCmd.exec()).thenThrow(new NotFoundException(""));
        assertThatThrownBy(() -> dockerService.startContainer(containerInfo))
            .isInstanceOf(ApiTestPlatformException.class)
            .extracting("code").isEqualTo(NO_SUCH_IMAGE_ERROR.getCode());
    }

    @DisplayName("An conflictException occurred while start container")
    @Test
    void startContainer_conflictException_test() {
        Void v = mock(Void.class);
        CreateContainerCmd createContainerCmd = mock(CreateContainerCmd.class);
        when(dockerClient.createContainerCmd(anyString())).thenReturn(createContainerCmd);
        when(createContainerCmd.withName(anyString())).thenReturn(createContainerCmd);
        when(createContainerCmd.withEnv(any(List.class))).thenReturn(createContainerCmd);
        when(createContainerCmd.exec()).thenThrow(new ConflictException(""));
        assertThatThrownBy(() -> dockerService.startContainer(containerInfo))
            .isInstanceOf(ApiTestPlatformException.class)
            .extracting("code").isEqualTo(THE_CONTAINER_ALREADY_EXISTED_ERROR.getCode());
    }*/

    @DisplayName("An customException occurred while start container")
    @Test
    void startContainer_customException_test() {
        when(containerSettingRepository.getFirstByOrderByModifyDateTimeDesc()).thenReturn(Optional.empty());
        assertThatThrownBy(() -> dockerService.startContainer(containerInfo))
            .isInstanceOf(ApiTestPlatformException.class)
            .extracting("code").isEqualTo(GET_CONTAINER_SETTING_ERROR.getCode());
    }

    @DisplayName("An runtimeException occurred while start container")
    @Test
    void startContainer_runtimeException_test() {
        when(containerSettingRepository.getFirstByOrderByModifyDateTimeDesc()).thenThrow(new RuntimeException());
        assertThatThrownBy(() -> dockerService.startContainer(containerInfo))
            .isInstanceOf(ApiTestPlatformException.class)
            .extracting("code").isEqualTo(PULL_IMAGE_ERROR.getCode());
    }

    @DisplayName("Test the queryLog method in the docker service")
    @Test
    void queryLog_test() {
        DockerLogRequest request = DockerLogRequest.builder().name("name").since(new Date()).build();
        LogContainerCmd logContainerCmd = mock(LogContainerCmd.class);
        when(dockerClient.logContainerCmd(anyString())).thenReturn(logContainerCmd);
        when(logContainerCmd.withStdErr(anyBoolean())).thenReturn(logContainerCmd);
        when(logContainerCmd.withStdOut(anyBoolean())).thenReturn(logContainerCmd);
        when(logContainerCmd.withTimestamps(anyBoolean())).thenReturn(logContainerCmd);
        when(logContainerCmd.withTail(anyInt())).thenReturn(logContainerCmd);
        when(logContainerCmd.withSince(anyInt())).thenReturn(logContainerCmd);
        dockerService.queryLog(request);
        verify(logContainerCmd, times(1)).exec(any());
    }

    @DisplayName("An notFoundException occurred while query log")
    @Test
    void queryLog_notFoundException_test() {
        DockerLogRequest request = DockerLogRequest.builder().name("name").since(new Date()).build();
        LogContainerCmd logContainerCmd = mock(LogContainerCmd.class);
        when(dockerClient.logContainerCmd(anyString())).thenReturn(logContainerCmd);
        when(logContainerCmd.withStdErr(anyBoolean())).thenReturn(logContainerCmd);
        when(logContainerCmd.withStdOut(anyBoolean())).thenReturn(logContainerCmd);
        when(logContainerCmd.withTimestamps(anyBoolean())).thenReturn(logContainerCmd);
        when(logContainerCmd.withTail(anyInt())).thenReturn(logContainerCmd);
        when(logContainerCmd.withSince(anyInt())).thenReturn(logContainerCmd);
        when(logContainerCmd.exec(any())).thenThrow(new NotFoundException(""));
        assertThatThrownBy(() -> dockerService.queryLog(request)).isInstanceOf(ApiTestPlatformException.class)
            .extracting("code").isEqualTo(NO_SUCH_CONTAINER_ERROR.getCode());
    }

    @DisplayName("An runtimeException occurred while query log")
    @Test
    void queryLog_runtimeException_test() {
        DockerLogRequest request = DockerLogRequest.builder().name("name").since(new Date()).build();
        LogContainerCmd logContainerCmd = mock(LogContainerCmd.class);
        when(dockerClient.logContainerCmd(anyString())).thenReturn(logContainerCmd);
        assertThatThrownBy(() -> dockerService.queryLog(request)).isInstanceOf(ApiTestPlatformException.class)
            .extracting("code").isEqualTo(QUERY_CONTAINER_LOG_ERROR.getCode());
    }

    @DisplayName("Test the deleteContainer method in the docker service")
    @Test
    void deleteContainer_test() {
        RemoveContainerCmd removeContainerCmd = mock(RemoveContainerCmd.class);
        when(dockerClient.removeContainerCmd(anyString())).thenReturn(removeContainerCmd);
        when(removeContainerCmd.withForce(true)).thenReturn(removeContainerCmd);
        dockerService.deleteContainer("name");
        verify(removeContainerCmd, times(1)).exec();
    }

    @DisplayName("An notFoundException occurred while delete container")
    @Test
    void deleteContainer_notFoundException_test() {
        RemoveContainerCmd removeContainerCmd = mock(RemoveContainerCmd.class);
        when(dockerClient.removeContainerCmd(anyString())).thenReturn(removeContainerCmd);
        when(removeContainerCmd.withForce(true)).thenReturn(removeContainerCmd);
        when(removeContainerCmd.exec()).thenThrow(new NotFoundException(""));
        assertThatThrownBy(() -> dockerService.deleteContainer("name")).isInstanceOf(ApiTestPlatformException.class)
            .extracting("code").isEqualTo(NO_SUCH_CONTAINER_ERROR.getCode());
    }

    @DisplayName("An runtimeException occurred while delete container")
    @Test
    void deleteContainer_runtimeException_test() {
        RemoveContainerCmd removeContainerCmd = mock(RemoveContainerCmd.class);
        when(dockerClient.removeContainerCmd(anyString())).thenReturn(removeContainerCmd);
        assertThatThrownBy(() -> dockerService.deleteContainer("name")).isInstanceOf(ApiTestPlatformException.class)
            .extracting("code").isEqualTo(DELETE_CONTAINER_ERROR.getCode());
    }


    @DisplayName("Test the restartContainer method in the docker service")
    @Test
    void restartContainer_test() {
        RestartContainerCmd restartContainerCmd = mock(RestartContainerCmd.class);
        when(dockerClient.restartContainerCmd(anyString())).thenReturn(restartContainerCmd);
        dockerService.restartContainer("name");
        verify(restartContainerCmd, times(1)).exec();
    }

    @DisplayName("An notFoundException occurred while restart container")
    @Test
    void restartContainer_notFoundException_test() {
        RestartContainerCmd restartContainerCmd = mock(RestartContainerCmd.class);
        when(dockerClient.restartContainerCmd(anyString())).thenReturn(restartContainerCmd);
        when(restartContainerCmd.exec()).thenThrow(new NotFoundException(""));
        assertThatThrownBy(() -> dockerService.restartContainer("name")).isInstanceOf(ApiTestPlatformException.class)
            .extracting("code").isEqualTo(NO_SUCH_CONTAINER_ERROR.getCode());
    }

    @DisplayName("An runtimeException occurred while restart container")
    @Test
    void restartContainer_runtimeException_test() {
        RestartContainerCmd restartContainerCmd = mock(RestartContainerCmd.class);
        when(restartContainerCmd.exec()).thenThrow(new RuntimeException(""));
        assertThatThrownBy(() -> dockerService.restartContainer("name")).isInstanceOf(ApiTestPlatformException.class)
            .extracting("code").isEqualTo(RESTART_CONTAINER_ERROR.getCode());
    }

    @DisplayName("Test the findOne method in the docker service")
    @Test
    void findOne_test() {
        when(containerSettingRepository.getFirstByOrderByModifyDateTimeDesc())
            .thenReturn(Optional.of(ContainerSettingEntity.builder().build()));
        ContainerSettingResponse one = dockerService.findOne();
        assertThat(one).isNotNull();
    }

    @DisplayName("Test the editContainerSetting method in the docker service")
    @Test
    void editContainerSetting_test() {
        ContainerSettingRequest containerSettingRequest = new ContainerSettingRequest();
        when(containerSettingRepository.save(any())).thenReturn(new ContainerSettingEntity());
        Boolean result = dockerService.editContainerSetting(containerSettingRequest);
        assertThat(result).isTrue();
    }
}
