package com.sms.courier.repository;

import com.sms.courier.dto.request.ApiPageRequest;
import com.sms.courier.dto.response.ApiResponse;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;

public interface CustomizedApiRepository {

    Optional<ApiResponse> findById(String id);

    Page<ApiResponse> page(ApiPageRequest apiPageRequest);

    Boolean deleteById(String id);

    Boolean deleteByIds(List<String> ids);

    Boolean recover(List<String> ids);

    void deleteByGroupIds(List<String> groupIds);
}
