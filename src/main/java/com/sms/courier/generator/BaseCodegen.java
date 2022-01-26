package com.sms.courier.generator;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public class BaseCodegen {

    private String className;

    private String packageName;

    private String classPackage;

    private List<String> imports;
}
