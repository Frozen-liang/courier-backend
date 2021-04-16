package com.sms.satp.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.sms.satp.common.enums.RequestMethod;
import java.time.LocalDateTime;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "ApiInterface")
public class ApiInterface {

    @Id
    @JsonIgnore
    private ObjectId id;
    private RequestMethod method;
    private List<String> tag;
    @JsonIgnore
    private String md5;
    private String title;
    private String path;
    private String description;
    @Field("group_id")
    private String groupId;
    @Field("project_id")
    private String projectId;
    @Field("request_headers")
    private List<Header> requestHeaders;
    @Field("response_headers")
    private List<Header> responseHeaders;
    @Field("query_params")
    private List<Parameter> queryParams;
    @Field("path_params")
    private List<Parameter> pathParams;
    @Field("request_body")
    private RequestBody requestBody;
    private Response response;
    @JsonIgnore
    @Field("create_date_time")
    private LocalDateTime createDateTime;
    @JsonIgnore
    @Field("modify_date_time")
    private LocalDateTime modifyDateTime;


}
