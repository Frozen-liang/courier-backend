package com.sms.courier.entity.generator;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CodeFieldDesc {

    private String key;

    private String desc;

    private List<CodeFieldDesc> childField;
}
