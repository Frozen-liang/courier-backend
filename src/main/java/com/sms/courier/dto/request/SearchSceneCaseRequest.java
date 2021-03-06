package com.sms.courier.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.sms.courier.dto.PageDto;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.bson.types.ObjectId;

@EqualsAndHashCode(callSuper = true)
@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public class SearchSceneCaseRequest extends PageDto {

    private String name;
    private ObjectId groupId;
    @JsonProperty("isRemoved")
    private boolean removed;
    private ObjectId apiId;
    private List<String> testStatus;
    private List<ObjectId> tagId;
    private List<Integer> priority;
    private List<ObjectId> createUserId;
    private Integer reviewStatus;
}
