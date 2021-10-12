package com.sms.courier.mapper;

import com.sms.courier.dto.request.ApiCommentRequest;
import com.sms.courier.dto.response.ApiCommentResponse;
import com.sms.courier.entity.api.ApiCommentEntity;
import java.util.List;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", injectionStrategy = InjectionStrategy.CONSTRUCTOR,
    unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ApiCommentMapper {

    ApiCommentResponse toDto(ApiCommentEntity apiComment);

    List<ApiCommentResponse> toDtoList(List<ApiCommentEntity> apiCommentList);

    ApiCommentEntity toEntity(ApiCommentRequest apiCommentRequest);
}
