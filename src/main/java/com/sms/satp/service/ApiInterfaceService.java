package com.sms.satp.service;

import com.sms.satp.entity.dto.ApiInterfaceDto;
import com.sms.satp.entity.dto.PageDto;
import java.util.List;
import org.springframework.data.domain.Page;

public interface ApiInterfaceService {

    List<ApiInterfaceDto> list(String projectId);

    Page<ApiInterfaceDto> page(PageDto pageDto, String projectId);

    void save(String url, String documentType, String projectId);

    void add(ApiInterfaceDto apiInterfaceDto);

    ApiInterfaceDto getApiInterfaceById(String id);

    void deleteById(String id);
}