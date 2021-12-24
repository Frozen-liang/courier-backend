package com.sms.courier.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import com.sms.courier.engine.grpc.api.v1.GrpcSceneCaseJobRequest;
import com.sms.courier.entity.job.SceneCaseJobEntity;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("Test for GrpcMapper")
public class GrpcMapperTest {

    private final GrpcMapper grpcMapper = new GrpcMapperImpl();
    private static final String ID = ObjectId.get().toString();

    @Test
    public void toGrpcJobRequest_test() {
        SceneCaseJobEntity sceneCaseJobEntity = SceneCaseJobEntity.builder()
            .createUserId("test").id("id").name("name").build();
        GrpcSceneCaseJobRequest jobRequest = grpcMapper.toGrpcJobRequest(sceneCaseJobEntity);
        assertThat(sceneCaseJobEntity.getCreateUserId()).isEqualTo(jobRequest.getCreateUserId());
        /*ApiTestCaseJobEntity apiTestCaseJobEntity = ApiTestCaseJobEntity.builder().id(ID).build();
        GrpcCaseJobRequest grpcCaseJobRequest = grpcMapper.toGrpcJobRequest(apiTestCaseJobEntity);
        assertThat(grpcCaseJobRequest.getId()).isEqualTo(apiTestCaseJobEntity.getId());
        ScheduleCaseJobEntity scheduleCaseJobEntity = ScheduleCaseJobEntity.builder().id(ID).build();
        GrpcCaseJobRequest grpcCaseJobRequest1 = grpcMapper.toGrpcJobRequest(scheduleCaseJobEntity);
        assertThat(grpcCaseJobRequest1.getId()).isEqualTo(scheduleCaseJobEntity.getId());
        ScheduleSceneCaseJobEntity scheduleSceneCaseJobEntity = ScheduleSceneCaseJobEntity.builder().id(ID).build();
        GrpcSceneCaseJobRequest grpcCaseJobRequest3 = grpcMapper.toGrpcJobRequest(scheduleSceneCaseJobEntity);
        assertThat(grpcCaseJobRequest3.getId()).isEqualTo(scheduleSceneCaseJobEntity.getId());*/
    }


}
