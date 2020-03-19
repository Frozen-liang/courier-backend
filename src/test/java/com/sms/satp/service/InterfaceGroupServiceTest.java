package com.sms.satp.service;

import static com.sms.satp.common.ErrorCode.ADD_INTERFACE_GROUP_ERROR;
import static com.sms.satp.common.ErrorCode.DELETE_INTERFACE_GROUP_BY_ID_ERROR;
import static com.sms.satp.common.ErrorCode.EDIT_INTERFACE_GROUP_ERROR;
import static com.sms.satp.common.ErrorCode.GET_INTERFACE_GROUP_LIST_ERROR;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.sms.satp.ApplicationTests;
import com.sms.satp.common.ApiTestPlatformException;
import com.sms.satp.entity.InterfaceGroup;
import com.sms.satp.entity.dto.InterfaceGroupDto;
import com.sms.satp.mapper.InterfaceGroupMapper;
import com.sms.satp.repository.InterfaceGroupRepository;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.data.domain.Example;

@SpringBootTest(classes = ApplicationTests.class)
@DisplayName("Test cases for InterfaceGroupService")
public class InterfaceGroupServiceTest {

    @MockBean
    private InterfaceGroupRepository interfaceGroupRepository;
    
    @SpyBean
    InterfaceGroupService interfaceGroupService;

    @SpyBean
    InterfaceGroupMapper interfaceGroupMapper;

    private final static int LIST_SIZE = 10;
    private final static String PROJECT_ID = "25";
    private final static String GROUP_ID = "25";
    private final static String GROUP_NAME = "name";

    @Test
    @DisplayName("Test the getGroupList method in the apiInterface service")
    void getGroupList_test() {
        List<InterfaceGroup> interfaceGroupList = new ArrayList<>();
        for (int i = 0; i < LIST_SIZE; i++) {
            interfaceGroupList.add(
                InterfaceGroup.builder()
                    .projectId(PROJECT_ID)
                    .name(GROUP_NAME)
                    .build());
        }
        InterfaceGroup interfaceGroup = InterfaceGroup.builder().projectId(PROJECT_ID).build();
        Example<InterfaceGroup> example = Example.of(interfaceGroup);
        when(interfaceGroupRepository.findAll(example)).thenReturn(interfaceGroupList);
        List<InterfaceGroupDto> interfaceGroupDtoList = interfaceGroupService.getGroupList(PROJECT_ID);
        assertThat(interfaceGroupDtoList).allMatch(result -> StringUtils.equals(result.getName(), GROUP_NAME));
        assertThat(interfaceGroupDtoList.size()).isEqualTo(LIST_SIZE);
    }

    @Test
    @DisplayName("Test the addGroup method in the apiInterface service")
    void addGroup_test() {
        InterfaceGroupDto interfaceGroupDto = InterfaceGroupDto.builder().build();
        InterfaceGroup interfaceGroup = interfaceGroupMapper.toEntity(interfaceGroupDto);
        when(interfaceGroupRepository.insert(interfaceGroup)).thenReturn(interfaceGroup);
        interfaceGroupService.addGroup(interfaceGroupDto);
        verify(interfaceGroupRepository, times(1)).insert(any(InterfaceGroup.class));
    }

    @Test
    @DisplayName("Test the editGroup method in the apiInterface service")
    void editGroup_test() {
        InterfaceGroupDto interfaceGroupDto = InterfaceGroupDto.builder().id(GROUP_ID).build();
        InterfaceGroup interfaceGroup = interfaceGroupMapper.toEntity(interfaceGroupDto);
        when(interfaceGroupRepository.findById(GROUP_ID)).thenReturn(Optional.of(InterfaceGroup.builder().build()));
        when(interfaceGroupRepository.save(interfaceGroup)).thenReturn(interfaceGroup);
        interfaceGroupService.editGroup(interfaceGroupDto);
        verify(interfaceGroupRepository, times(1)).save(any(InterfaceGroup.class));
    }

    @Test
    @DisplayName("Test the deleteGroup method in the apiInterface service")
    void deleteGroup_test() {
        doNothing().when(interfaceGroupRepository).deleteById(GROUP_ID);
        interfaceGroupService.deleteGroup(GROUP_ID);
        verify(interfaceGroupRepository, times(1)).deleteById(GROUP_ID);
    }

    @Test
    @DisplayName("An exception occurred while getting InterfaceGroup list")
    void getGroupList_exception_test() {
        InterfaceGroup interfaceGroup = InterfaceGroup.builder().projectId(PROJECT_ID).build();
        Example<InterfaceGroup> example = Example.of(interfaceGroup);
        doThrow(new RuntimeException()).when(interfaceGroupRepository).findAll(example);
        assertThatThrownBy(() -> interfaceGroupService.getGroupList(PROJECT_ID))
            .isInstanceOf(ApiTestPlatformException.class)
            .extracting("code").isEqualTo(GET_INTERFACE_GROUP_LIST_ERROR.getCode());
    }

    @Test
    @DisplayName("An exception occurred while adding InterfaceGroup")
    void addInterfaceGroup_exception_test() {
        doThrow(new RuntimeException()).when(interfaceGroupRepository).insert(any(InterfaceGroup.class));
        assertThatThrownBy(() -> interfaceGroupService.addGroup(InterfaceGroupDto.builder().build()))
            .isInstanceOf(ApiTestPlatformException.class)
            .extracting("code").isEqualTo(ADD_INTERFACE_GROUP_ERROR.getCode());
    }

    @Test
    @DisplayName("An exception occurred while edit InterfaceGroup")
    void editInterfaceGroup_exception_test() {
        when(interfaceGroupRepository.findById(GROUP_ID)).thenReturn(Optional.of(InterfaceGroup.builder().id(GROUP_ID).build()));
        doThrow(new RuntimeException()).when(interfaceGroupRepository).save(argThat(t -> true));
        assertThatThrownBy(() -> interfaceGroupService.editGroup(InterfaceGroupDto.builder().id(GROUP_ID).build()))
            .isInstanceOf(ApiTestPlatformException.class)
            .extracting("code").isEqualTo(EDIT_INTERFACE_GROUP_ERROR.getCode());
    }

    @Test
    @DisplayName("An exception occurred while deleting InterfaceGroup")
    void deleteInterfaceGroup_exception_test() {
        doThrow(new RuntimeException()).when(interfaceGroupRepository).deleteById(anyString());
        assertThatThrownBy(() -> interfaceGroupService.deleteGroup(anyString()))
            .isInstanceOf(ApiTestPlatformException.class)
            .extracting("code").isEqualTo(DELETE_INTERFACE_GROUP_BY_ID_ERROR.getCode());
    }

}