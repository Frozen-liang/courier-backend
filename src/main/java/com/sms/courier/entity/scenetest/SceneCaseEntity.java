package com.sms.courier.entity.scenetest;

import com.sms.courier.common.enums.ReviewStatus;
import com.sms.courier.entity.BaseEntity;
import java.util.ArrayList;
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
@Document(collection = "SceneCase")
public class SceneCaseEntity extends BaseEntity {

    private String name;

    private String description;

    @Field(targetType = FieldType.OBJECT_ID)
    private String projectId;

    @Field(targetType = FieldType.OBJECT_ID)
    private String groupId;

    @Field(targetType = FieldType.OBJECT_ID)
    private List<String> tagId;

    private Integer priority;

    /**
     * 出错时，是否执行下一个步骤.
     */
    @Field("isNext")
    private boolean next;

    @Default
    private List<EnvDataCollConn> envDataCollConnList = new ArrayList<>();

    private ReviewStatus reviewStatus;
}
