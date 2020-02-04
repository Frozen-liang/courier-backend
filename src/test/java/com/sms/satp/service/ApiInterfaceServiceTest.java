package com.sms.satp.service;


import static com.sms.satp.parser.DocumentFactoryTest.CONFIG_OPEN_API_V_3_YAML;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyIterable;
import static org.mockito.Mockito.when;

import com.sms.satp.entity.ApiInterface;
import com.sms.satp.parser.DocumentFactoryTest;
import com.sms.satp.repository.ApiInterfaceRepository;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;

@SpringBootTest
@DisplayName("Test the service layer interface of the ApiInterface")
class ApiInterfaceServiceTest {

    @MockBean
    private ApiInterfaceRepository apiInterfaceRepository;

    @SpyBean
    private ApiInterfaceService apiInterfaceService;

    private final static int LIST_SIZE = 10;
    private final static String ID = "123";
    private final static String TITLE = "title";
    private final static String NOT_EXIST_ID = "nul";
    private final static String DOCUMENT_TYPE_SWAGGER = "SWAGGER";
    private final static String URL = "https://meshdev.smsassist.com/filestorage/swagger/v1/swagger.json";


    @Test
    @DisplayName("Test the query method without query criteria")
    void list_test() {
        ApiInterface apiInterface;
        List<ApiInterface> apiInterfaces = new ArrayList<>();
        for (int i = 0; i < LIST_SIZE; i++) {
            apiInterface = ApiInterface.builder()
                .title(TITLE)
                .build();
            apiInterfaces.add(apiInterface);
        }
        when(apiInterfaceRepository.findAll()).thenReturn(apiInterfaces);
        List<ApiInterface> apiInterfaceList = apiInterfaceService.list();
        assertThat(apiInterfaceList).allMatch(result -> StringUtils.equals(result.getTitle(), TITLE));
        assertThat(apiInterfaceList.size()).isEqualTo(LIST_SIZE);
    }

    @Test
    @DisplayName("Test the method of querying the API by id")
    void findApiInterfaceById() {
        ApiInterface apiInterface = ApiInterface.builder()
            .title(TITLE)
            .build();
        Optional<ApiInterface> apiInterfaceOptional = Optional.ofNullable(apiInterface);
        when(apiInterfaceRepository.findById(ID)).thenReturn(apiInterfaceOptional);
        ApiInterface result1 = apiInterfaceService.getApiInterfaceById(ID);
        ApiInterface result2 = apiInterfaceService.getApiInterfaceById(NOT_EXIST_ID);
        assertThat(result1.getTitle()).isEqualTo(TITLE);
        assertThat(result2).isNull();
    }

    @Test
    void save() {
        List<ApiInterface> apiInterfaces = new ArrayList<>();
        for (int i = 0; i < LIST_SIZE; i++) {
            apiInterfaces.add(
                ApiInterface.builder()
                    .title(TITLE)
                    .build()
            );
        }
        when(apiInterfaceRepository.insert(anyIterable())).thenReturn(apiInterfaces);
        apiInterfaceService.save(URL, DOCUMENT_TYPE_SWAGGER);
    }

    @Test
    void save2() {
        String location = DocumentFactoryTest.class.getResource(CONFIG_OPEN_API_V_3_YAML).toString();
        apiInterfaceService.save(location, DOCUMENT_TYPE_SWAGGER);
    }
}