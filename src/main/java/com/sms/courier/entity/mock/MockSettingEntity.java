package com.sms.courier.entity.mock;

import com.sms.courier.entity.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import org.springframework.data.mongodb.core.mapping.Document;

@EqualsAndHashCode(callSuper = true)
@Data
@ToString(callSuper = true)
@NoArgsConstructor
@SuperBuilder
@Document(collection = "MockSetting")
public class MockSettingEntity extends BaseEntity {

    private String mockUrl;

    private String mockToken;
}
