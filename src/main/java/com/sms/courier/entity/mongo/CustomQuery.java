package com.sms.courier.entity.mongo;

import com.sms.courier.common.field.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Builder.Default;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.query.Criteria;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CustomQuery {

    // 查询条件
    @Default
    private List<Optional<Criteria>> criteriaList = new ArrayList<>();

    // 查询字段
    private List<Field> includeFields;

    // 不查询字段
    private List<Field> excludeFields;

}
