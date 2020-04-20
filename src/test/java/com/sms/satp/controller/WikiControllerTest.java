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
import com.sms.satp.entity.dto.PageDto;
import com.sms.satp.entity.dto.WikiDto;
import com.sms.satp.service.WikiService;
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

@WebMvcTest(value = WikiController.class)
@DisplayName("Tests for WikiController")
class WikiControllerTest {
    
    @MockBean
    private WikiService wikiService;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    private final static Integer PAGE_NUMBER = 3;
    private final static Integer PAGE_SIZE = 20;
    private final static String ID = "30";
    private final static String PROJECT_ID = "id";
    private final static String TITLE = "name";

    @Test
    @DisplayName("Query the page data for the Wiki by default query criteria")
    void getWikiPageByDefaultRequirements() throws Exception {
        PageDto pageDto = PageDto.builder().build();
        when(wikiService.page(pageDto, PROJECT_ID)).thenReturn(null);
        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
            .get(Constants.WIKI_PATH + "/page/" + PROJECT_ID);
        ResultActions perform = mockMvc.perform(request);
        perform.andExpect(status().isOk())
            .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.code", is(Response.ok().build().getCode())))
            .andExpect(jsonPath("$.message", is(Response.ok().build().getMessage())));
    }

    @Test
    @DisplayName("Query the page data for the Wiki by specified query criteria")
    void getWikiPageBySpecifiedRequirements() throws Exception {
        PageDto pageDto = PageDto.builder()
            .pageNumber(PAGE_NUMBER)
            .pageSize(PAGE_SIZE)
            .build();
        when(wikiService.page(pageDto, PROJECT_ID)).thenReturn(null);
        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
            .get(Constants.WIKI_PATH + "/page/" + PROJECT_ID)
            .param("pageNumber", String.valueOf(PAGE_NUMBER))
            .param("pageSize", String.valueOf(PAGE_SIZE));
        ResultActions perform = mockMvc.perform(request);
        perform.andExpect(status().isOk())
            .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.code", is(Response.ok().build().getCode())))
            .andExpect(jsonPath("$.message", is(Response.ok().build().getMessage())));
    }

    @Test
    @DisplayName("Add a Wiki")
    void addWiki() throws Exception{
        WikiDto wikiDto = WikiDto.builder()
            .title(TITLE)
            .build();
        doNothing().when(wikiService).add(wikiDto);
        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
            .post(Constants.WIKI_PATH)
            .contentType(MediaType.APPLICATION_JSON)
            .content(asJsonString(objectMapper, wikiDto));
        ResultActions perform = mockMvc.perform(request);
        perform.andExpect(status().isOk())
            .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.code", is(Response.ok().build().getCode())))
            .andExpect(jsonPath("$.message", is(Response.ok().build().getMessage())));
    }

    @Test
    @DisplayName("Edit the Wiki by id")
    void editWiki() throws Exception{
        WikiDto wikiDto = WikiDto.builder()
            .id(PROJECT_ID)
            .title(TITLE)
            .build();
        doNothing().when(wikiService).edit(wikiDto);
        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
            .put(Constants.WIKI_PATH)
            .contentType(MediaType.APPLICATION_JSON)
            .content(asJsonString(objectMapper, wikiDto));
        ResultActions perform = mockMvc.perform(request);
        perform.andExpect(status().isOk())
            .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.code", is(Response.ok().build().getCode())))
            .andExpect(jsonPath("$.message", is(Response.ok().build().getMessage())));
    }

    @Test
    @DisplayName("Get its specific information through the id of the Wiki")
    void getInfoById() throws Exception{
        WikiDto wikiDto = WikiDto.builder().build();
        when(wikiService.findById(ID)).thenReturn(wikiDto);
        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
            .get(Constants.WIKI_PATH + "/" + ID);
        ResultActions perform = mockMvc.perform(request);
        perform.andExpect(status().isOk())
            .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.code", is(Response.ok().build().getCode())))
            .andExpect(jsonPath("$.message", is(Response.ok().build().getMessage())));
    }

    @Test
    @DisplayName("Delete the Wiki by id")
    void deleteWiki() throws Exception{
        doNothing().when(wikiService).deleteById(PROJECT_ID);
        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
            .delete(Constants.WIKI_PATH + "/" + PROJECT_ID);
        ResultActions perform = mockMvc.perform(request);
        perform.andExpect(status().isOk())
            .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.code", is(Response.ok().build().getCode())))
            .andExpect(jsonPath("$.message", is(Response.ok().build().getMessage())));
    }

}