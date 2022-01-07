package com.sms.courier.entity.mongo;

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
public class QueryVo {

    // 查询的表
    private String collectionName;

    // v1.0.9增加查询的实体 建议查询使用实体 因为使用collectionName会存在String和ObjectId查询不匹配问题
    private Class<?> entityClass;

    // 关联的表 不关联则为空
    @Default
    private List<LookupVo> lookupVo = new ArrayList<>();

    // 查询条件
    @Default
    private List<Optional<Criteria>> criteriaList = new ArrayList<>();
}
