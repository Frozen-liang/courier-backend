package com.sms.courier.dto.request;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.sms.courier.common.validate.InsertGroup;
import com.sms.courier.common.validate.UpdateGroup;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Null;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AmazonStorageSettingRequest {

    private String id;

    private String accessKeyId;

    private String accessKeySecret;

    private String bucketName;

    @NotBlank(groups = {InsertGroup.class, UpdateGroup.class}, message = "The region must not be empty.")
    private String region;

    @JsonAlias("isRemoved")
    private boolean removed;
}
