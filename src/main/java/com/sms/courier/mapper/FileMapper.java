package com.sms.courier.mapper;

import com.sms.courier.dto.response.FileInfoResponse;
import com.sms.courier.entity.file.FileInfoEntity;
import java.util.List;
import org.bson.types.ObjectId;
import org.mapstruct.Builder;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", injectionStrategy = InjectionStrategy.CONSTRUCTOR, unmappedTargetPolicy =
    ReportingPolicy.IGNORE, builder = @Builder(disableBuilder = true), imports = ObjectId.class)
public interface FileMapper {

    FileInfoResponse toFileInfoResponse(FileInfoEntity fileInfoEntity);

    List<FileInfoResponse> toFileInfoResponseList(List<FileInfoEntity> fileInfoEntityList);

}