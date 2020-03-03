package com.sms.satp.service;

import com.sms.satp.entity.dto.PageDto;
import com.sms.satp.entity.dto.WikiDto;
import org.springframework.data.domain.Page;

public interface WikiService {

    Page<WikiDto> page(PageDto pageDto, String projectId);

    void add(WikiDto wikiDto);

    void edit(WikiDto wikiDto);

    void deleteById(String id);

    WikiDto findById(String id);
}
