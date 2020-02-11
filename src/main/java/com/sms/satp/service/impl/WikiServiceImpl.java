package com.sms.satp.service.impl;

import com.sms.satp.entity.Wiki;
import com.sms.satp.entity.dto.PageDto;
import com.sms.satp.entity.dto.WikiDto;
import com.sms.satp.mapper.WikiMapper;
import com.sms.satp.repository.WikiRepository;
import com.sms.satp.service.WikiService;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;

@Service
public class WikiServiceImpl implements WikiService {

    private final WikiRepository wikiRepository;
    private final WikiMapper wikiMapper;

    public WikiServiceImpl(WikiRepository wikiRepository, WikiMapper wikiMapper) {
        this.wikiRepository = wikiRepository;
        this.wikiMapper = wikiMapper;
    }

    @Override
    public Page<WikiDto> page(PageDto pageDto, String projectId) {
        Wiki wiki = Wiki.builder()
            .projectId(projectId)
            .build();
        Example<Wiki> example = Example.of(wiki);
        Sort sort = Sort.by(Direction.fromString(pageDto.getOrder()), pageDto.getSort());
        Pageable pageable = PageRequest.of(pageDto.getPageNumber(), pageDto.getPageSize(), sort);
        return wikiRepository.findAll(example, pageable)
            .map(wikiMapper::toDto);
    }

    @Override
    public void add(WikiDto wikiDto) {
        wikiRepository.insert(
            wikiMapper.toEntity(wikiDto));
    }

    @Override
    public void edit(WikiDto wikiDto) {
        wikiRepository.save(
            wikiMapper.toEntity(wikiDto));
    }

    @Override
    public void deleteById(String id) {
        wikiRepository.deleteById(id);
    }
}