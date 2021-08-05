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

    // 关联的表
    private List<LookupVo> lookupVo;

    // 查询条件
    @Default
    private List<Optional<Criteria>> criteriaList = new ArrayList<>();
}
