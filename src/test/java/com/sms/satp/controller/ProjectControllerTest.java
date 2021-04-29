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
import com.sms.satp.dto.PageDto;
import com.sms.satp.dto.ProjectDto;
import com.sms.satp.service.ProjectService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

@WebMvcTest(value = ProjectController.class)
@DisplayName("Tests for ProjectControllerTest")
@Import(TestConfig.class)
class ProjectControllerTest {

    @MockBean
    ProjectService projectService;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    private final static Integer PAGE_NUMBER = 3;
    private final static Integer PAGE_SIZE = 20;
    private final static String ID = "30";
    private final static String PROJECT_ID = "id";
    private final static String PROJECT_NAME = "name";
    private static final String PARAM_INVALIDATE_CODE = "400";

    @Test
    @DisplayName("Query the page data for the Project by default query criteria")
    void getProjectPageByDefaultRequirements() throws Exception {
        PageDto pageDto = PageDto.builder().build();
        when(projectService.page(pageDto)).thenReturn(null);
        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
            .get(Constants.PROJECT_PATH + "/page");
        ResultActions perform = mockMvc.perform(request);
        perform.andExpect(status().isOk())
            .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.code", is(Response.ok().build().getCode())))
            .andExpect(jsonPath("$.message", is(Response.ok().build().getMessage())));
    }

    @Test
    @DisplayName("Query the page data for the Project by specified query criteria")
    void getProjectPageBySpecifiedRequirements() throws Exception {
        PageDto pageDto = PageDto.builder()
            .pageNumber(PAGE_NUMBER)
            .pageSize(PAGE_SIZE)
            .build();
        when(projectService.page(pageDto)).thenReturn(null);
        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
            .get(Constants.PROJECT_PATH + "/page")
            .param("pageNumber", String.valueOf(PAGE_NUMBER))
            .param("pageSize", String.valueOf(PAGE_SIZE));
        ResultActions perform = mockMvc.perform(request);
        perform.andExpect(status().isOk())
            .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.code", is(Response.ok().build().getCode())))
            .andExpect(jsonPath("$.message", is(Response.ok().build().getMessage())));
    }

    @Test
    @DisplayName("Add a Project")
    void addProject() throws Exception{
        ProjectDto projectDto = ProjectDto.builder()
            .name(PROJECT_NAME)
            .build();
        doNothing().when(projectService).add(projectDto);
        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
            .post(Constants.PROJECT_PATH)
            .contentType(MediaType.APPLICATION_JSON)
            .content(asJsonString(objectMapper, projectDto));
        ResultActions perform = mockMvc.perform(request);
        perform.andExpect(status().isOk())
            .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.code", is(Response.ok().build().getCode())))
            .andExpect(jsonPath("$.message", is(Response.ok().build().getMessage())));
    }

    @Test
    @DisplayName("Edit the Project by id")
    void editProject() throws Exception{
        ProjectDto projectDto = ProjectDto.builder()
            .id(PROJECT_ID)
            .name(PROJECT_NAME)
            .build();
        doNothing().when(projectService).edit(projectDto);
        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
            .put(Constants.PROJECT_PATH)
            .contentType(MediaType.APPLICATION_JSON)
            .content(asJsonString(objectMapper, projectDto));
        ResultActions perform = mockMvc.perform(request);
        perform.andExpect(status().isOk())
            .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.code", is(Response.ok().build().getCode())))
            .andExpect(jsonPath("$.message", is(Response.ok().build().getMessage())));
    }

    @Test
    @DisplayName("Get its specific information through the id of the Project")
    void getInfoById() throws Exception{
        ProjectDto projectDto = ProjectDto.builder().build();
        when(projectService.findById(ID)).thenReturn(projectDto);
        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
            .get(Constants.PROJECT_PATH + "/" + ID);
        ResultActions perform = mockMvc.perform(request);
        perform.andExpect(status().isOk())
            .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.code", is(Response.ok().build().getCode())))
            .andExpect(jsonPath("$.message", is(Response.ok().build().getMessage())));
    }

    @Test
    @DisplayName("Delete the Project by id")
    void deleteProject() throws Exception{
        doNothing().when(projectService).delete(PROJECT_ID);
        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
            .delete(Constants.PROJECT_PATH + "/" + PROJECT_ID);
        ResultActions perform = mockMvc.perform(request);
        perform.andExpect(status().isOk())
            .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.code", is(Response.ok().build().getCode())))
            .andExpect(jsonPath("$.message", is(Response.ok().build().getMessage())));
    }

    @Test
    @DisplayName("Add a Project with Empty name")
    void addProject_withEmptyName() throws Exception{
        ProjectDto projectDto = ProjectDto.builder()
            .build();
        doNothing().when(projectService).add(projectDto);
        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
            .post(Constants.PROJECT_PATH)
            .contentType(MediaType.APPLICATION_JSON)
            .content(asJsonString(objectMapper, projectDto));
        ResultActions perform = mockMvc.perform(request);
        perform.andExpect(status().isBadRequest())
            .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.code", is(PARAM_INVALIDATE_CODE)))
            .andExpect(jsonPath("$.message", is("Project name cannot be empty")));
    }

}