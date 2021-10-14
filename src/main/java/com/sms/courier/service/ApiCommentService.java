package com.sms.courier.service;

import com.sms.courier.dto.request.ApiCommentRequest;
import com.sms.courier.dto.response.ApiCommentResponse;
import java.util.List;
import org.bson.types.ObjectId;

public interface ApiCommentService {

    ApiCommentResponse findById(String id);

    List<ApiCommentResponse> list(ObjectId apiId);

    Boolean add(ApiCommentRequest apiCommentRequest);

    Boolean edit(ApiCommentRequest apiCommentRequest);

    Boolean delete(List<String> ids);
}
