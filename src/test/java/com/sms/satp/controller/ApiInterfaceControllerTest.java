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
import com.sms.satp.entity.criteria.InterfaceCriteria;
import com.sms.satp.entity.dto.ApiInterfaceDto;
import com.sms.satp.entity.dto.InterfaceGroupDto;
import com.sms.satp.entity.dto.PageDto;
import com.sms.satp.service.ApiInterfaceService;
import com.sms.satp.service.InterfaceGroupService;
import java.util.ArrayList;
import java.util.List;
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

@WebMvcTest(value = ApiInterfaceController.class)
@DisplayName("Tests for ApiInterfaceControllerTest")
@Import(TestConfig.class)
class ApiInterfaceControllerTest {

    @MockBean
    ApiInterfaceService apiInterfaceService;

    @MockBean
    InterfaceGroupService interfaceGroupService;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    private final static int LIST_SIZE = 10;
    private final static String TITLE = "title";
    private final static String PROJECT_ID = "25";
    private final static String GROUP_ID = "25";
    private final static String GROUP_NAME = "name";
    private final static String TAG = "tag";
    private final static String API_INTERFACE_ID = "25";
    private final static Integer PAGE_NUMBER = 3;
    private final static Integer PAGE_SIZE = 20;
    private static final String PARAM_INVALIDATE_CODE = "400";

    @Test
    @DisplayName("Query the page data for the ApiInterface by projectId and default query criteria")
    void getApiInterfacePageByDefaultRequirements() throws Exception {
        PageDto pageDto = PageDto.builder().build();
        InterfaceCriteria interfaceCriteria = InterfaceCriteria.builder()
            .groupId(GROUP_ID).projectId(PROJECT_ID).tag(TAG).build();
        when(apiInterfaceService.page(pageDto, interfaceCriteria)).thenReturn(null);
        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
            .get(Constants.INTERFACE_PATH + "/page/" + PROJECT_ID + "/" + GROUP_ID)
            .param("tag", TAG);
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
        InterfaceCriteria interfaceCriteria = InterfaceCriteria.builder()
            .groupId(GROUP_ID).projectId(PROJECT_ID).tag(TAG).build();
        when(apiInterfaceService.page(pageDto, interfaceCriteria)).thenReturn(null);
        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
            .get(Constants.INTERFACE_PATH + "/page/" + PROJECT_ID + "/" + GROUP_ID)
            .param("pageNumber", String.valueOf(PAGE_NUMBER))
            .param("pageSize", String.valueOf(PAGE_SIZE))
            .param("tag", TAG);
        ResultActions perform = mockMvc.perform(request);
        perform.andExpect(status().isOk())
            .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.code", is(Response.ok().build().getCode())))
            .andExpect(jsonPath("$.message", is(Response.ok().build().getMessage())));
    }

