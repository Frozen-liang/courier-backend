package com.sms.satp.service;

import com.sms.satp.dto.PageDto;
import com.sms.satp.dto.request.AddSceneCaseRequest;
import com.sms.satp.dto.request.SearchSceneCaseRequest;
import com.sms.satp.dto.request.UpdateSceneCaseRequest;
import com.sms.satp.dto.request.UpdateSceneTemplateRequest;
import com.sms.satp.dto.response.SceneCaseResponse;
import com.sms.satp.dto.response.SceneTemplateResponse;
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
