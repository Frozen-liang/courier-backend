package com.sms.satp.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import com.sms.satp.ApplicationTests;
import com.sms.satp.entity.InterfaceGroup;
import com.sms.satp.entity.dto.InterfaceGroupDto;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;

@DisplayName("Tests for InterfaceGroupMapper")
@SpringBootTest(classes = ApplicationTests.class,
    webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class InterfaceGroupMapperTest {

    @SpyBean
    InterfaceGroupMapper interfaceGroupMapper;

    private static final Integer SIZE = 10;
    private static final String NAME = "name";

    @Test
    @DisplayName("Test the method to convert the InterfaceGroup's entity object to a dto object")
    void entity_to_dto() {
        InterfaceGroup interfaceGroup = InterfaceGroup.builder()
            .name(NAME)
            .build();
        InterfaceGroupDto interfaceGroupDto = interfaceGroupMapper.toDto(interfaceGroup);
        assertThat(interfaceGroupDto.getName()).isEqualTo(NAME);
    }

    @Test
    @DisplayName("Test the method to convert the InterfaceGroup's dto object to a entity object")
    void dto_to_entity() {
        InterfaceGroupDto interfaceGroupDto = InterfaceGroupDto.builder()
            .name(NAME)
            .build();
        InterfaceGroup interfaceGroup = interfaceGroupMapper.toEntity(interfaceGroupDto);
        assertThat(interfaceGroup.getName()).isEqualTo(NAME);
    }

        @Test
    @DisplayName("Test the method for converting an InterfaceGroup entity list object to a dto list object")
    void interfaceGroupList_to_interfaceGroupDtoList() {
        List<InterfaceGroup> interfaceGroups = new ArrayList<>();
        for (int i = 0; i < SIZE; i++) {
            interfaceGroups.add(
                InterfaceGroup.builder()
                    .name(NAME)
                    .build());
        }
        List<InterfaceGroupDto> interfaceGroupDtoList = interfaceGroupMapper.toDtoList(interfaceGroups);
        assertThat(interfaceGroupDtoList.size()).isEqualTo(SIZE);
        assertThat(interfaceGroupDtoList).allMatch(result -> StringUtils.equals(result.getName(), NAME));
    }

    @Test
    @DisplayName("[Null Input Parameter]Test the method to convert the InterfaceGroup's entity object to a dto object")
    void null_entity_to_dto() {
        InterfaceGroupDto interfaceGroupDto = interfaceGroupMapper.toDto(null);
        assertThat(interfaceGroupDto).isNull();
    }

    @Test
    @DisplayName("[Null Input Parameter]Test the method to convert the InterfaceGroup's dto object to a entity object")
    void null_dto_to_entity() {
        InterfaceGroup interfaceGroup = interfaceGroupMapper.toEntity(null);
        assertThat(interfaceGroup).isNull();
    }

    @Test
    @DisplayName("[Null Input Parameter]Test the method for converting an InterfaceGroup entity list object to a dto list object")
    void null_entityList_to_dtoList() {
        List<InterfaceGroupDto> interfaceGroupDtoList = interfaceGroupMapper.toDtoList(null);
        assertThat(interfaceGroupDtoList).isNull();
    }

}