package com.sms.courier.service;

import com.sms.courier.dto.request.ApiCommentRequest;
import com.sms.courier.dto.response.TreeResponse;
import java.util.List;
import org.bson.types.ObjectId;

public interface ApiCommentService {

    List<TreeResponse> list(ObjectId apiId);

    Boolean add(ApiCommentRequest apiCommentRequest);

    Boolean delete(List<String> ids);
}
