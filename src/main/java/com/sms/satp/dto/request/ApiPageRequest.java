package com.sms.satp.dto.request;

import com.sms.satp.dto.PageDto;
import java.util.List;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ApiPageRequest extends PageDto {

    @NotNull(message = "The projectId must not be null.")
    private ObjectId projectId;

    private List<ObjectId> groupId;

    private List<ObjectId> tagId;

    private List<Integer> apiProtocol;

    private List<Integer> requestMethod;

    private List<Integer> apiStatus;

}
