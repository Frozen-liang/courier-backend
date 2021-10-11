package com.sms.courier.dto.request;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ScheduleListRequest {

    private ObjectId projectId;

    private ObjectId groupId;

    private String name;
}
