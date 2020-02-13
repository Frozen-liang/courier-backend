package com.sms.satp.service.impl;

import com.sms.satp.entity.StatusCodeDoc;
import com.sms.satp.entity.dto.PageDto;
import com.sms.satp.entity.dto.StatusCodeDocDto;
import com.sms.satp.mapper.StatusCodeDocMapper;
import com.sms.satp.repository.StatusCodeDocRepository;
import com.sms.satp.service.StatusCodeDocService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class StatusCodeDocServiceImpl implements StatusCodeDocService {

    private final StatusCodeDocRepository statusCodeDocRepository;
    private final StatusCodeDocMapper statusCodeDocMapper;

    public StatusCodeDocServiceImpl(
            StatusCodeDocRepository statusCodeDocRepository, StatusCodeDocMapper statusCodeDocMapper) {
        this.statusCodeDocRepository = statusCodeDocRepository;
        this.statusCodeDocMapper = statusCodeDocMapper;
    }

    @Override
    public Page<StatusCodeDocDto> page(PageDto pageDto, String projectId) {
        try {
            StatusCodeDoc statusCodeDoc = StatusCodeDoc.builder()
                .projectId(projectId)
                .build();
            Example<StatusCodeDoc> example = Example.of(statusCodeDoc);
            Sort sort = Sort.by(Direction.fromString(pageDto.getOrder()), pageDto.getSort());
            Pageable pageable = PageRequest.of(
                pageDto.getPageNumber(), pageDto.getPageSize(), sort);
            return statusCodeDocRepository.findAll(example, pageable)
                .map(statusCodeDocMapper::toDto);
        } catch (Exception e) {
            log.error("Failed to get the StatusCodeDoc page!", e);
            throw e;
        }
    }

    @Override
    public void add(StatusCodeDocDto statusCodeDocDto) {
        if (log.isDebugEnabled()) {
            log.debug(String.format("StatusCodeDocService-add()-Parameter: %s",
                statusCodeDocDto.toString()));
        }
        try {
            statusCodeDocRepository.insert(
                statusCodeDocMapper.toEntity(statusCodeDocDto));
        } catch (Exception e) {
            log.error("Failed to add the statusCodeDoc!", e);
            throw e;
        }
    }

    @Override
    public void edit(StatusCodeDocDto statusCodeDocDto) {
        if (log.isDebugEnabled()) {
            log.debug(String.format("StatusCodeDocService-edit()-Parameter: %s",
                statusCodeDocDto.toString()));
        }
        try {
            statusCodeDocRepository.save(
                statusCodeDocMapper.toEntity(statusCodeDocDto));
        } catch (Exception e) {
            log.error("Failed to edit the statusCodeDoc!", e);
            throw e;
        }
    }

    @Override
    public void deleteById(String id) {
        try {
            statusCodeDocRepository.deleteById(id);
        } catch (Exception e) {
            log.error("Failed to delete the statusCodeDoc!", e);
            throw e;
        }
    }
}