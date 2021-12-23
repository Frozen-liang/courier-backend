package com.sms.courier.mapper;

import com.sms.courier.common.enums.DatabaseType;
import com.sms.courier.dto.request.DataBaseRequest;
import com.sms.courier.dto.response.DataBaseResponse;
import com.sms.courier.entity.database.DatabaseEntity;
import com.sms.courier.entity.job.JobDatabase;
import com.sms.courier.utils.EnumCommonUtils;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", injectionStrategy = InjectionStrategy.CONSTRUCTOR,
    unmappedTargetPolicy = ReportingPolicy.IGNORE, uses = EnumCommonUtils.class,
    imports = {DatabaseType.class})
public interface DatabaseMapper {

    DataBaseResponse toResponse(DatabaseEntity dataBaseEntity);

    @Mapping(target = "databaseType", expression = "java(com.sms.courier.common.enums.DatabaseType"
        + ".getDatabaseType(dataBaseRequest.getDatabaseType()))")
    DatabaseEntity toEntity(DataBaseRequest dataBaseRequest);

    JobDatabase toJobDatabase(DatabaseEntity databaseEntity);

}
