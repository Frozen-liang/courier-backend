package com.sms.satp.repository;

import com.sms.satp.dto.request.SearchSceneCaseRequest;
import com.sms.satp.entity.scenetest.SceneCase;
import org.springframework.data.domain.Page;

public interface CustomizedSceneCaseRepository {

    Page<SceneCase> search(SearchSceneCaseRequest searchDto, String projectId);
}
