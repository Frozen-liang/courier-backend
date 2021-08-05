package com.sms.courier.dto.request;

import com.sms.courier.common.validate.InsertGroup;
import com.sms.courier.common.validate.UpdateGroup;
import javax.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class DataParamRequest {

    @NotBlank(groups = {InsertGroup.class, UpdateGroup.class}, message = "The key cannot by empty")
    private String key;

    private String value;
}
