package com.sms.satp.entity.mongo;

import com.sms.satp.common.enums.OperationModule;
import com.sms.satp.common.field.Field;
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

    // 关联的表
    private OperationModule from;
    // 主表的字段
    private Field localField;
    // 关联表的字段
    private Field foreignField;
    // 给表定义别名
    private String as;
    //查询的字段 如全部查询为空就行
    @Default
    private List<LookupField> queryFields = new ArrayList<>();
}
