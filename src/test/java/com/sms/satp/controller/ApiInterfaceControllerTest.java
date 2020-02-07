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
import com.sms.satp.entity.dto.ApiInterfaceDto;
import com.sms.satp.entity.dto.PageDto;
import com.sms.satp.repository.ApiInterfaceRepository;
import com.sms.satp.repository.ProjectRepository;
import com.sms.satp.service.ApiInterfaceService;
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

@WebMvcTest(value = ApiInterfaceController.class)
@DisplayName("Tests for ApiInterfaceControllerTest")
class ApiInterfaceControllerTest {

    @MockBean
    ApiInterfaceRepository apiInterfaceRepository;

    @MockBean
    ProjectRepository projectRepository;

    @MockBean
    ApiInterfaceService apiInterfaceService;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    private final static String TITLE = "title";
    private final static String PROJECT_ID = "25";
    private final static String API_INTERFACE_ID = "25";
    private final static Integer PAGE_NUMBER = 3;
    private final static Integer PAGE_SIZE = 20;

    @Test
    @DisplayName("Query the page data for the ApiInterface by projectId and default query criteria")
    void getApiInterfacePageByDefaultRequirements() throws Exception {
        PageDto pageDto = PageDto.builder().build();
        when(apiInterfaceService.page(pageDto, PROJECT_ID)).thenReturn(null);
        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
            .get(Constants.INTERFACE_PATH + "/page/" + PROJECT_ID);
        ResultActions perform = mockMvc.perform(request);
        perform.andExpect(status().isOk())
            .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.code", is(Response.ok().build().getCode())))
            .andExpect(jsonPath("$.message", is(Response.ok().build().getMessage())));
    }

    @Test
    @DisplayName("Query the page data for the ApiInterface by projectId and specified query criteria")
    void getApiInterfacePageBySpecifiedRequirements() throws Exception {
        PageDto pageDto = PageDto.builder()
            .pageNumber(PAGE_NUMBER)
            .pageSize(PAGE_SIZE)
            .build();
        when(apiInterfaceService.page(pageDto, PROJECT_ID)).thenReturn(null);
        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
            .get(Constants.INTERFACE_PATH + "/page/" + PROJECT_ID)
            .param("pageNumber", String.valueOf(PAGE_NUMBER))
            .param("pageSize", String.valueOf(PAGE_SIZE));
        ResultActions perform = mockMvc.perform(request);
        perform.andExpect(status().isOk())
            .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.code", is(Response.ok().build().getCode())))
            .andExpect(jsonPath("$.message", is(Response.ok().build().getMessage())));
    }

    @Test
    @DisplayName("Get its specific information through the id of the ApiInterface")
    void getInfoById() throws Exception{
        ApiInterfaceDto apiInterfaceDto = ApiInterfaceDto.builder().build();
        when(apiInterfaceService.getApiInterfaceById(API_INTERFACE_ID)).thenReturn(apiInterfaceDto);
        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
            .get(Constants.INTERFACE_PATH + "/" + API_INTERFACE_ID);
        ResultActions perform = mockMvc.perform(request);
        perform.andExpect(status().isOk())
            .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.code", is(Response.ok().build().getCode())))
            .andExpect(jsonPath("$.message", is(Response.ok().build().getMessage())));
    }

    @Test
    @DisplayName("Add ApiInterface")
    void addApiInterface() throws Exception{
        ApiInterfaceDto apiInterfaceDto = ApiInterfaceDto.builder()
            .title(TITLE)
            .build();
        doNothing().when(apiInterfaceService).add(apiInterfaceDto);
        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
            .post(Constants.INTERFACE_PATH)
            .contentType(MediaType.APPLICATION_JSON)
            .content(asJsonString(objectMapper, apiInterfaceDto));
        ResultActions perform = mockMvc.perform(request);
        perform.andExpect(status().isOk())
            .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.code", is(Response.ok().build().getCode())))
            .andExpect(jsonPath("$.message", is(Response.ok().build().getMessage())));
    }

    @Test
    @DisplayName("Delete the ApiInterface by id")
    void deleteProject() throws Exception{
        doNothing().when(apiInterfaceService).deleteById(API_INTERFACE_ID);
        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
            .delete(Constants.INTERFACE_PATH + "/" + API_INTERFACE_ID);
        ResultActions perform = mockMvc.perform(request);
        perform.andExpect(status().isOk())
            .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.code", is(Response.ok().build().getCode())))
            .andExpect(jsonPath("$.message", is(Response.ok().build().getMessage())));
    }

}