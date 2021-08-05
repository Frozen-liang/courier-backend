package com.sms.courier.service;

import com.sms.courier.dto.request.ApiTagListRequest;
import com.sms.courier.dto.request.ApiTagRequest;
import com.sms.courier.dto.response.ApiTagResponse;
import java.util.List;

public interface ApiTagService {

    ApiTagResponse findById(String id);

    List<ApiTagResponse> list(ApiTagListRequest apiTagListRequest);

    Boolean add(ApiTagRequest apiTagRequest);

    Boolean edit(ApiTagRequest apiTagRequest);

    Boolean delete(List<String> ids);

}
