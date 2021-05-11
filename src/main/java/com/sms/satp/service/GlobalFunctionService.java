package com.sms.satp.service;

import com.sms.satp.dto.GlobalFunctionRequest;
import com.sms.satp.dto.GlobalFunctionResponse;
import java.util.List;

public interface GlobalFunctionService {

    GlobalFunctionResponse findById(String id);

    List<GlobalFunctionResponse> list(String functionDesc, String functionName);

    void add(GlobalFunctionRequest globalFunctionRequest);

    void edit(GlobalFunctionRequest globalFunctionRequest);

    void delete(String[] ids);
}