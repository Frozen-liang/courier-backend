package com.sms.courier.initialize.reset;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

@Document("ResetSetting")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ResetSetting {

    private String resetType;
    private boolean reset;
}
