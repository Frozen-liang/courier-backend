package com.sms.satp.service.impl;

import static com.sms.satp.common.ErrorCode.ADD_SCHEMA_ERROR;
import static com.sms.satp.common.ErrorCode.DELETE_SCHEMA_BY_ID_ERROR;
import static com.sms.satp.common.ErrorCode.EDIT_SCHEMA_ERROR;
import static com.sms.satp.common.ErrorCode.GET_SCHEMA_BY_ID_ERROR;
import static com.sms.satp.common.ErrorCode.GET_SCHEMA_PAGE_ERROR;

import com.sms.satp.common.ApiTestPlatformException;
import com.sms.satp.entity.Schema;
import com.sms.satp.entity.dto.PageDto;
import com.sms.satp.entity.dto.SchemaDto;
import com.sms.satp.mapper.SchemaMapper;
import com.sms.satp.repository.SchemaRepository;
import com.sms.satp.service.SchemaService;
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
public class SchemaServiceImpl implements SchemaService {
    
    private final SchemaRepository schemaRepository;
    private final SchemaMapper schemaMapper;
    
    public SchemaServiceImpl(SchemaRepository schemaRepository, SchemaMapper schemaMapper) {
        this.schemaRepository = schemaRepository;
        this.schemaMapper = schemaMapper;
    }

    @Override
    public Page<SchemaDto> page(PageDto pageDto, String projectId) {
        try {
            PageDtoConverter.frontMapping(pageDto);
            Schema schema = Schema.builder()
                .projectId(projectId)
                .build();
            Example<Schema> example = Example.of(schema);
            Sort sort = Sort.by(Direction.fromString(pageDto.getOrder()), pageDto.getSort());
            Pageable pageable = PageRequest.of(
                pageDto.getPageNumber(), pageDto.getPageSize(), sort);
            return schemaRepository.findAll(example, pageable)
                .map(schemaMapper::toDto);
        } catch (Exception e) {
            log.error("Failed to get the Schema page!", e);
            throw new ApiTestPlatformException(GET_SCHEMA_PAGE_ERROR);
        }
    }

    @Override
    public void add(SchemaDto schemaDto) {
        log.info("SchemaService-add()-params: [Schema]={}", schemaDto.toString());
        try {
            Schema schema = schemaMapper.toEntity(schemaDto);
            schema.setId(new ObjectId().toString());
            schema.setCreateDateTime(LocalDateTime.now());
            schemaRepository.insert(schema);
        } catch (Exception e) {
            log.error("Failed to add the schema!", e);
            throw new ApiTestPlatformException(ADD_SCHEMA_ERROR);
        }
    }

    @Override
    public void edit(SchemaDto schemaDto) {
        log.info("SchemaService-edit()-params: [Schema]={}", schemaDto.toString());
        try {
            Schema schema = schemaMapper.toEntity(schemaDto);
            Optional<Schema> schemaOptional = schemaRepository
                .findById(schema.getId());
            schemaOptional.ifPresent(wikiFindById -> {
                schema.setCreateDateTime(wikiFindById.getCreateDateTime());
                schema.setModifyDateTime(LocalDateTime.now());
                schemaRepository.save(schema);
            });
        } catch (Exception e) {
            log.error("Failed to edit the schema!", e);
            throw new ApiTestPlatformException(EDIT_SCHEMA_ERROR);
        }
    }

    @Override
    public void deleteById(String id) {
        try {
            schemaRepository.deleteById(id);
        } catch (Exception e) {
            log.error("Failed to delete the schema!", e);
            throw new ApiTestPlatformException(DELETE_SCHEMA_BY_ID_ERROR);
        }
    }

    @Override
    public SchemaDto findById(String id) {
        try {
            Optional<Schema> schemaOptional
                = schemaRepository.findById(id);
            return schemaMapper.toDto(schemaOptional.orElse(null));
        } catch (Exception e) {
            log.error("Failed to get the Schema by id!", e);
            throw new ApiTestPlatformException(GET_SCHEMA_BY_ID_ERROR);
        }
    }
}