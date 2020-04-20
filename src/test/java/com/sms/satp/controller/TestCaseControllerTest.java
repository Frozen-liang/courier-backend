package com.sms.satp.controller;

import static com.sms.satp.utils.JsonUtil.asJsonString;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sms.satp.common.constant.Constants;
import com.sms.satp.common.response.Response;
import com.sms.satp.entity.dto.TestCaseDto;
import com.sms.satp.service.TestCaseService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

@WebMvcTest(value = TestCaseController.class)
@DisplayName("Tests for TestCaseController")
class TestCaseControllerTest {
    
    @MockBean
    private TestCaseService testCaseService;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    private final static String ID = "30";
    private final static String PROJECT_ID = "id";
    private final static String NAME = "name";
    
    @Test
    @DisplayName("Add a TestCase")
    void addWiki() throws Exception{
        TestCaseDto testCaseDto = TestCaseDto.builder()
            .name(NAME)
            .build();
        doNothing().when(testCaseService).add(testCaseDto);
        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
            .post(Constants.TEST_CASE_PATH)
            .contentType(MediaType.APPLICATION_JSON)
            .content(asJsonString(objectMapper, testCaseDto));
        ResultActions perform = mockMvc.perform(request);
        perform.andExpect(status().isOk())
            .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.code", is(Response.ok().build().getCode())))
            .andExpect(jsonPath("$.message", is(Response.ok().build().getMessage())));
    }

    @Test
    @DisplayName("Edit the TestCase by id")
    void editWiki() throws Exception{
        TestCaseDto testCaseDto = TestCaseDto.builder()
            .id(PROJECT_ID)
            .name(NAME)
            .build();
        doNothing().when(testCaseService).edit(testCaseDto);
        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
            .put(Constants.TEST_CASE_PATH)
            .contentType(MediaType.APPLICATION_JSON)
            .content(asJsonString(objectMapper, testCaseDto));
        ResultActions perform = mockMvc.perform(request);
        perform.andExpect(status().isOk())
            .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.code", is(Response.ok().build().getCode())))
            .andExpect(jsonPath("$.message", is(Response.ok().build().getMessage())));
    }

    @Test
    @DisplayName("Get its specific information through the id of the TestCase")
    void getInfoById() throws Exception{
        TestCaseDto testCaseDto = TestCaseDto.builder().build();
        when(testCaseService.getDtoById(ID)).thenReturn(testCaseDto);
        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
            .get(Constants.TEST_CASE_PATH + "/" + ID);
        ResultActions perform = mockMvc.perform(request);
        perform.andExpect(status().isOk())
            .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.code", is(Response.ok().build().getCode())))
            .andExpect(jsonPath("$.message", is(Response.ok().build().getMessage())));
    }

    @Test
    @DisplayName("Delete the TestCase by id")
    void deleteWiki() throws Exception{
        doNothing().when(testCaseService).deleteById(PROJECT_ID);
        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
            .delete(Constants.TEST_CASE_PATH + "/" + PROJECT_ID);
        ResultActions perform = mockMvc.perform(request);
        perform.andExpect(status().isOk())
            .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.code", is(Response.ok().build().getCode())))
            .andExpect(jsonPath("$.message", is(Response.ok().build().getMessage())));
    }

}