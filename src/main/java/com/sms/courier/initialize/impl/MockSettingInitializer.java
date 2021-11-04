package com.sms.courier.initialize.impl;

import com.sms.courier.common.constant.Constants;
import com.sms.courier.entity.mock.MockSettingEntity;
import com.sms.courier.initialize.DataInitializer;
import com.sms.courier.initialize.constant.Order;
import com.sms.courier.repository.MockSettingRepository;
import com.sms.courier.security.jwt.JwtTokenManager;
import com.sms.courier.security.pojo.CustomUser;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

@Slf4j
@Component
public class MockSettingInitializer implements DataInitializer {

    @Override
    public void init(ApplicationContext applicationContext) {
        MockSettingRepository mockSettingRepository = applicationContext.getBean(MockSettingRepository.class);
        List<MockSettingEntity> entityList = mockSettingRepository.findAll();
        if (CollectionUtils.isEmpty(entityList)) {
            JwtTokenManager jwtTokenManager = applicationContext.getBean(JwtTokenManager.class);
            CustomUser mock = CustomUser.createMock();
            String token = jwtTokenManager.generateAccessToken(mock);
            Map<String, String> envMap = new HashMap<>();
            envMap.put(Constants.API_TOKEN, token);
            MockSettingEntity mockSettingEntity = MockSettingEntity.builder().envVariable(envMap).build();
            mockSettingRepository.insert(mockSettingEntity);
        }
    }

    @Override
    public int getOrder() {
        return Order.MOCK_SETTING;
    }

}
