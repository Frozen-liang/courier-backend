package com.sms.satp.service;

import com.sms.satp.dto.request.ApiImportRequest;
import com.sms.satp.dto.request.ApiPageRequest;
import com.sms.satp.dto.request.ApiRequest;
import com.sms.satp.dto.response.ApiResponse;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

@Service
public interface ApiService {

    boolean importDocumentByFile(ApiImportRequest apiImportRequest);

    Boolean importDocumentByProImpSourceIds(List<String> proImpSourceIds);

    ApiResponse findById(String id);

    Page<ApiResponse> page(ApiPageRequest apiPageRequest);

    Boolean add(ApiRequest apiRequestDto);

    Boolean edit(ApiRequest apiRequestDto);

    Boolean delete(List<String> ids);

    Boolean deleteByIds(List<String> ids);

    Boolean deleteAll();

    Boolean recover(List<String> ids);
}
