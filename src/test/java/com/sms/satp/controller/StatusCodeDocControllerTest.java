package com.sms.satp.controller;

import static com.sms.satp.utils.JsonUtils.asJsonString;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sms.satp.common.constant.Constants;
import com.sms.satp.common.response.Response;
import com.sms.satp.entity.dto.PageDto;
import com.sms.satp.entity.dto.StatusCodeDocDto;
import com.sms.satp.service.StatusCodeDocService;
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

@WebMvcTest(value = StatusCodeDocController.class)
@DisplayName("Tests for StatusCodeDocController")
class StatusCodeDocControllerTest {

    @MockBean
    private StatusCodeDocService statusCodeDocService;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    private final static Integer PAGE_NUMBER = 3;
    private final static Integer PAGE_SIZE = 20;
    private final static String ID = "30";
    private final static String PROJECT_ID = "id";
    private final static String CODE = "200";
    private static final String PARAM_INVALIDATE_CODE = "400";

    @Test
    @DisplayName("Query the page data for the StatusCodeDoc by default query criteria")
    void getStatusCodeDocPageByDefaultRequirements() throws Exception {
        PageDto pageDto = PageDto.builder().build();
        when(statusCodeDocService.page(pageDto, PROJECT_ID)).thenReturn(null);
        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
            .get(Constants.STATUS_CODE_DOC_PATH + "/page/" + PROJECT_ID);
        ResultActions perform = mockMvc.perform(request);
        perform.andExpect(status().isOk())
            .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.code", is(Response.ok().build().getCode())))
            .andExpect(jsonPath("$.message", is(Response.ok().build().getMessage())));
    }

    @Test
    @DisplayName("Query the page data for the StatusCodeDoc by specified query criteria")
    void getStatusCodeDocPageBySpecifiedRequirements() throws Exception {
        PageDto pageDto = PageDto.builder()
            .pageNumber(PAGE_NUMBER)
            .pageSize(PAGE_SIZE)
            .build();
        when(statusCodeDocService.page(pageDto, PROJECT_ID)).thenReturn(null);
        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
            .get(Constants.STATUS_CODE_DOC_PATH + "/page/" + PROJECT_ID)
            .param("pageNumber", String.valueOf(PAGE_NUMBER))
            .param("pageSize", String.valueOf(PAGE_SIZE));
        ResultActions perform = mockMvc.perform(request);
        perform.andExpect(status().isOk())
            .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.code", is(Response.ok().build().getCode())))
            .andExpect(jsonPath("$.message", is(Response.ok().build().getMessage())));
    }

    @Test
    @DisplayName("Add a StatusCodeDoc")
    void addStatusCodeDoc() throws Exception{
        StatusCodeDocDto statusCodeDocDto = StatusCodeDocDto.builder()
            .code(CODE)
            .projectId(PROJECT_ID)
            .build();
        doNothing().when(statusCodeDocService).add(statusCodeDocDto);
        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
            .post(Constants.STATUS_CODE_DOC_PATH)
            .contentType(MediaType.APPLICATION_JSON)
            .content(asJsonString(objectMapper, statusCodeDocDto));
        ResultActions perform = mockMvc.perform(request);
        perform.andExpect(status().isOk())
            .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.code", is(Response.ok().build().getCode())))
            .andExpect(jsonPath("$.message", is(Response.ok().build().getMessage())));
    }

    @Test
    @DisplayName("Edit the StatusCodeDoc by id")
    void editStatusCodeDoc() throws Exception{
        StatusCodeDocDto statusCodeDocDto = StatusCodeDocDto.builder()
            .id(PROJECT_ID)
            .code(CODE)
            .projectId(PROJECT_ID)
            .build();
        doNothing().when(statusCodeDocService).edit(statusCodeDocDto);
        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
            .put(Constants.STATUS_CODE_DOC_PATH)
            .contentType(MediaType.APPLICATION_JSON)
            .content(asJsonString(objectMapper, statusCodeDocDto));
        ResultActions perform = mockMvc.perform(request);
        perform.andExpect(status().isOk())
            .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.code", is(Response.ok().build().getCode())))
            .andExpect(jsonPath("$.message", is(Response.ok().build().getMessage())));
    }

    @Test
    @DisplayName("Get its specific information through the id of the StatusCodeDoc")
    void getInfoById() throws Exception{
        StatusCodeDocDto statusCodeDocDto = StatusCodeDocDto.builder().build();
        when(statusCodeDocService.findById(ID)).thenReturn(statusCodeDocDto);
        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
            .get(Constants.STATUS_CODE_DOC_PATH + "/" + ID);
        ResultActions perform = mockMvc.perform(request);
        perform.andExpect(status().isOk())
            .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.code", is(Response.ok().build().getCode())))
            .andExpect(jsonPath("$.message", is(Response.ok().build().getMessage())));
    }

    @Test
    @DisplayName("Delete the StatusCodeDoc by id")
    void deleteStatusCodeDoc() throws Exception{
        doNothing().when(statusCodeDocService).deleteById(PROJECT_ID);
        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
            .delete(Constants.STATUS_CODE_DOC_PATH + "/" + PROJECT_ID);
        ResultActions perform = mockMvc.perform(request);
        perform.andExpect(status().isOk())
            .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.code", is(Response.ok().build().getCode())))
            .andExpect(jsonPath("$.message", is(Response.ok().build().getMessage())));
    }

    @Test
    @DisplayName("Add a StatusCodeDoc with empty code")
    void addStatusCodeDoc_withEmptyCode() throws Exception{
        StatusCodeDocDto statusCodeDocDto = StatusCodeDocDto.builder()
            .projectId(PROJECT_ID)
            .build();
        doNothing().when(statusCodeDocService).add(statusCodeDocDto);
        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
            .post(Constants.STATUS_CODE_DOC_PATH)
            .contentType(MediaType.APPLICATION_JSON)
            .content(asJsonString(objectMapper, statusCodeDocDto));
        ResultActions perform = mockMvc.perform(request);
        perform.andExpect(status().isBadRequest())
            .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.code", is(PARAM_INVALIDATE_CODE)))
            .andExpect(jsonPath("$.message", is("StatusCode cannot be empty")));
    }

    @Test
    @DisplayName("Add a StatusCodeDoc with empty projectId")
    void addStatusCodeDoc_withEmptyProjectId() throws Exception {
        StatusCodeDocDto statusCodeDocDto = StatusCodeDocDto.builder()
            .code(CODE)
            .build();
        doNothing().when(statusCodeDocService).add(statusCodeDocDto);
        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
            .post(Constants.STATUS_CODE_DOC_PATH)
            .contentType(MediaType.APPLICATION_JSON)
            .content(asJsonString(objectMapper, statusCodeDocDto));
        ResultActions perform = mockMvc.perform(request);
        perform.andExpect(status().isBadRequest())
            .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.code", is(PARAM_INVALIDATE_CODE)))
            .andExpect(jsonPath("$.message", is("ProjectId cannot be empty")));
    }

}