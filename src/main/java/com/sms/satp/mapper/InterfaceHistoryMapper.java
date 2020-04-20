package com.sms.satp.mapper;

import com.sms.satp.entity.ApiInterface;
import com.sms.satp.entity.InterfaceHistory;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", injectionStrategy = InjectionStrategy.CONSTRUCTOR)
public interface InterfaceHistoryMapper {

    ApiInterface convertToApi(InterfaceHistory interfaceHistory);

    InterfaceHistory convertToHistory(ApiInterface apiInterface);

}