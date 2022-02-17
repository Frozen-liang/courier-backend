package com.sms.courier.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.sms.courier.common.constant.TimePatternConstant;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@EqualsAndHashCode(callSuper = true)
public class SceneCaseCommentResponse extends TreeResponse {

    private String sceneCaseId;

    @JsonFormat(pattern = TimePatternConstant.DEFAULT_PATTERN)
    private LocalDateTime createDateTime;

    private String createUserId;

    private String createUsername;

    private String createNickname;

    private String comment;
}
