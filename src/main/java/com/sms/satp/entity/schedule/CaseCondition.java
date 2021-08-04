package com.sms.satp.entity.schedule;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CaseCondition {

    private List<String> tag;

    private List<Integer> priority;
}
