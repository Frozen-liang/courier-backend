package com.sms.courier.entity.job;

import com.sms.courier.common.enums.NoticeType;
import com.sms.courier.entity.job.common.AbstractCaseJobEntity;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.mongodb.core.mapping.FieldType;

@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@Data
@EqualsAndHashCode(callSuper = true)
@Document(collection = "ApiTestCaseJob")
public class ApiTestCaseJobEntity extends AbstractCaseJobEntity {

    @Builder.Default
    @Field("isRemoved")
    private boolean removed = false;

    @Field(targetType = FieldType.OBJECT_ID)
    private String createUserId;

    @Field(targetType = FieldType.OBJECT_ID)
    private String modifyUserId;

    /**
     * 测试人员.
     */
    private String createUserName;

    private NoticeType noticeType;

    private List<String> emails;
}
