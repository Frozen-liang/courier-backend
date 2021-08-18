package com.sms.courier.mapper;

import com.sms.courier.dto.request.DataStructureRequest;
import com.sms.courier.dto.response.DataStructureResponse;
import com.sms.courier.entity.structure.StructureEntity;
import com.sms.courier.utils.EnumCommonUtils;
import java.util.List;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", injectionStrategy = InjectionStrategy.CONSTRUCTOR,
    unmappedTargetPolicy = ReportingPolicy.IGNORE,uses = {EnumCommonUtils.class,ParamInfoMapper.class})
public interface DataStructureMapper {

    DataStructureResponse toResponse(StructureEntity dataStructure);

    List<DataStructureResponse> toResponseList(List<StructureEntity> dataStructureList);

    StructureEntity toEntity(DataStructureRequest dataStructureRequest);
}
