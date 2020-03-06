package com.sms.satp.service;

import com.sms.satp.entity.dto.ApiInterfaceDto;
import com.sms.satp.entity.dto.InterfaceGroupDto;
import com.sms.satp.entity.dto.PageDto;
import java.io.IOException;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.web.multipart.MultipartFile;

public interface ApiInterfaceService {

    Page<ApiInterfaceDto> page(PageDto pageDto, String projectId, String groupId);

    void save(String location, String documentType, String projectId);

    void save(MultipartFile multipartFile, String documentType, String projectId) throws IOException;

    void add(ApiInterfaceDto apiInterfaceDto);

    void edit(ApiInterfaceDto apiInterfaceDto);

    ApiInterfaceDto findById(String id);

    void deleteById(String id);

    List<InterfaceGroupDto> getGroupList(String projectId);

    String addGroup(InterfaceGroupDto interfaceGroupDto);

    void editGroup(InterfaceGroupDto interfaceGroupDto);

    void deleteGroup(String id);

    String addGroupByNameAndReturnId(String groupName, String projectId);
}