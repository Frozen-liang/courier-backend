package com.sms.satp.service;

import com.sms.satp.entity.dto.ApiInterfaceDto;
import com.sms.satp.entity.dto.PageDto;
import java.io.IOException;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.web.multipart.MultipartFile;

public interface ApiInterfaceService {

    List<ApiInterfaceDto> list(String projectId);

    Page<ApiInterfaceDto> page(PageDto pageDto, String projectId);

    void save(String location, String documentType, String projectId);

    void save(MultipartFile multipartFile, String documentType, String projectId) throws IOException;

    void add(ApiInterfaceDto apiInterfaceDto);

    ApiInterfaceDto getApiInterfaceById(String id);

    void deleteById(String id);
}