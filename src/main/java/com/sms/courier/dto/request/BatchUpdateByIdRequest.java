package com.sms.courier.dto.request;

import java.util.List;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BatchUpdateByIdRequest<T> {

    @NotNull(message = "The ids must not be null.")
    @Size(min = 1, message = "The ids must not be empty.")
    private List<String> ids;

    @NotNull(message = "The updateRequest must not bu null.")
    @Valid
    private UpdateRequest<T> updateRequest;
}
