package com.sms.satp.entity.scenetest;

import com.sms.satp.entity.BaseEntity;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder.Default;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.mongodb.core.mapping.FieldType;

@EqualsAndHashCode(callSuper = true)
@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@ToString(callSuper = true)
@Document(collection = "CaseTemplate")
public class CaseTemplateEntity extends BaseEntity {

    private String name;

    private String createUserName;

    @Field(targetType = FieldType.OBJECT_ID)
    private String projectId;

    @Field(targetType = FieldType.OBJECT_ID)
    private String dataCollId;

    @Field(targetType = FieldType.OBJECT_ID)
    private String groupId;

    private List<String> tagId;

    /**
     * 是否锁定，当前步骤出错或未通过时，依然执行下一个步骤.
     */
    @Default
    @Field("isLock")
    private boolean lock = true;
}
