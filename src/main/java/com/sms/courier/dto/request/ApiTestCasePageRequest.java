package com.sms.courier.dto.request;

import com.sms.courier.common.enums.ApiBindingStatus;
import com.sms.courier.dto.PageDto;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.bson.types.ObjectId;

@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class ApiTestCasePageRequest extends PageDto {

    private ObjectId projectId;
    private String caseName;
    private List<String> tagId;
    private ApiBindingStatus status;
    private ObjectId apiId;
    private List<ObjectId> createUserId;
}