    @Test
    @DisplayName("Get its specific information through the id of the ApiInterface")
    void getInfoById() throws Exception {
        ApiInterfaceDto apiInterfaceDto = ApiInterfaceDto.builder().build();
        when(apiInterfaceService.findById(API_INTERFACE_ID)).thenReturn(apiInterfaceDto);
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
    void addApiInterface() throws Exception {
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
    @DisplayName("Edit the ApiInterface by id")
    void editApiInterface() throws Exception {
        ApiInterfaceDto apiInterfaceDto = ApiInterfaceDto.builder()
            .id(PROJECT_ID)
            .title(TITLE)
            .build();
        doNothing().when(apiInterfaceService).edit(apiInterfaceDto);
        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
            .put(Constants.INTERFACE_PATH)
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
    void deleteApiInterface() throws Exception {
        doNothing().when(apiInterfaceService).deleteById(API_INTERFACE_ID);
        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
            .delete(Constants.INTERFACE_PATH + "/" + API_INTERFACE_ID);
        ResultActions perform = mockMvc.perform(request);
        perform.andExpect(status().isOk())
            .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.code", is(Response.ok().build().getCode())))
            .andExpect(jsonPath("$.message", is(Response.ok().build().getMessage())));
    }

    @Test
    @DisplayName("Get the InterfaceGroup List")
    void getInterfaceGroupList() throws Exception {
        List<InterfaceGroupDto> interfaceGroupList = new ArrayList<>();
        for (int i = 0; i < LIST_SIZE; i++) {
            interfaceGroupList.add(
                InterfaceGroupDto.builder()
                    .name(GROUP_NAME)
                    .build());
        }
        when(interfaceGroupService.getGroupList(PROJECT_ID, null)).thenReturn(interfaceGroupList);
        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
            .get(Constants.INTERFACE_PATH + "/group/list/" + PROJECT_ID);
        ResultActions perform = mockMvc.perform(request);
        perform.andExpect(status().isOk())
            .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.code", is(Response.ok().build().getCode())))
            .andExpect(jsonPath("$.message", is(Response.ok().build().getMessage())));
    }

    @Test
    @DisplayName("Add InterfaceGroup")
    void addInterfaceGroup() throws Exception {
        InterfaceGroupDto interfaceGroupDto = InterfaceGroupDto.builder()
            .projectId(PROJECT_ID)
            .name(GROUP_NAME)
            .build();
        when(interfaceGroupService.addGroup(interfaceGroupDto)).thenReturn(GROUP_ID);
        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
            .post(Constants.INTERFACE_PATH + "/group")
            .contentType(MediaType.APPLICATION_JSON)
            .content(asJsonString(objectMapper, interfaceGroupDto));
        ResultActions perform = mockMvc.perform(request);
        perform.andExpect(status().isOk())
            .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.code", is(Response.ok().build().getCode())))
            .andExpect(jsonPath("$.message", is(Response.ok().build().getMessage())));
    }

    @Test
    @DisplayName("Edit the InterfaceGroup by id")
    void editInterfaceGroup() throws Exception {
        InterfaceGroupDto interfaceGroupDto = InterfaceGroupDto.builder()
            .id(GROUP_ID)
            .name(GROUP_NAME)
            .projectId(PROJECT_ID)
            .build();
        doNothing().when(interfaceGroupService).editGroup(interfaceGroupDto);
        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
            .put(Constants.INTERFACE_PATH + "/group")
            .contentType(MediaType.APPLICATION_JSON)
            .content(asJsonString(objectMapper, interfaceGroupDto));
        ResultActions perform = mockMvc.perform(request);
        perform.andExpect(status().isOk())
            .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.code", is(Response.ok().build().getCode())))
            .andExpect(jsonPath("$.message", is(Response.ok().build().getMessage())));
    }

    @Test
    @DisplayName("Delete the InterfaceGroup by id")
    void deleteInterfaceGroup() throws Exception {
        doNothing().when(interfaceGroupService).deleteGroup(GROUP_ID);
        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
            .delete(Constants.INTERFACE_PATH + "/group/" + GROUP_ID);
        ResultActions perform = mockMvc.perform(request);
        perform.andExpect(status().isOk())
            .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.code", is(Response.ok().build().getCode())))
            .andExpect(jsonPath("$.message", is(Response.ok().build().getMessage())));
    }

    @Test
    @DisplayName("Add InterfaceGroup with empty name")
    void addInterfaceGroup_withEmptyName() throws Exception {
        InterfaceGroupDto interfaceGroupDto = InterfaceGroupDto.builder().projectId(PROJECT_ID).build();
        when(interfaceGroupService.addGroup(interfaceGroupDto)).thenReturn(GROUP_ID);
        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
            .post(Constants.INTERFACE_PATH + "/group")
            .contentType(MediaType.APPLICATION_JSON)
            .content(asJsonString(objectMapper, interfaceGroupDto));
        ResultActions perform = mockMvc.perform(request);
        perform.andExpect(status().isBadRequest())
            .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.code", is(PARAM_INVALIDATE_CODE)))
            .andExpect(jsonPath("$.message", is("GroupName cannot be empty")));
    }

    @Test
    @DisplayName("Add InterfaceGroup with empty projectId")
    void addInterfaceGroup_withEmptyProjectId() throws Exception {
        InterfaceGroupDto interfaceGroupDto = InterfaceGroupDto.builder().name(GROUP_NAME).build();
        when(interfaceGroupService.addGroup(interfaceGroupDto)).thenReturn(GROUP_ID);
        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
            .post(Constants.INTERFACE_PATH + "/group")
            .contentType(MediaType.APPLICATION_JSON)
            .content(asJsonString(objectMapper, interfaceGroupDto));
        ResultActions perform = mockMvc.perform(request);
        perform.andExpect(status().isBadRequest())
            .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.code", is(PARAM_INVALIDATE_CODE)))
            .andExpect(jsonPath("$.message", is("ProjectId cannot be empty")));
    }


}