package com.sms.satp.service.impl;

import com.sms.satp.entity.StatusCodeDoc;
import com.sms.satp.entity.dto.PageDto;
import com.sms.satp.entity.dto.StatusCodeDocDto;
import com.sms.satp.mapper.StatusCodeDocMapper;
import com.sms.satp.repository.StatusCodeDocRepository;
import com.sms.satp.service.StatusCodeDocService;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;

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
        StatusCodeDoc statusCodeDoc = StatusCodeDoc.builder()
            .projectId(projectId)
            .build();
        Example<StatusCodeDoc> example = Example.of(statusCodeDoc);
        Sort sort = Sort.by(Direction.fromString(pageDto.getOrder()), pageDto.getSort());
        Pageable pageable = PageRequest.of(pageDto.getPageNumber(), pageDto.getPageSize(), sort);
        return statusCodeDocRepository.findAll(example, pageable)
            .map(statusCodeDocMapper::toDto);
    }

    @Override
    public void add(StatusCodeDocDto statusCodeDocDto) {
        statusCodeDocRepository.insert(
            statusCodeDocMapper.toEntity(statusCodeDocDto));
    }

    @Override
    public void edit(StatusCodeDocDto statusCodeDocDto) {
        statusCodeDocRepository.save(
            statusCodeDocMapper.toEntity(statusCodeDocDto));
    }

    @Override
    public void deleteById(String id) {
        statusCodeDocRepository.deleteById(id);
    }
}