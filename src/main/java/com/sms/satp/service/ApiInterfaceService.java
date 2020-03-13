package com.sms.satp.service;

import com.sms.satp.entity.dto.ApiInterfaceDto;
import com.sms.satp.entity.dto.DocumentImportDto;
import com.sms.satp.entity.dto.ImportWay;
import com.sms.satp.entity.dto.PageDto;
import org.springframework.data.domain.Page;

public interface ApiInterfaceService {

    Page<ApiInterfaceDto> page(PageDto pageDto, String projectId, String groupId);

    void add(ApiInterfaceDto apiInterfaceDto);

    void edit(ApiInterfaceDto apiInterfaceDto);

    ApiInterfaceDto findById(String id);

    void deleteById(String id);

    void importDocument(DocumentImportDto documentImportDto, ImportWay importWay);

}