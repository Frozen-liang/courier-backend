package com.sms.courier.initialize.impl;

import static com.sms.courier.common.constant.Constants.MOCK_CONTAINER_NAME;

import com.sms.courier.docker.entity.PortMapping;
import com.sms.courier.entity.mock.MockSettingEntity;
import com.sms.courier.initialize.DataInitializer;
import com.sms.courier.initialize.constant.Order;
import com.sms.courier.repository.MockSettingRepository;
import com.sms.courier.security.AccessTokenProperties;
import com.sms.courier.utils.AesUtil;
import java.util.List;
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
            mockSetting.setSecretKey(AesUtil.encrypt(accessTokenProperties.getMockSecretKey()));
            mockSetting.setContainerName(MOCK_CONTAINER_NAME);
            mockSetting.setPortMappings(List.of(new PortMapping(4001, 8885)));
            mockSettingRepository.save(mockSetting);
        }
        accessTokenProperties.setMockSecretKey(AesUtil.decrypt(mockSetting.getSecretKey()));
    }

    @Override
    public int getOrder() {
        return Order.MOCK_SETTING;
    }

}
