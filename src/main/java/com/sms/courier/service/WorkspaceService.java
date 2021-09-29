package com.sms.courier.service;

import com.sms.courier.dto.request.WorkspaceRequest;
import com.sms.courier.dto.response.WorkspaceResponse;
import java.util.List;

public interface WorkspaceService {

    WorkspaceResponse findById(String id);

    List<WorkspaceResponse> list();

    Boolean add(WorkspaceRequest workspaceRequest);

    Boolean edit(WorkspaceRequest workspaceRequest);

    Boolean delete(String id);

    List<WorkspaceResponse> findByUserId();

    Long caseCount(String id);
}