package com.sms.courier.initialize.impl;

import com.sms.courier.entity.mock.MockSettingEntity;
import com.sms.courier.initialize.DataInitializer;
import com.sms.courier.initialize.constant.Order;
import com.sms.courier.repository.MockSettingRepository;
import com.sms.courier.security.AccessTokenProperties;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class MockSettingInitializer implements DataInitializer {

    @Override
    public void init(ApplicationContext applicationContext) {
        MockSettingRepository mockSettingRepository = applicationContext.getBean(MockSettingRepository.class);
        MockSettingEntity mockSetting = mockSettingRepository
            .findFirstByOrderByCreateDateTimeDesc().orElse(new MockSettingEntity());
        AccessTokenProperties accessTokenProperties = applicationContext.getBean(AccessTokenProperties.class);
        if (StringUtils.isBlank(mockSetting.getSecretKey())) {
            mockSetting.setSecretKey(accessTokenProperties.getMockSecretKey());
            mockSettingRepository.save(mockSetting);
        }
        accessTokenProperties.setMockSecretKey(mockSetting.getSecretKey());
    }

    @Override
    public int getOrder() {
        return Order.MOCK_SETTING;
    }

}
