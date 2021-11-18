package com.sms.courier.dto.request;

import com.sms.courier.common.constant.TimePatternConstant;
import java.util.Date;
import javax.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Builder.Default;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DockerLogRequest {

    @NotBlank(message = "The name must not be empty!")
    private String name;

    @DateTimeFormat(pattern = TimePatternConstant.DEFAULT_PATTERN)
    private Date since;

    @Default
    private Integer tail = 100;
}
