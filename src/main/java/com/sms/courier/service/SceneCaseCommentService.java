package com.sms.courier.service;

import com.sms.courier.dto.request.SceneCaseCommentRequest;
import com.sms.courier.dto.response.TreeResponse;
import java.util.List;
import org.bson.types.ObjectId;

public interface SceneCaseCommentService {

    Boolean add(SceneCaseCommentRequest sceneCaseCommentRequest);

    List<TreeResponse> list(ObjectId sceneCaseId);

    Boolean delete(List<String> ids);
}
