package com.sms.satp.entity.test;

import java.util.List;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Field;

@Data
@Builder
public class CaseRequestBody {

    @Field("media_types")
    private List<String> mediaTypes;
    private CaseSchema schema;

}