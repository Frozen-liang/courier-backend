package com.sms.satp.service.impl;

import static com.sms.satp.common.ErrorCode.ADD_WIKI_ERROR;
import static com.sms.satp.common.ErrorCode.DELETE_WIKI_BY_ID_ERROR;
import static com.sms.satp.common.ErrorCode.EDIT_WIKI_ERROR;
import static com.sms.satp.common.ErrorCode.GET_WIKI_PAGE_ERROR;

import com.sms.satp.common.ApiTestPlatformException;
import com.sms.satp.entity.Wiki;
import com.sms.satp.entity.dto.PageDto;
import com.sms.satp.entity.dto.WikiDto;
import com.sms.satp.mapper.WikiMapper;
import com.sms.satp.repository.WikiRepository;
import com.sms.satp.service.WikiService;
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
public class WikiServiceImpl implements WikiService {

    private final WikiRepository wikiRepository;
    private final WikiMapper wikiMapper;

    public WikiServiceImpl(WikiRepository wikiRepository, WikiMapper wikiMapper) {
        this.wikiRepository = wikiRepository;
        this.wikiMapper = wikiMapper;
    }

    @Override
    public Page<WikiDto> page(PageDto pageDto, String projectId) {
        try {
            Wiki wiki = Wiki.builder()
                .projectId(projectId)
                .build();
            Example<Wiki> example = Example.of(wiki);
            Sort sort = Sort.by(Direction.fromString(pageDto.getOrder()), pageDto.getSort());
            Pageable pageable = PageRequest.of(
                pageDto.getPageNumber(), pageDto.getPageSize(), sort);
            return wikiRepository.findAll(example, pageable)
                .map(wikiMapper::toDto);
        } catch (Exception e) {
            log.error("Failed to get the Wiki page!", e);
            throw new ApiTestPlatformException(GET_WIKI_PAGE_ERROR);
        }
    }

    @Override
    public void add(WikiDto wikiDto) {
        if (log.isDebugEnabled()) {
            log.debug(String.format("WikiService-add()-Parameter: %s",
                wikiDto.toString()));
        }
        try {
            wikiRepository.insert(
                wikiMapper.toEntity(wikiDto));
        } catch (Exception e) {
            log.error("Failed to add the wiki!", e);
            throw new ApiTestPlatformException(ADD_WIKI_ERROR);
        }
    }

    @Override
    public void edit(WikiDto wikiDto) {
        if (log.isDebugEnabled()) {
            log.debug(String.format("WikiService-edit()-Parameter: %s",
                wikiDto.toString()));
        }
        try {
            wikiRepository.save(
                wikiMapper.toEntity(wikiDto));
        } catch (Exception e) {
            log.error("Failed to edit the wiki!", e);
            throw new ApiTestPlatformException(EDIT_WIKI_ERROR);
        }
    }

    @Override
    public void deleteById(String id) {
        try {
            wikiRepository.deleteById(id);
        } catch (Exception e) {
            log.error("Failed to delete the wiki!", e);
            throw new ApiTestPlatformException(DELETE_WIKI_BY_ID_ERROR);
        }
    }
}