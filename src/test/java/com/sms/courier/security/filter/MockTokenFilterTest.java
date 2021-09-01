package com.sms.courier.security.filter;

import com.sms.courier.security.jwt.JwtTokenManager;
import com.sms.courier.utils.JwtUtils;
import javax.servlet.FilterChain;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.springframework.security.core.Authentication;

import static com.sms.courier.common.enums.RoleType.MOCK;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@DisplayName("MockTokenFilter Test")
public class MockTokenFilterTest {

    private final JwtTokenManager jwtTokenManager = mock(JwtTokenManager.class);
    private final HttpServletRequest request = mock(HttpServletRequest.class);
    private final HttpServletResponse response = mock(HttpServletResponse.class);
    private final FilterChain chain = mock(FilterChain.class);

    private final MockTokenFilter mocktokenfilter = new MockTokenFilter(jwtTokenManager);

    private MockedStatic<JwtUtils> jwt_util_mocked_static;
    private static final String TOKEN = "test";

    @BeforeEach
    void setUp(){
        jwt_util_mocked_static = mockStatic(JwtUtils.class);
    }

    @AfterEach
    public void close() {
        jwt_util_mocked_static.close();
    }

    @Test
    @DisplayName("Set token success")
    void doFilterInternal_test() throws Exception{
        jwt_util_mocked_static.when(()->JwtUtils.getToken(any())).thenReturn(TOKEN);
        Authentication authentication = mock(Authentication.class);
        when(jwtTokenManager.getTokenType(any())).thenReturn(MOCK.name());
        when(jwtTokenManager.createAuthentication(any())).thenReturn(authentication);
        when(jwtTokenManager.validate(any())).thenReturn(Boolean.TRUE);
        mocktokenfilter.doFilterInternal(request,response,chain);
        verify(jwtTokenManager,times(1)).createAuthentication(TOKEN);
    }

    @Test
    @DisplayName("Set token fail")
    void doFilterInternalTokenIsNull_test() throws Exception{
        jwt_util_mocked_static.when(()->JwtUtils.getToken(any())).thenReturn(null);
        mocktokenfilter.doFilterInternal(request,response,chain);
        verify(chain,times(1)).doFilter(any(),any());
    }

    @Test
    @DisplayName("Set token fail")
    void doFilterInternalValidateIsFalse_test() throws Exception{
        jwt_util_mocked_static.when(()->JwtUtils.getToken(any())).thenReturn(TOKEN);
        when(jwtTokenManager.getTokenType(any())).thenReturn(MOCK.name());
        when(jwtTokenManager.validate(any())).thenReturn(Boolean.FALSE);
        mocktokenfilter.doFilterInternal(request,response,chain);
        verify(chain,times(1)).doFilter(any(),any());
    }

}
