package com.sms.satp.service;

import com.sms.satp.dto.AddSceneCaseRequest;
import com.sms.satp.dto.PageDto;
import com.sms.satp.dto.SceneCaseResponse;
import com.sms.satp.dto.SceneTemplateResponse;
import com.sms.satp.dto.SearchSceneCaseRequest;
import com.sms.satp.dto.UpdateSceneCaseRequest;
import com.sms.satp.dto.UpdateSceneTemplateRequest;
import java.util.List;
import org.springframework.data.domain.Page;

public interface SceneCaseService {

    Boolean add(AddSceneCaseRequest addSceneCaseRequest);

    Boolean deleteByIds(List<String> ids);

    Boolean edit(UpdateSceneCaseRequest updateSceneCaseRequest);

    Page<SceneCaseResponse> page(PageDto pageDto, String projectId);

    Page<SceneCaseResponse> search(SearchSceneCaseRequest searchDto, String projectId);

    SceneTemplateResponse getConn(String id);

    Boolean editConn(UpdateSceneTemplateRequest updateSceneTemplateRequest);
}
