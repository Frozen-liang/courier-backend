package com.sms.courier.docker.entity;

import com.sms.courier.entity.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
@Document(collection = "ContainerSetting")
public class ContainerSettingEntity extends BaseEntity {

    private String netWorkId;
    private String username;
    private String password;
    private String registryAddress;

}
