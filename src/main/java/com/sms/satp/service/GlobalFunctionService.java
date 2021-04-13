package com.sms.satp.service;

import com.sms.satp.entity.dto.GlobalFunctionDto;
import java.util.List;
import org.bson.types.ObjectId;

public interface GlobalFunctionService {

    GlobalFunctionDto findById(ObjectId id);

    List<GlobalFunctionDto> list(String functionDesc, String functionName);

    void add(GlobalFunctionDto globalFunctionDto);

    void edit(GlobalFunctionDto globalFunctionDto);

    void delete(ObjectId id);
}