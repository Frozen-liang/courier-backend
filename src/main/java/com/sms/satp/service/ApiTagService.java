package com.sms.satp.service;

import com.sms.satp.common.enums.ApiTagType;
import com.sms.satp.dto.ApiTagRequest;
import com.sms.satp.dto.ApiTagResponse;
import java.util.List;

public interface ApiTagService {

    ApiTagResponse findById(String id);

    List<ApiTagResponse> list(String projectId, String tagName, ApiTagType tagType);

    void add(ApiTagRequest apiTagRequest);

    void edit(ApiTagRequest apiTagRequest);

    void delete(String id);

}
