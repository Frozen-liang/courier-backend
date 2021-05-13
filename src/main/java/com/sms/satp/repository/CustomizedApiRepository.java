package com.sms.satp.repository;

import com.sms.satp.dto.request.ApiPageRequest;
import com.sms.satp.dto.response.ApiResponse;
import java.util.List;
import org.springframework.data.domain.Page;

public interface CustomizedApiRepository {

    Page<ApiResponse> page(ApiPageRequest apiPageRequest);

    Boolean deleteById(String id);

    Boolean deleteByIds(List<String> ids);
}
