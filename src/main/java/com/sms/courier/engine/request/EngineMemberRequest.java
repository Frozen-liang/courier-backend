package com.sms.courier.engine.request;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Range;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EngineMemberRequest {

    @NotBlank(message = "The id must not be empty!")
    private String id;

    @NotNull(message = "The taskSizeLimit must not be null!")
    @Range(min = -1, message = "The taskSizeLimit must greater -1!")
    private Integer taskSizeLimit;
}
