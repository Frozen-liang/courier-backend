package com.sms.courier.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.sms.courier.dto.PageDto;
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

    private String apiName;

    private String apiPath;

    @JsonProperty("isRemoved")
    private boolean removed;

    private ObjectId groupId;

    private List<ObjectId> tagId;

    private List<ObjectId> apiManagerId;

    private List<Integer> apiProtocol;

    private List<Integer> requestMethod;

    private List<Integer> apiStatus;

    private Integer caseCount;

    private Integer sceneCaseCount;

    private Integer otherProjectSceneCaseCount;

}
