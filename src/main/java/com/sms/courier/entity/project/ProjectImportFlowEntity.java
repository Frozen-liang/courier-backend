package com.sms.courier.entity.project;

import com.sms.courier.common.enums.ImportStatus;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Builder.Default;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.mongodb.core.mapping.FieldType;
import org.springframework.data.mongodb.core.mapping.MongoId;

@SuppressFBWarnings("EQ_OVERRIDING_EQUALS_NOT_SYMMETRIC")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Document(collection = "ProjectImportFlow")
public class ProjectImportFlowEntity {

    @MongoId(FieldType.OBJECT_ID)
    private String id;
    private String projectId;
    private String importSourceId;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private ImportStatus importStatus;
    private String errorCode;
    private String errorDetail;
    @LastModifiedDate
    private LocalDateTime createDateTime;
    @CreatedBy
    @Field(targetType = FieldType.OBJECT_ID)
    private String createUserId;
    @Default
    private List<ApiRecord> addedApi = new ArrayList<>();
    @Default
    private List<ApiRecord> deletedApi = new ArrayList<>();
    @Default
    private List<ApiRecord> updatedApi = new ArrayList<>();
    @Default
    private LinkedList<ApiImportRollbackRecord> rollbackRecords = new LinkedList<>();
}
