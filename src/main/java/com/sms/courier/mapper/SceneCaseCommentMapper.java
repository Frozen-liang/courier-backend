package com.sms.courier.mapper;

import com.sms.courier.dto.request.SceneCaseCommentRequest;
import com.sms.courier.dto.response.SceneCaseCommentResponse;
import com.sms.courier.entity.scenetest.SceneCaseCommentEntity;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", injectionStrategy = InjectionStrategy.CONSTRUCTOR,
    unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface SceneCaseCommentMapper {

    SceneCaseCommentResponse toDto(SceneCaseCommentEntity sceneCaseCommentEntity);

    SceneCaseCommentEntity toEntity(SceneCaseCommentRequest sceneCaseCommentRequest);

}
