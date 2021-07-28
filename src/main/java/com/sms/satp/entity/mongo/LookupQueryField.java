package com.sms.satp.entity.mongo;

import com.sms.satp.common.field.Field;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class LookupQueryField {

    // 数据库的字段
    private Field field;
    //字段别名 与属性映射 如果与数据库字段一直则可以不设置
    private String alias;
}
