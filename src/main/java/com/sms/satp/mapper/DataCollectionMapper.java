package com.sms.satp.mapper;

import com.sms.satp.dto.DataCollectionRequest;
import com.sms.satp.dto.DataCollectionResponse;
import com.sms.satp.entity.datacollection.DataCollection;
import java.util.List;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", injectionStrategy = InjectionStrategy.CONSTRUCTOR,
    unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface DataCollectionMapper {

    @Mapping(target = "createDateTime", source = "createDateTime", dateFormat = "yyyy-MM-dd HH:mm:ss")
    @Mapping(target = "modifyDateTime", source = "modifyDateTime", dateFormat = "yyyy-MM-dd HH:mm:ss")
    DataCollectionResponse toDto(DataCollection dataCollection);

    List<DataCollectionResponse> toDtoList(List<DataCollection> dataCollections);

    @Mapping(target = "createDateTime", ignore = true)
    @Mapping(target = "modifyDateTime", ignore = true)
    DataCollection toEntity(DataCollectionRequest dataCollectionDto);

}