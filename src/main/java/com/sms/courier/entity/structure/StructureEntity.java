package com.sms.courier.entity.structure;

import com.sms.courier.common.enums.ApiRequestParamType;
import com.sms.courier.entity.BaseEntity;
import com.sms.courier.entity.api.common.ParamInfo;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import java.util.List;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.mongodb.core.mapping.FieldType;

@EqualsAndHashCode(callSuper = true)
@SuppressFBWarnings("EQ_OVERRIDING_EQUALS_NOT_SYMMETRIC")
@Data
@ToString(callSuper = true)
@NoArgsConstructor
@SuperBuilder
@Document(collection = "DataStructure")
@CompoundIndex(def = "{'refId':1,'name':1}", unique = true)
public class StructureEntity extends BaseEntity {

    private String name;

    /**
     * 这里既有可能是projectId 也有可能是spaceId, 需要看是否是全局isGlobal字段来进行区分.
     */
    @Field(targetType = FieldType.OBJECT_ID)
    private String refId;

    private boolean global;

    private List<ParamInfo> struct;

    private ApiRequestParamType structType;

    private String description;

    private List<String> refStructIds;
}
