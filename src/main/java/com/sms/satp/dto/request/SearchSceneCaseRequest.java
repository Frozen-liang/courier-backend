package com.sms.satp.dto.request;

import com.sms.satp.dto.PageDto;
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
    private boolean removed;
    private List<String> testStatus;
    private List<ObjectId> tagId;
    private List<Integer> priority;
    private List<String> createUserName;

}
