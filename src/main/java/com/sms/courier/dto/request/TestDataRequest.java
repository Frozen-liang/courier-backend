package com.sms.courier.dto.request;

import com.sms.courier.common.validate.InsertGroup;
import com.sms.courier.common.validate.UpdateGroup;
import java.util.List;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class TestDataRequest {

    @NotBlank(groups = {InsertGroup.class, UpdateGroup.class}, message = "The dataName cannot be empty")
    private String dataName;

    @Valid
    @NotNull(groups = {InsertGroup.class, UpdateGroup.class}, message = "The data cannot be null")
    @Size(min = 1, groups = {InsertGroup.class, UpdateGroup.class}, message = "The data cannot be empty")
    private List<DataParamRequest> data;
}
