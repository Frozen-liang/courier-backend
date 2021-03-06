package com.sms.courier.dto.request;

import com.sms.courier.dto.PageDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@EqualsAndHashCode(callSuper = true)
@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public class MockApiPageRequest extends PageDto {

    private Boolean isEnable;

}
