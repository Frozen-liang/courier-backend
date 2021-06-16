package com.sms.satp.mapper;

import com.mongodb.client.gridfs.model.GridFSFile;
import com.sms.satp.dto.response.FileInfoResponse;
import edu.umd.cs.findbugs.annotations.SuppressWarnings;
import java.util.List;
import java.util.Objects;
import org.bson.Document;
import org.bson.types.ObjectId;
import org.mapstruct.Builder;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", injectionStrategy = InjectionStrategy.CONSTRUCTOR, unmappedTargetPolicy =
    ReportingPolicy.IGNORE, builder = @Builder(disableBuilder = true), imports = ObjectId.class)
public interface FileMapper {

    @Mapping(target = "id", expression = "java(gridFsFile.getId().asObjectId().getValue().toString())")
    @Mapping(target = "uploadUser", expression = "java(toUploadUser(gridFsFile.getMetadata()))")
    @Mapping(target = "projectId", expression = "java(toProjectId(gridFsFile.getMetadata()))")
    FileInfoResponse toFileInfoResponse(GridFSFile gridFsFile);

    List<FileInfoResponse> toFileInfoResponseList(List<GridFSFile> gridFsFile);

    default String toUploadUser(Document metadata) {
        if (Objects.nonNull(metadata)) {
            return metadata.getString("uploadUser");
        }
        return null;
    }

    default String toProjectId(Document metadata) {
        if (Objects.nonNull(metadata)) {
            ObjectId objectId = metadata.get("projectId", ObjectId.class);
            return Objects.nonNull(objectId) ? objectId.toString() : null;
        }
        return null;
    }

}