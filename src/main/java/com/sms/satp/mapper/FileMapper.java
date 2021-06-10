package com.sms.satp.mapper;

import static com.sms.satp.common.constant.TimePatternConstant.DEFAULT_PATTERN;

import com.mongodb.client.gridfs.model.GridFSFile;
import com.sms.satp.dto.response.FileInfoResponse;
import java.util.List;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", injectionStrategy = InjectionStrategy.CONSTRUCTOR, unmappedTargetPolicy =
    ReportingPolicy.IGNORE)
public interface FileMapper {

    @Mapping(target = "id", expression = "java(gridFSFile.getId().asObjectId().getValue().toString())")
    @Mapping(target = "uploadDate", source = "uploadDate", dateFormat = DEFAULT_PATTERN)
    @Mapping(target = "projectId",
        expression = "java(gridFSFile.getMetadata().get(\"projectId\",org.bson.types.ObjectId.class).toString())")
    @Mapping(target = "uploadUser", expression = "java(gridFSFile.getMetadata().getString(\"uploadUser\"))")
    FileInfoResponse toFileInfoResponse(GridFSFile gridFSFile);

    List<FileInfoResponse> toFileInfoResponseList(List<GridFSFile> gridFSFile);
}