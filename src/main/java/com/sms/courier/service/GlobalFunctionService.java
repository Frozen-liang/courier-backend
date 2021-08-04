package com.sms.courier.service;

import com.sms.courier.dto.request.GlobalFunctionRequest;
import com.sms.courier.dto.response.GlobalFunctionResponse;
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