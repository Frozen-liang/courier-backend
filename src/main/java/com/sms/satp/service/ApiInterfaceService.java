package com.sms.satp.service;

import com.sms.satp.entity.criteria.InterfaceCriteria;
import com.sms.satp.entity.dto.ApiInterfaceDto;
import com.sms.satp.entity.dto.DocumentImportDto;
import com.sms.satp.entity.dto.ImportWay;
import com.sms.satp.entity.dto.PageDto;
import com.sms.satp.entity.dto.SelectDto;
import java.util.List;
import org.springframework.data.domain.Page;

public interface ApiInterfaceService {

    Page<ApiInterfaceDto> page(PageDto pageDto, InterfaceCriteria interfaceCriteria);

    void add(ApiInterfaceDto apiInterfaceDto);

    void edit(ApiInterfaceDto apiInterfaceDto);

    ApiInterfaceDto findById(String id);

    void deleteById(String id);

    void importDocument(DocumentImportDto documentImportDto, ImportWay importWay);

    List<SelectDto> getAllTags(String projectId, String condition);

    void updateGroupById(List ids, String groupId);

    void deleteAll();

}