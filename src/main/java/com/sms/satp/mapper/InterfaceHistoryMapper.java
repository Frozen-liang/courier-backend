package com.sms.satp.mapper;

import com.sms.satp.entity.ApiInterface;
import com.sms.satp.entity.InterfaceHistory;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", injectionStrategy = InjectionStrategy.CONSTRUCTOR)
public interface InterfaceHistoryMapper {

    @Mapping(source = "id", target = "id", ignore = true)
    ApiInterface convertToApi(InterfaceHistory interfaceHistory);

    @Mapping(source = "id", target = "id", ignore = true)
    InterfaceHistory convertToHistory(ApiInterface apiInterface);

}