package com.sms.courier.entity.datacollection;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class TestData {

    private String dataName;
    private List<DataParam> data;
}
