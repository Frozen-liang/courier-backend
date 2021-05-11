package com.sms.satp.service;

import com.sms.satp.dto.ApiImportRequest;
import com.sms.satp.dto.ApiPageRequest;
import com.sms.satp.dto.ApiRequest;
import com.sms.satp.dto.ApiResponse;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

@Service
public interface ApiService {

    boolean importDocument(ApiImportRequest apiImportRequest);

    ApiResponse findById(String id);

    Page<ApiResponse> page(ApiPageRequest apiPageRequest);

    Boolean add(ApiRequest apiRequestDto);

    Boolean edit(ApiRequest apiRequestDto);

    Boolean delete(String[] ids);

}
