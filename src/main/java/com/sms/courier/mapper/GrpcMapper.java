package com.sms.courier.mapper;

import com.sms.courier.engine.grpc.api.v1.GrpcCaseJobReport;
import com.sms.courier.engine.grpc.api.v1.GrpcCaseJobRequest;
import com.sms.courier.engine.grpc.api.v1.GrpcCaseReport;
import com.sms.courier.engine.grpc.api.v1.GrpcFunction;
import com.sms.courier.engine.grpc.api.v1.GrpcHttpStatusVerification;
import com.sms.courier.engine.grpc.api.v1.GrpcJobApi;
import com.sms.courier.engine.grpc.api.v1.GrpcJobApiTestCase;
import com.sms.courier.engine.grpc.api.v1.GrpcJobSceneCaseApi;
import com.sms.courier.engine.grpc.api.v1.GrpcMatchParamInfo;
import com.sms.courier.engine.grpc.api.v1.GrpcParamInfo;
import com.sms.courier.engine.grpc.api.v1.GrpcResponseHeadersVerification;
import com.sms.courier.engine.grpc.api.v1.GrpcResponseResultVerification;
import com.sms.courier.engine.grpc.api.v1.GrpcResponseTimeVerification;
import com.sms.courier.engine.grpc.api.v1.GrpcSceneCaseJobReport;
import com.sms.courier.engine.grpc.api.v1.GrpcSceneCaseJobRequest;
import com.sms.courier.entity.api.common.HttpStatusVerification;
import com.sms.courier.entity.api.common.MatchParamInfo;
import com.sms.courier.entity.api.common.ParamInfo;
import com.sms.courier.entity.api.common.ResponseHeadersVerification;
import com.sms.courier.entity.api.common.ResponseResultVerification;
import com.sms.courier.entity.api.common.ResponseTimeVerification;
import com.sms.courier.entity.function.GlobalFunctionEntity;
import com.sms.courier.entity.function.ProjectFunctionEntity;
import com.sms.courier.entity.job.ApiTestCaseJobEntity;
import com.sms.courier.entity.job.ApiTestCaseJobReport;
import com.sms.courier.entity.job.JobSceneCaseApi;
import com.sms.courier.entity.job.SceneCaseJobEntity;
import com.sms.courier.entity.job.SceneCaseJobReport;
import com.sms.courier.entity.job.ScheduleCaseJobEntity;
import com.sms.courier.entity.job.ScheduleSceneCaseJobEntity;
import com.sms.courier.entity.job.common.CaseReport;
import com.sms.courier.entity.job.common.JobApi;
import com.sms.courier.entity.job.common.JobApiTestCase;
import com.sms.courier.utils.EnumCommonUtils;
import java.time.Duration;
import java.util.Objects;
import org.mapstruct.CollectionMappingStrategy;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValueCheckStrategy;
import org.mapstruct.ReportingPolicy;
import org.springframework.boot.convert.ApplicationConversionService;
import org.springframework.core.convert.TypeDescriptor;

@Mapper(componentModel = "spring", injectionStrategy = InjectionStrategy.CONSTRUCTOR,
    nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS,
    collectionMappingStrategy = CollectionMappingStrategy.ADDER_PREFERRED,
    unmappedTargetPolicy = ReportingPolicy.IGNORE,
    uses = {EnumCommonUtils.class})
public interface GrpcMapper {

    GrpcSceneCaseJobRequest toGrpcJobRequest(SceneCaseJobEntity sceneCaseJobEntity);

    GrpcSceneCaseJobRequest toGrpcJobRequest(ScheduleSceneCaseJobEntity sceneCaseJobEntity);

    GrpcCaseJobRequest toGrpcJobRequest(ApiTestCaseJobEntity apiTestCaseJobEntity);

    GrpcCaseJobRequest toGrpcJobRequest(ScheduleCaseJobEntity scheduleCaseJobEntity);


    @Mapping(target = "isRequired", source = "required")
    @Mapping(target = "isExtract", source = "extract")
    GrpcParamInfo toGrpcParamInfo(ParamInfo paramInfo);

    @Mapping(target = "isVerificationParamType", source = "verificationParamType")
    @Mapping(target = "isVerificationArrayElement", source = "verificationArrayElement")
    @Mapping(target = "isVerify", source = "verify")
    GrpcMatchParamInfo toGrpcMatchParamInfo(MatchParamInfo matchParamInfo);

    @Mapping(target = "isLock", source = "lock")
    GrpcJobSceneCaseApi toGrpcJobSceneCaseApi(JobSceneCaseApi jobSceneCaseApi);

    @Mapping(target = "isExecute", source = "execute")
    GrpcJobApiTestCase toGrpcJobApiTestCase(JobApiTestCase jobSceneCaseApi);

    GrpcJobApi toGrpcJobApiResponse(JobApi jobApi);

    @Mapping(target = "isCheckStatus", source = "checkStatus")
    GrpcHttpStatusVerification toGrpcHttpStatusVerification(
        HttpStatusVerification httpStatusVerification);

    @Mapping(target = "isCheckStatus", source = "checkStatus")
    GrpcResponseResultVerification toGrpcResponseResultVerification(
        ResponseResultVerification responseResultVerification);

    @Mapping(target = "isCheckStatus", source = "checkStatus")
    GrpcResponseHeadersVerification toResponseHeadersVerification(
        ResponseHeadersVerification responseResultVerification);

    @Mapping(target = "isCheckStatus", source = "checkStatus")
    @Mapping(target = "timeoutLimit",
        expression = "java(durationToString(responseTimeVerification.getTimeoutLimit()))")
    GrpcResponseTimeVerification toResponseTimeVerification(
        ResponseTimeVerification responseTimeVerification);

    @Mapping(target = "jobStatus",
        expression = "java(com.sms.courier.common.enums.JobStatus.getType(jobReport.getJobStatus()))")
    SceneCaseJobReport toJobReport(GrpcSceneCaseJobReport jobReport);

    @Mapping(target = "jobStatus",
        expression = "java(com.sms.courier.common.enums.JobStatus.getType(jobReport.getJobStatus()))")
    ApiTestCaseJobReport toJobReport(GrpcCaseJobReport jobReport);

    @Mapping(target = "requestMethod",
        expression = "java(com.sms.courier.common.enums.RequestMethod.getType(grpcCaseReport.getRequestMethod()))")
    @Mapping(target = "isSuccess",
        expression = "java(com.sms.courier.common.enums.ResultType.getType(grpcCaseReport.getIsSuccess()))")
    CaseReport toCaseReport(GrpcCaseReport grpcCaseReport);

    GrpcFunction toGrpcFunction(ProjectFunctionEntity projectFunction);

    @Mapping(target = "isGlobal", expression = "java(true)")
    GrpcFunction toGrpcFunction(GlobalFunctionEntity globalFunction);

    default String durationToString(Duration duration) {
        if (Objects.isNull(duration)) {
            return "";
        }
        return String.valueOf(ApplicationConversionService.getSharedInstance().convert(duration,
            TypeDescriptor.valueOf(duration.getClass()), TypeDescriptor.valueOf(Long.class)));
    }
}
