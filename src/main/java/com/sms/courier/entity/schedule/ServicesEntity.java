package com.sms.satp.entity.schedule;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Builder.Default;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Document(collection = "Services")
@CompoundIndex(def = "{'ip': 1, 'port': 1}", unique = true)
public class ServicesEntity {

    private String ip;
    private int port;
    @Field("isAvailableServices")
    @Default
    private boolean availableServices = true;
}
