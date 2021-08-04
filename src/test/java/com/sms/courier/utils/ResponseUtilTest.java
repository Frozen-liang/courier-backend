package com.sms.courier.utils;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sms.courier.common.response.Response;
import javax.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("Tests for ResponseUtil")
public class ResponseUtilTest {

    @Test
    @DisplayName("Test the out method in the ResponseUtil")
    public void out_null_test() {
        ResponseUtil.out(null, null, null);
    }

    @Test
    @DisplayName("Test the out method in the ResponseUtil")
    public void out_test() throws JsonProcessingException {
        HttpServletResponse httpServletResponse = mock(HttpServletResponse.class);
        Response response = Response.builder().build();
        ObjectMapper objectMapper = mock(ObjectMapper.class);
        ResponseUtil.out(httpServletResponse, response, objectMapper);
        verify(objectMapper).writeValueAsString(response);
    }
}
