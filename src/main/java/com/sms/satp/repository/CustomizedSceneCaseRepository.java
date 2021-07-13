package com.sms.satp.repository;

import com.sms.satp.dto.request.SearchSceneCaseRequest;
import com.sms.satp.dto.response.SceneCaseResponse;
import org.bson.types.ObjectId;
import org.springframework.data.domain.Page;

public interface CustomizedSceneCaseRepository {

    Page<SceneCaseResponse> search(SearchSceneCaseRequest searchDto, ObjectId projectId);
}
