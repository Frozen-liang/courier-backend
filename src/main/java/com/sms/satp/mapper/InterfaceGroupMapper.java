package com.sms.satp.mapper;

import com.sms.satp.entity.InterfaceGroup;
import com.sms.satp.entity.dto.InterfaceGroupDto;
import java.util.List;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", injectionStrategy = InjectionStrategy.CONSTRUCTOR)
public interface InterfaceGroupMapper {

    InterfaceGroupDto toDto(InterfaceGroup interfaceGroup);

    InterfaceGroup toEntity(InterfaceGroupDto interfaceGroupDto);

    List<InterfaceGroupDto> toDtoList(List<InterfaceGroup> apiInterfaceList);

}