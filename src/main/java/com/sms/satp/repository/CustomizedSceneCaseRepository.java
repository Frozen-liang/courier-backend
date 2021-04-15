package com.sms.satp.repository;

import com.sms.satp.entity.scenetest.SceneCase;
import com.sms.satp.entity.scenetest.SceneCaseSearchDto;
import org.springframework.data.domain.Page;

public interface CustomizedSceneCaseRepository {

    Page<SceneCase> search(SceneCaseSearchDto searchDto, String projectId);
}
