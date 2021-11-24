package com.sms.courier.repository;

import com.sms.courier.dto.request.ApiIncludeCaseRequest;
import com.sms.courier.dto.request.ApiPageRequest;
import com.sms.courier.dto.request.UpdateRequest;
import com.sms.courier.dto.response.ApiPageResponse;
import com.sms.courier.dto.response.ApiResponse;
import java.util.List;
import java.util.Optional;
import org.bson.types.ObjectId;
import org.springframework.data.domain.Page;

public interface CustomizedApiRepository {

    Optional<ApiResponse> findById(String id);

    Page<ApiPageResponse> page(ApiPageRequest apiPageRequest);

    Boolean deleteById(String id);

    Boolean deleteByIds(List<String> ids);

    Boolean recover(List<String> ids);

    void deleteByGroupIds(List<String> groupIds);

    Boolean updateFieldByIds(List<String> ids, UpdateRequest<Object> updateRequest);

    Boolean update(String json);

    Long sceneCount(ObjectId projectId);

    Long caseCount(ObjectId projectId);

    Page<ApiPageResponse> sceneCountPage(ApiIncludeCaseRequest request);

    Page<ApiPageResponse> caseCountPage(ApiIncludeCaseRequest request);

    Long count(List<String> projectId);
}
