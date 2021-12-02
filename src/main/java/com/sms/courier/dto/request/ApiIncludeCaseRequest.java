package com.sms.courier.dto.request;

import com.sms.courier.dto.PageDto;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.bson.types.ObjectId;

@EqualsAndHashCode(callSuper = true)
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class ApiIncludeCaseRequest extends PageDto {

    private boolean include;

    @NotNull(message = "The projectId must be null.")
    private ObjectId projectId;

    private String apiName;
}
