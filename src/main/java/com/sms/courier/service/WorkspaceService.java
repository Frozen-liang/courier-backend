package com.sms.courier.service;

import com.sms.courier.dto.PageDto;
import com.sms.courier.dto.request.WorkspaceRequest;
import com.sms.courier.dto.response.ApiTestCaseResponse;
import com.sms.courier.dto.response.WorkspaceResponse;
import java.util.List;
import org.springframework.data.domain.Page;

public interface WorkspaceService {

    WorkspaceResponse findById(String id);

    List<WorkspaceResponse> list();

    Boolean add(WorkspaceRequest workspaceRequest);

    Boolean edit(WorkspaceRequest workspaceRequest);

    Boolean delete(String id);

    List<WorkspaceResponse> findByUserId();

    Page<ApiTestCaseResponse> getCase(String id, PageDto pageDto);
}