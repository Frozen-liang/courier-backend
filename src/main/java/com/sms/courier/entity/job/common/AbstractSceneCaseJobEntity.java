package com.sms.courier.entity.job.common;

import com.sms.courier.entity.job.JobSceneCaseApi;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.mongodb.core.mapping.FieldType;

@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class AbstractSceneCaseJobEntity extends JobEntity {

    @Field(targetType = FieldType.OBJECT_ID)
    private String sceneCaseId;

    private List<JobSceneCaseApi> apiTestCase;

    /**
     * 出错时，是否执行下一个步骤.
     */
    @Field("isNext")
    private boolean next;
}
