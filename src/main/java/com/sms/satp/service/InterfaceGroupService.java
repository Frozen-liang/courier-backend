package com.sms.satp.service;

import com.sms.satp.entity.dto.InterfaceGroupDto;
import java.util.List;

public interface InterfaceGroupService {

    List<InterfaceGroupDto> getGroupList(String projectId, String condition);

    String addGroup(InterfaceGroupDto interfaceGroupDto);

    void editGroup(InterfaceGroupDto interfaceGroupDto);

    void deleteGroup(String id);

}