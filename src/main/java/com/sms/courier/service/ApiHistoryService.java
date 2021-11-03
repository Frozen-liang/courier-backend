package com.sms.courier.service;

import com.sms.courier.dto.response.ApiHistoryDetailResponse;
import com.sms.courier.dto.response.ApiHistoryListResponse;
import java.util.List;
import org.bson.types.ObjectId;

public interface ApiHistoryService {

    List<ApiHistoryListResponse> findByApiId(ObjectId apiId);

    ApiHistoryDetailResponse findById(String id);
}
