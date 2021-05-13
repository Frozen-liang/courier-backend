package com.sms.satp.dto.request;

import com.sms.satp.dto.response.SceneCaseApiResponse;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BatchUpdateSceneCaseApiRequest {

    private List<SceneCaseApiResponse> sceneCaseApiRequestList;
}
