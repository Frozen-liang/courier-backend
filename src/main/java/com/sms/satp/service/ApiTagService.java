package com.sms.satp.service;

import com.sms.satp.dto.request.ApiTagListRequest;
import com.sms.satp.dto.request.ApiTagRequest;
import com.sms.satp.dto.response.ApiTagResponse;
import java.util.List;

public interface ApiTagService {

    ApiTagResponse findById(String id);

    List<ApiTagResponse> list(ApiTagListRequest apiTagListRequest);

    Boolean add(ApiTagRequest apiTagRequest);

    Boolean edit(ApiTagRequest apiTagRequest);

    Boolean delete(List<String> ids);

}
