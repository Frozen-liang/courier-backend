package com.sms.satp.service;

import com.sms.satp.dto.request.GlobalFunctionRequest;
import com.sms.satp.dto.response.GlobalFunctionResponse;
import java.util.List;

public interface GlobalFunctionService {

    GlobalFunctionResponse findById(String id);

    List<GlobalFunctionResponse> list(String functionDesc, String functionName);

    Boolean add(GlobalFunctionRequest globalFunctionRequest);

    Boolean edit(GlobalFunctionRequest globalFunctionRequest);

    Boolean delete(List<String> ids);
}