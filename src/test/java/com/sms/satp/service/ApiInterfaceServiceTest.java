package com.sms.satp.service;


import static com.sms.satp.parser.DocumentFactoryTest.CONFIG_OPEN_API_V_3_YAML;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.sms.satp.entity.ApiInterface;
import com.sms.satp.entity.dto.ApiInterfaceDto;
import com.sms.satp.entity.dto.PageDto;
import com.sms.satp.mapper.ApiInterfaceMapper;
import com.sms.satp.parser.DocumentFactoryTest;
import com.sms.satp.repository.ApiInterfaceRepository;
import com.sms.satp.repository.ProjectRepository;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;

@SpringBootTest(classes = ApplicationTests.class,
    webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DisplayName("Test the service layer interface of the ApiInterface")
class ApiInterfaceServiceTest {

    @MockBean
    private ApiInterfaceRepository apiInterfaceRepository;

    @MockBean
    private ProjectRepository projectRepository;

    @SpyBean
    private ApiInterfaceService apiInterfaceService;

    @Autowired
    ApiInterfaceMapper apiInterfaceMapper;

    private final static int LIST_SIZE = 10;
    private final static int TOTAL_ELEMENTS = 60;
    private final static int PAGE_NUMBER = 2;
    private final static int PAGE_SIZE = 20;
    private final static int DEFAULT_PAGE_NUMBER = 0;
    private final static int DEFAULT_PAGE_SIZE = 10;
    private final static String PROJECT_ID = "25";
    private final static String ID = "123";
    private final static String TITLE = "title";
    private final static String NOT_EXIST_ID = "nul";
    private final static String DOCUMENT_TYPE_SWAGGER = "SWAGGER";
    private final static String URL = "https://meshdev.smsassist.com/filestorage/swagger/v1/swagger.json";


    @Test
    @DisplayName("Test the query method without query criteria")
    void list_test() {
        List<ApiInterface> apiInterfaces = new ArrayList<>();
        for (int i = 0; i < LIST_SIZE; i++) {
            apiInterfaces.add(
                ApiInterface.builder()
                    .title(TITLE)
                    .build());
        }
        ApiInterface apiInterface = ApiInterface.builder()
            .projectId(ID)
            .build();
        Example<ApiInterface> example = Example.of(apiInterface);
        when(apiInterfaceRepository.findAll(example)).thenReturn(apiInterfaces);
        List<ApiInterfaceDto> apiInterfaceList = apiInterfaceService.list(ID);
        assertThat(apiInterfaceList).allMatch(result -> StringUtils.equals(result.getTitle(), TITLE));
        assertThat(apiInterfaceList.size()).isEqualTo(LIST_SIZE);
    }


    @Test
    @DisplayName("Test the paging method with no parameters in the apiInterface service")
    void page_default_test() {
        ApiInterface apiInterface = ApiInterface.builder()
            .projectId(PROJECT_ID)
            .build();
        Example<ApiInterface> example = Example.of(apiInterface);
        PageDto pageDto = PageDto.builder().build();
        Sort sort = Sort.by(Direction.fromString(pageDto.getOrder()), pageDto.getSort());
        Pageable pageable = PageRequest.of(pageDto.getPageNumber(), pageDto.getPageSize(), sort);
        List<ApiInterface> apiInterfaceList = new ArrayList<>();
        for (int i = 0; i < TOTAL_ELEMENTS; i++) {
            apiInterfaceList.add(ApiInterface.builder().title(TITLE).build());
        }
        Page<ApiInterface> apiInterfacePage = new PageImpl<>(apiInterfaceList, pageable, TOTAL_ELEMENTS);
        when(apiInterfaceRepository.findAll(example, pageable)).thenReturn(apiInterfacePage);
        Page<ApiInterfaceDto> projectDtoPage = apiInterfaceService.page(pageDto, PROJECT_ID);
        assertThat(projectDtoPage.getTotalElements()).isEqualTo(TOTAL_ELEMENTS);
        assertThat(projectDtoPage.getPageable().getPageNumber()).isEqualTo(DEFAULT_PAGE_NUMBER);
        assertThat(projectDtoPage.getPageable().getPageSize()).isEqualTo(DEFAULT_PAGE_SIZE);
        assertThat(projectDtoPage.getContent()).allMatch(projectDto -> StringUtils.equals(projectDto.getTitle(), TITLE));
    }

    @Test
    @DisplayName("Test the paging method with specified parameters in the apiInterface service")
    void page_test() {
        ApiInterface apiInterface = ApiInterface.builder()
            .projectId(PROJECT_ID)
            .build();
        Example<ApiInterface> example = Example.of(apiInterface);
        PageDto pageDto = PageDto.builder()
            .pageNumber(PAGE_NUMBER)
            .pageSize(PAGE_SIZE)
            .order("asc")
            .build();
        Sort sort = Sort.by(Direction.fromString(pageDto.getOrder()), pageDto.getSort());
        Pageable pageable = PageRequest.of(pageDto.getPageNumber(), pageDto.getPageSize(), sort);
        List<ApiInterface> apiInterfaceList = new ArrayList<>();
        for (int i = 0; i < TOTAL_ELEMENTS; i++) {
            apiInterfaceList.add(ApiInterface.builder().title(TITLE).build());
        }
        Page<ApiInterface> apiInterfacePage = new PageImpl<>(apiInterfaceList, pageable, TOTAL_ELEMENTS);
        when(apiInterfaceRepository.findAll(example, pageable)).thenReturn(apiInterfacePage);
        Page<ApiInterfaceDto> projectDtoPage = apiInterfaceService.page(pageDto, PROJECT_ID);
        assertThat(projectDtoPage.getTotalElements()).isEqualTo(TOTAL_ELEMENTS);
        assertThat(projectDtoPage.getPageable().getPageNumber()).isEqualTo(PAGE_NUMBER);
        assertThat(projectDtoPage.getPageable().getPageSize()).isEqualTo(PAGE_SIZE);
        assertThat(projectDtoPage.getContent()).allMatch(projectDto -> StringUtils.equals(projectDto.getTitle(), TITLE));

    }

    @Test
    @DisplayName("Test the method of querying the API by id")
    void findApiInterfaceById() {
        ApiInterface apiInterface = ApiInterface.builder()
            .title(TITLE)
            .build();
        Optional<ApiInterface> apiInterfaceOptional = Optional.ofNullable(apiInterface);
        when(apiInterfaceRepository.findById(ID)).thenReturn(apiInterfaceOptional);
        ApiInterfaceDto result1 = apiInterfaceService.getApiInterfaceById(ID);
        ApiInterfaceDto result2 = apiInterfaceService.getApiInterfaceById(NOT_EXIST_ID);
        assertThat(result1.getTitle()).isEqualTo(TITLE);
        assertThat(result2).isNull();
    }

    @Test
    @DisplayName("Test the save method in the apiInterface service")
    void save() {
        List<ApiInterface> apiInterfaces = new ArrayList<>();
        for (int i = 0; i < LIST_SIZE; i++) {
            apiInterfaces.add(
                ApiInterface.builder()
                    .title(TITLE)
                    .build()
            );
        }
        when(apiInterfaceRepository.insert(anyList())).thenReturn(apiInterfaces);
        apiInterfaceService.save(URL, DOCUMENT_TYPE_SWAGGER, PROJECT_ID);
        verify(apiInterfaceRepository, times(1)).insert(anyList());
    }

    @Test
    @DisplayName("Test the add method in the apiInterface service")
    void add_test() {
        ApiInterfaceDto apiInterfaceDto = ApiInterfaceDto.builder().build();
        ApiInterface apiInterface = apiInterfaceMapper.toEntity(apiInterfaceDto);
        when(apiInterfaceRepository.insert(apiInterface)).thenReturn(apiInterface);
        apiInterfaceService.add(apiInterfaceDto);
        verify(apiInterfaceRepository, times(1)).insert(apiInterface);
    }

    @Test
    @DisplayName("Test the delete method in the apiInterface service")
    void delete_test() {
        doNothing().when(apiInterfaceRepository).deleteById(PROJECT_ID);
        apiInterfaceService.deleteById(PROJECT_ID);
        verify(apiInterfaceRepository, times(1)).deleteById(PROJECT_ID);
    }

    @Test
    void save2() {
        String location = DocumentFactoryTest.class.getResource(CONFIG_OPEN_API_V_3_YAML).toString();
    }
}