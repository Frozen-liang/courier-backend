package com.sms.courier.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.sms.courier.common.validate.InsertGroup;
import com.sms.courier.common.validate.UpdateGroup;
import com.sms.courier.generator.enums.CodeType;
import java.util.List;
import javax.validation.Valid;
import javax.validation.constraints.AssertFalse;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class GeneratorTemplateRequest {

    @NotNull(groups = UpdateGroup.class, message = "The id can not be empty")
    @Null(groups = InsertGroup.class, message = "The id must be null")
    private String id;

    @NotNull(groups = {InsertGroup.class, UpdateGroup.class}, message = "The name can not be empty")
    private String name;

    @NotNull(groups = {InsertGroup.class}, message = "The projectId can not be empty")
    private String projectId;

    @NotNull(groups = {InsertGroup.class, UpdateGroup.class}, message = "The codeType can not be empty")
    private CodeType codeType;

    @AssertFalse(groups = {InsertGroup.class}, message = "The isDefaultTemplate must be false")
    @JsonProperty("isDefaultTemplate")
    private boolean defaultTemplate;

    @Valid
    @Min(1)
    @NotEmpty(groups = {InsertGroup.class, UpdateGroup.class}, message = "The codeTemplates can not be empty")
    private List<CodeTemplateRequest> codeTemplates;
}
