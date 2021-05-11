package com.sms.satp.service;

import com.sms.satp.dto.ApiImportRequest;
import com.sms.satp.dto.ApiPageRequestDto;
import com.sms.satp.dto.ApiRequestDto;
import com.sms.satp.dto.ApiResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

@Service
public interface ApiService {

    boolean importDocument(ApiImportRequest apiImportRequest);

    ApiResponseDto findById(String id);

    Page<ApiResponseDto> page(ApiPageRequestDto apiPageRequestDto);

    Boolean add(ApiRequestDto apiRequestDto);

    Boolean edit(ApiRequestDto apiRequestDto);

    Boolean delete(String[] ids);

}
