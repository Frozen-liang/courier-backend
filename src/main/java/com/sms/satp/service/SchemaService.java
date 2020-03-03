package com.sms.satp.service;

import com.sms.satp.entity.dto.PageDto;
import com.sms.satp.entity.dto.SchemaDto;
import org.springframework.data.domain.Page;

public interface SchemaService {
    
    Page<SchemaDto> page(PageDto pageDto, String projectId);

    void add(SchemaDto schemaDto);

    void edit(SchemaDto schemaDto);

    void deleteById(String id);

    SchemaDto findById(String id);

}
