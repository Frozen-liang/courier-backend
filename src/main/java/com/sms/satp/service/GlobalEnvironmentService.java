package com.sms.satp.service;

import com.sms.satp.entity.dto.GlobalEnvironmentDto;
import java.util.List;

public interface GlobalEnvironmentService {

    GlobalEnvironmentDto findById(String id);

    void add(GlobalEnvironmentDto globalEnvironmentDto);

    void edit(GlobalEnvironmentDto globalEnvironmentDto);

    List<GlobalEnvironmentDto> list();

    void delete(String[] ids);

}
