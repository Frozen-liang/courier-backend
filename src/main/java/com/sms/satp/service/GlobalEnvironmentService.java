package com.sms.satp.service;

import com.sms.satp.entity.dto.GlobalEnvironmentDto;
import java.util.List;
import org.bson.types.ObjectId;

public interface GlobalEnvironmentService {

    GlobalEnvironmentDto findById(ObjectId id);

    void add(GlobalEnvironmentDto globalEnvironmentDto);

    void edit(GlobalEnvironmentDto globalEnvironmentDto);

    List<GlobalEnvironmentDto> list();
}
