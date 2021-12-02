package com.sms.courier.dto.request;

import com.sms.courier.dto.PageDto;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.bson.types.ObjectId;

@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class ApiTestCaseJobPageRequest extends PageDto {

    private List<ObjectId> userIds;

    private ObjectId apiTestCaseId;

    private ObjectId apiId;

    private ObjectId envId;
}
