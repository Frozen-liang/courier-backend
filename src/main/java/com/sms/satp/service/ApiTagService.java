package com.sms.satp.service;

import com.sms.satp.dto.request.ApiTagRequest;
import com.sms.satp.dto.response.ApiTagResponse;
import java.util.List;

public interface ApiTagService {

    ApiTagResponse findById(String id);

    List<ApiTagResponse> list(String projectId, String tagName, Integer tagType);

    Boolean add(ApiTagRequest apiTagRequest);

    Boolean edit(ApiTagRequest apiTagRequest);

    Boolean delete(List<String> ids);

}
