package com.sms.satp.service;

import com.sms.satp.entity.dto.GlobalFunctionDto;
import java.util.List;

public interface GlobalFunctionService {

    GlobalFunctionDto findById(String id);

    List<GlobalFunctionDto> list(String functionDesc, String functionName);

    void add(GlobalFunctionDto globalFunctionDto);

    void edit(GlobalFunctionDto globalFunctionDto);

    void delete(String id);
}