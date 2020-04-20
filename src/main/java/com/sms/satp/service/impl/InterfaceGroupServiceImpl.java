package com.sms.satp.service.impl;

import static com.sms.satp.common.ErrorCode.ADD_INTERFACE_GROUP_ERROR;
import static com.sms.satp.common.ErrorCode.DELETE_INTERFACE_GROUP_BY_ID_ERROR;
import static com.sms.satp.common.ErrorCode.EDIT_INTERFACE_GROUP_ERROR;
import static com.sms.satp.common.ErrorCode.GET_INTERFACE_GROUP_LIST_ERROR;

import com.sms.satp.common.ApiTestPlatformException;
import com.sms.satp.entity.InterfaceGroup;
import com.sms.satp.entity.dto.InterfaceGroupDto;
import com.sms.satp.mapper.InterfaceGroupMapper;
import com.sms.satp.repository.InterfaceGroupRepository;
import com.sms.satp.service.InterfaceGroupService;
import java.util.List;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.bson.types.ObjectId;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class InterfaceGroupServiceImpl implements InterfaceGroupService {

    private final InterfaceGroupRepository interfaceGroupRepository;
    private final InterfaceGroupMapper interfaceGroupMapper;

    private static final String REG_ALL = ".*";

    public InterfaceGroupServiceImpl(InterfaceGroupRepository interfaceGroupRepository,
        InterfaceGroupMapper interfaceGroupMapper) {
        this.interfaceGroupRepository = interfaceGroupRepository;
        this.interfaceGroupMapper = interfaceGroupMapper;
    }

    @Override
    public List<InterfaceGroupDto> getGroupList(String projectId, String condition) {
        try {
            InterfaceGroup interfaceGroup = InterfaceGroup.builder().projectId(projectId)
                .name(StringUtils.isNotBlank(condition) ? REG_ALL + condition + REG_ALL : REG_ALL).build();
            ExampleMatcher exampleMatcher = ExampleMatcher.matching()
                .withMatcher("project_id", ExampleMatcher.GenericPropertyMatchers.exact())
                .withMatcher("name", ExampleMatcher.GenericPropertyMatchers.regex());
            Example<InterfaceGroup> example = Example.of(interfaceGroup, exampleMatcher);
            return interfaceGroupMapper.toDtoList(interfaceGroupRepository.findAll(example));
        } catch (Exception e) {
            log.error("Failed to get the InterfaceGroup list!", e);
            throw new ApiTestPlatformException(GET_INTERFACE_GROUP_LIST_ERROR);
        }
    }

    @Override
    public String addGroup(InterfaceGroupDto interfaceGroupDto) {
        log.info(
            "InterfaceGroupService-addGroup()-params: [groupName]={}, [projectId]={}",
            interfaceGroupDto.getName(), interfaceGroupDto.getProjectId());
        try {
            InterfaceGroup interfaceGroup = interfaceGroupMapper.toEntity(interfaceGroupDto);
            interfaceGroup.setId(new ObjectId().toString());
            interfaceGroupRepository.insert(interfaceGroup);
            return interfaceGroup.getId();
        } catch (Exception e) {
            log.error("Failed to add the InterfaceGroup!", e);
            throw new ApiTestPlatformException(ADD_INTERFACE_GROUP_ERROR);
        }
    }

    @Override
    public void editGroup(InterfaceGroupDto interfaceGroupDto) {
        log.info(
            "InterfaceGroupService-editGroup()-params: [groupName]={}, [projectId]={}",
            interfaceGroupDto.getName(), interfaceGroupDto.getProjectId());
        try {
            InterfaceGroup interfaceGroup = interfaceGroupMapper.toEntity(interfaceGroupDto);
            Optional<InterfaceGroup> interfaceGroupOptional = interfaceGroupRepository
                .findById(interfaceGroup.getId());
            if (interfaceGroupOptional.isPresent()) {
                interfaceGroupRepository.save(interfaceGroup);
            }
        } catch (Exception e) {
            log.error("Failed to add the InterfaceGroup!", e);
            throw new ApiTestPlatformException(EDIT_INTERFACE_GROUP_ERROR);
        }
    }

    @Override
    public void deleteGroup(String id) {
        try {
            interfaceGroupRepository.deleteById(id);
        } catch (Exception e) {
            log.error("Failed to delete the InterfaceGroup!", e);
            throw new ApiTestPlatformException(DELETE_INTERFACE_GROUP_BY_ID_ERROR);
        }
    }
}