package com.sms.courier.mapper;

import com.sms.courier.dto.request.DataCollectionRequest;
import com.sms.courier.dto.response.DataCollectionResponse;
import com.sms.courier.entity.datacollection.DataCollectionEntity;
import java.util.List;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", injectionStrategy = InjectionStrategy.CONSTRUCTOR,
    unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface DataCollectionMapper {

    DataCollectionResponse toDto(DataCollectionEntity dataCollection);

    List<DataCollectionResponse> toDtoList(List<DataCollectionEntity> dataCollections);

    DataCollectionEntity toEntity(DataCollectionRequest dataCollectionDto);

}