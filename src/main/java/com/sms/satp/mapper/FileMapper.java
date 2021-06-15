package com.sms.satp.mapper;

import static com.sms.satp.common.constant.TimePatternConstant.DEFAULT_PATTERN;

import com.mongodb.client.gridfs.model.GridFSFile;
import com.sms.satp.dto.response.FileInfoResponse;
import java.util.List;
import org.bson.types.ObjectId;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", injectionStrategy = InjectionStrategy.CONSTRUCTOR, unmappedTargetPolicy =
    ReportingPolicy.IGNORE, imports = ObjectId.class)
public interface FileMapper {

    @Mapping(target = "id", expression = "java(gridFsFile.getId().asObjectId().getValue().toString())")
    @Mapping(target = "uploadDate", source = "uploadDate", dateFormat = DEFAULT_PATTERN)
    @Mapping(target = "projectId",
        expression = "java(gridFsFile.getMetadata().get(\"projectId\",ObjectId.class).toString())")
    @Mapping(target = "uploadUser", expression = "java(gridFsFile.getMetadata().getString(\"uploadUser\"))")
    FileInfoResponse toFileInfoResponse(GridFSFile gridFsFile);

    List<FileInfoResponse> toFileInfoResponseList(List<GridFSFile> gridFsFile);
}