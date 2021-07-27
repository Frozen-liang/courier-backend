package com.sms.satp.service;

import com.sms.satp.dto.request.GlobalFunctionRequest;
import com.sms.satp.dto.response.GlobalFunctionResponse;
import java.util.List;
import java.util.Map;

public interface GlobalFunctionService {

    GlobalFunctionResponse findById(String id);

    List<GlobalFunctionResponse> list(String workspaceId, String functionDesc, String functionName);

    Boolean add(GlobalFunctionRequest globalFunctionRequest);

    Boolean edit(GlobalFunctionRequest globalFunctionRequest);

    Boolean delete(List<String> ids);

    Map<String, List<GlobalFunctionResponse>> findAll();

    List<GlobalFunctionResponse> pullFunction(List<String> ids);
}