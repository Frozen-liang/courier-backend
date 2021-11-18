package com.sms.courier.entity.job;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.sms.courier.common.enums.NoticeType;
import com.sms.courier.entity.job.common.AbstractSceneCaseJobEntity;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.mongodb.core.mapping.FieldType;

@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@Document(collection = "SceneCaseJob")
public class SceneCaseJobEntity extends AbstractSceneCaseJobEntity {

    @Builder.Default
    @JsonIgnore
    private Boolean removed = false;
    @Field(targetType = FieldType.OBJECT_ID)
    private String createUserId;

    @Field(targetType = FieldType.OBJECT_ID)
    private String modifyUserId;
    // 测试模板Id
    @Field(targetType = FieldType.OBJECT_ID)
    private String caseTemplateId;
    /**
     * 测试人员.
     */
    private String createUserName;

    private NoticeType noticeType;

    private List<String> emails;
}
