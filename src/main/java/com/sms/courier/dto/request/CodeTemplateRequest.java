package com.sms.courier.dto.request;

import com.sms.courier.common.enums.TemplateType;
import com.sms.courier.common.validate.InsertGroup;
import com.sms.courier.common.validate.UpdateGroup;
import com.sms.courier.entity.generator.CodeFieldDesc;
import java.util.List;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CodeTemplateRequest {

    @NotNull(groups = {InsertGroup.class, UpdateGroup.class}, message = "The name can not be empty")
    private String name;

    @NotNull(groups = {InsertGroup.class, UpdateGroup.class}, message = "The value can not be empty")
    private String value;

    @NotNull(groups = {InsertGroup.class, UpdateGroup.class}, message = "The templateType can not be empty")
    private TemplateType templateType;
}
