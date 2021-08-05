package com.sms.courier.dto.request;

import com.sms.courier.common.validate.InsertGroup;
import com.sms.courier.common.validate.UpdateGroup;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.web.multipart.MultipartFile;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class TestFileRequest {

    @NotNull(groups = UpdateGroup.class, message = "The id must not be null.")
    @Null(groups = InsertGroup.class, message = "The id must be null.")
    private ObjectId id;

    @NotNull(groups = {InsertGroup.class}, message = "The projectId must not be null.")
    private ObjectId projectId;

    @NotNull(groups = {UpdateGroup.class, InsertGroup.class}, message = "The testFile must not be null.")
    private MultipartFile testFile;
}
