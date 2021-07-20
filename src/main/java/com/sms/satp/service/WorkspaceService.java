package com.sms.satp.service;

import com.sms.satp.dto.request.WorkspaceRequest;
import com.sms.satp.dto.response.WorkspaceResponse;
import java.util.List;

public interface WorkspaceService {

    WorkspaceResponse findById(String id);

    List<WorkspaceResponse> list();

    Boolean add(WorkspaceRequest workspaceRequest);

    Boolean edit(WorkspaceRequest workspaceRequest);

    Boolean delete(String id);

    List<WorkspaceResponse> findByUserId();
}