package com.sms.courier.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.when;

import com.sms.courier.dto.request.UserEnvConnRequest;
import com.sms.courier.dto.response.UserEnvConnResponse;
import com.sms.courier.entity.env.UserEnvConnEntity;
import com.sms.courier.mapper.UserEnvConnMapper;
import com.sms.courier.mapper.UserEnvConnMapperImpl;
import com.sms.courier.repository.UserEnvRepository;
import com.sms.courier.service.impl.UserEnvConnServiceImpl;
import com.sms.courier.utils.SecurityUtil;
import java.util.Optional;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;

@DisplayName("Test for UserEnvConnService")
public class UserEnvConnServiceTest {

    private final UserEnvRepository userEnvRepository = mock(UserEnvRepository.class);
    private final UserEnvConnMapper userEnvConnMapper = new UserEnvConnMapperImpl();
    private final UserEnvConnService userEnvConnService = new UserEnvConnServiceImpl(userEnvRepository,
        userEnvConnMapper);
    private static final String PROJECT_ID = ObjectId.get().toString();
    private static final String CURRENT_USER_ID = ObjectId.get().toString();
    private static final MockedStatic<SecurityUtil> SECURITY_UTIL_MOCKED_STATIC = mockStatic(SecurityUtil.class);

    @AfterAll
    public static void after() {
        SECURITY_UTIL_MOCKED_STATIC.close();
    }

    @Test
    @DisplayName("Test userEnv method in UserEnvConnService")
    public void userEnv_test() {
        when(userEnvRepository.findByProjectIdAndCreateUserId(PROJECT_ID, CURRENT_USER_ID))
            .thenReturn(Optional.of(new UserEnvConnResponse()));
        SECURITY_UTIL_MOCKED_STATIC.when(SecurityUtil::getCurrUserId).thenReturn(CURRENT_USER_ID);
        UserEnvConnResponse userEnvConnResponse = userEnvConnService.userEnv(PROJECT_ID);
        assertThat(userEnvConnResponse).isNotNull();
    }

    @Test
    @DisplayName("Test userEnvConn method in UserEnvConnService")
    public void userEnvConn_test() {
        when(userEnvRepository.save(any())).thenReturn(UserEnvConnEntity.builder().build());
        UserEnvConnResponse userEnvConnResponse = userEnvConnService.userEnvConn(new UserEnvConnRequest());
        assertThat(userEnvConnResponse).isNotNull();
    }

}
