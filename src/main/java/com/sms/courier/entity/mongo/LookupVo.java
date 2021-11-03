package com.sms.courier.entity.mongo;

import static com.sms.courier.common.field.CommonField.CREATE_USER_ID;
import static com.sms.courier.common.field.CommonField.ID;
import static com.sms.courier.common.field.CommonField.USERNAME;
import static com.sms.courier.common.field.UserField.NICKNAME;

import com.google.common.collect.Lists;
import com.sms.courier.common.enums.CollectionName;
import com.sms.courier.common.field.Field;
import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Builder.Default;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class LookupVo {

    // 关联的表的collectionName
    private CollectionName from;
    // 主表的字段
    private Field localField;
    // 关联表的字段
    private Field foreignField;
    // 给表定义别名
    private String as;
    //查询的字段 如全部查询为空就行
    @Default
    private List<LookupField> queryFields = new ArrayList<>();

    public static LookupVo createLookupUser() {
        return LookupVo.builder()
            .from(CollectionName.USER)
            .localField(CREATE_USER_ID)
            .foreignField(ID)
            .as("user")
            .queryFields(
                Lists.newArrayList(
                    LookupField.builder().field(USERNAME).alias("createUsername").build(),
                    LookupField.builder().field(NICKNAME).alias("createNickname").build()))
            .build();
    }
}
