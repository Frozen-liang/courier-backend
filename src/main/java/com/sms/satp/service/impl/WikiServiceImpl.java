package com.sms.satp.service.impl;

import static com.sms.satp.common.ErrorCode.ADD_WIKI_ERROR;
import static com.sms.satp.common.ErrorCode.DELETE_WIKI_BY_ID_ERROR;
import static com.sms.satp.common.ErrorCode.EDIT_WIKI_ERROR;
import static com.sms.satp.common.ErrorCode.GET_WIKI_BY_ID_ERROR;
import static com.sms.satp.common.ErrorCode.GET_WIKI_PAGE_ERROR;

import com.sms.satp.common.ApiTestPlatformException;
import com.sms.satp.entity.Wiki;
import com.sms.satp.entity.dto.PageDto;
import com.sms.satp.entity.dto.WikiDto;
import com.sms.satp.mapper.WikiMapper;
import com.sms.satp.repository.WikiRepository;
import com.sms.satp.service.WikiService;
import com.sms.satp.utils.PageDtoConverter;
import java.time.LocalDateTime;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.bson.types.ObjectId;
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
            PageDtoConverter.frontMapping(pageDto);
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
        log.info("WikiService-add()-params: [Wiki]={}", wikiDto.toString());
        try {
            Wiki wiki = wikiMapper.toEntity(wikiDto);
            wiki.setId(new ObjectId().toString());
            wiki.setCreateDateTime(LocalDateTime.now());
            wikiRepository.insert(wiki);
        } catch (Exception e) {
            log.error("Failed to add the wiki!", e);
            throw new ApiTestPlatformException(ADD_WIKI_ERROR);
        }
    }

    @Override
    public void edit(WikiDto wikiDto) {
        log.info("WikiService-edit()-params: [Wiki]={}", wikiDto.toString());
        try {
            Wiki wiki = wikiMapper.toEntity(wikiDto);
            Optional<Wiki> wikiOptional = wikiRepository
                .findById(wiki.getId());
            wikiOptional.ifPresent(wikiFindById -> {
                wiki.setCreateDateTime(wikiFindById.getCreateDateTime());
                wiki.setModifyDateTime(LocalDateTime.now());
                wikiRepository.save(wiki);
            });
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

    @Override
    public WikiDto findById(String id) {
        try {
            Optional<Wiki> wikiOptional
                = wikiRepository.findById(id);
            return wikiMapper.toDto(wikiOptional.orElse(null));
        } catch (Exception e) {
            log.error("Failed to get the Schema by id!", e);
            throw new ApiTestPlatformException(GET_WIKI_BY_ID_ERROR);
        }
    }
}