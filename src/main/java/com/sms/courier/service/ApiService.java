package com.sms.courier.service;

import com.sms.courier.dto.request.ApiCaseRequest;
import com.sms.courier.dto.request.ApiImportRequest;
import com.sms.courier.dto.request.ApiPageRequest;
import com.sms.courier.dto.request.ApiRequest;
import com.sms.courier.dto.request.BatchUpdateByIdRequest;
import com.sms.courier.dto.response.ApiAndCaseResponse;
import com.sms.courier.dto.response.ApiPageResponse;
import com.sms.courier.dto.response.ApiResponse;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

@Service
public interface ApiService {

    boolean importDocumentByFile(ApiImportRequest apiImportRequest);

    Boolean syncApiByProImpSourceIds(List<String> proImpSourceIds);

    ApiResponse findById(String id);

    Page<ApiPageResponse> page(ApiPageRequest apiPageRequest);

    Boolean add(ApiRequest apiRequestDto);

    Boolean edit(ApiRequest apiRequestDto);

    Boolean delete(List<String> ids);

    Boolean deleteByIds(List<String> ids);

    Boolean deleteAll(String projectId);

    Boolean recover(List<String> ids);

    Boolean batchUpdateByIds(BatchUpdateByIdRequest<Object> batchUpdateRequest);

    Boolean resetApiVersion(String historyId);

    List<ApiAndCaseResponse> queryByApiPathAndRequestMethod(String projectId, List<ApiCaseRequest> requests);
}
