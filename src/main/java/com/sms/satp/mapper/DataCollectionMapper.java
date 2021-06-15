package com.sms.satp.mapper;

import com.sms.satp.dto.request.DataCollectionRequest;
import com.sms.satp.dto.response.DataCollectionResponse;
import com.sms.satp.entity.datacollection.DataCollection;
import java.util.List;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", injectionStrategy = InjectionStrategy.CONSTRUCTOR,
    unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface DataCollectionMapper {

    DataCollectionResponse toDto(DataCollection dataCollection);

    List<DataCollectionResponse> toDtoList(List<DataCollection> dataCollections);

    DataCollection toEntity(DataCollectionRequest dataCollectionDto);

}