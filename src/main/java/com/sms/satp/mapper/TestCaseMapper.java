package com.sms.satp.mapper;

import com.sms.satp.entity.dto.TestCaseDto;
import com.sms.satp.entity.test.TestCase;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", injectionStrategy = InjectionStrategy.CONSTRUCTOR)
public interface TestCaseMapper {

    @Mapping(target = "createDateTime", source = "createDateTime", dateFormat = "yyyy-MM-dd HH:mm:ss")
    @Mapping(target = "modifyDateTime", source = "modifyDateTime", dateFormat = "yyyy-MM-dd HH:mm:ss")
    TestCaseDto toDto(TestCase wiki);

    @Mapping(target = "modifyDateTime", ignore = true)
    @Mapping(target = "createDateTime", ignore = true)
    TestCase toEntity(TestCaseDto wikiDto);
}
