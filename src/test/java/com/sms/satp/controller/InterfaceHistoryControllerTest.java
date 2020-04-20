package com.sms.satp.controller;

import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.sms.satp.common.constant.Constants;
import com.sms.satp.common.response.Response;
import com.sms.satp.entity.InterfaceHistory;
import com.sms.satp.entity.InterfaceShowInHistory;
import com.sms.satp.service.InterfaceHistoryService;
import java.util.ArrayList;
import java.util.List;
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

@WebMvcTest(value = InterfaceHistoryController.class)
@DisplayName("Tests for DataControllerTest")
class InterfaceHistoryControllerTest {

    @MockBean
    InterfaceHistoryService interfaceHistoryService;

    @Autowired
    private MockMvc mockMvc;

    private static final String ID = "ID";
    private final static String PROJECT_ID = "25";
    private final static String METHOD = "get";
    private final static String PATH = "/test";

    @Test
    @DisplayName("Get the interface history by id")
    void getInterfaceHistory_test() throws Exception {
        when(interfaceHistoryService.getHistoryById(ID)).thenReturn(InterfaceHistory.builder().build());
        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
            .get(Constants.INTERFACE_HISTORY_PATH + "/" + ID);
        ResultActions perform = mockMvc.perform(request);
        perform.andExpect(status().isOk())
            .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.code", is(Response.ok().build().getCode())))
            .andExpect(jsonPath("$.message", is(Response.ok().build().getMessage())));
    }

    @Test
    @DisplayName("Get the interface history list by projectId, method and path")
    void getInterfaceHistoryList_test() throws Exception {
        List<InterfaceShowInHistory> interfaceShowInHistoryList = new ArrayList<>();
        when(interfaceHistoryService.getHistoryList(PROJECT_ID, METHOD, PATH)).thenReturn(interfaceShowInHistoryList);
        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
            .get(Constants.INTERFACE_HISTORY_PATH + "/list/" + PROJECT_ID)
            .param("path", PATH)
            .param("method", METHOD);
        ResultActions perform = mockMvc.perform(request);
        perform.andExpect(status().isOk())
            .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.code", is(Response.ok().build().getCode())))
            .andExpect(jsonPath("$.message", is(Response.ok().build().getMessage())));
    }
}