package com.sms.satp.service;


import static com.sms.satp.common.ErrorCode.ADD_API_INTERFACE_ERROR;
import static com.sms.satp.common.ErrorCode.DELETE_API_INTERFACE_BY_ID_ERROR;
import static com.sms.satp.common.ErrorCode.DOCUMENT_TYPE_ERROR;
import static com.sms.satp.common.ErrorCode.EDIT_API_INTERFACE_ERROR;
import static com.sms.satp.common.ErrorCode.GET_API_INTERFACE_BY_ID_ERROR;
import static com.sms.satp.common.ErrorCode.GET_API_INTERFACE_LIST_ERROR;
import static com.sms.satp.common.ErrorCode.GET_API_INTERFACE_PAGE_ERROR;
import static com.sms.satp.common.ErrorCode.PARSE_FILE_AND_SAVE_AS_APIINTERFACE_ERROR;
import static com.sms.satp.parser.DocumentFactoryTest.CONFIG_OPEN_API_V_3_YAML;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.sms.satp.common.ApiTestPlatformException;
import com.sms.satp.entity.ApiInterface;
import com.sms.satp.entity.dto.ApiInterfaceDto;
import com.sms.satp.entity.dto.PageDto;
import com.sms.satp.mapper.ApiInterfaceMapper;
import com.sms.satp.parser.DocumentFactoryTest;
import com.sms.satp.repository.ApiInterfaceRepository;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.mock.web.MockMultipartFile;

@Disabled
@DisplayName("Test cases for ApiInterfaceService")
class ApiInterfaceServiceTest {

    @MockBean
    private ApiInterfaceRepository apiInterfaceRepository;

    @SpyBean
    private ApiInterfaceService apiInterfaceService;

    @SpyBean
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
    private final static String WRONG_DOCUMENT_TYPE = "wrong";
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
    @DisplayName("Test the save method with wrong type in the apiInterface service")
    void save_with_wrong_type() {
        assertThatThrownBy(() -> apiInterfaceService.save(URL, WRONG_DOCUMENT_TYPE, PROJECT_ID))
            .isInstanceOf(ApiTestPlatformException.class)
            .extracting("code").isEqualTo(DOCUMENT_TYPE_ERROR.getCode());
    }

    @Test
    @DisplayName("Test the method of saving by file in the apiInterface service")
    void save2() throws IOException {
        List<ApiInterface> apiInterfaces = new ArrayList<>();
        for (int i = 0; i < LIST_SIZE; i++) {
            apiInterfaces.add(
                ApiInterface.builder()
                    .title(TITLE)
                    .build()
            );
        }
        when(apiInterfaceRepository.insert(anyList())).thenReturn(apiInterfaces);
        String location = DocumentFactoryTest.class.getResource(CONFIG_OPEN_API_V_3_YAML).getPath();
        File file = new File(location);
        FileInputStream fileInputStream = new FileInputStream(file);
        MockMultipartFile mockMultipartFile = new MockMultipartFile(file.getName(),fileInputStream);
        apiInterfaceService.save(mockMultipartFile, DOCUMENT_TYPE_SWAGGER, PROJECT_ID);
        verify(apiInterfaceRepository, times(1)).insert(anyList());
    }

    @Test
    @DisplayName("Test the add method in the apiInterface service")
    void add_test() {
        ApiInterfaceDto apiInterfaceDto = ApiInterfaceDto.builder().build();
        ApiInterface apiInterface = apiInterfaceMapper.toEntity(apiInterfaceDto);
        when(apiInterfaceRepository.insert(apiInterface)).thenReturn(apiInterface);
        apiInterfaceService.add(apiInterfaceDto);
        verify(apiInterfaceRepository, times(1)).insert(any(ApiInterface.class));
    }

    @Test
    @DisplayName("Test the edit method in the apiInterface service")
    void edit_test() {
        ApiInterfaceDto apiInterfaceDto = ApiInterfaceDto.builder().id(ID).build();
        ApiInterface apiInterface = apiInterfaceMapper.toEntity(apiInterfaceDto);
        when(apiInterfaceRepository.findById(ID)).thenReturn(Optional.of(ApiInterface.builder().build()));
        when(apiInterfaceRepository.save(apiInterface)).thenReturn(apiInterface);
        apiInterfaceService.edit(apiInterfaceDto);
        verify(apiInterfaceRepository, times(1)).save(any(ApiInterface.class));
    }

    @Test
    @DisplayName("Test the delete method in the apiInterface service")
    void delete_test() {
        doNothing().when(apiInterfaceRepository).deleteById(PROJECT_ID);
        apiInterfaceService.deleteById(PROJECT_ID);
        verify(apiInterfaceRepository, times(1)).deleteById(PROJECT_ID);
    }

    @Test
    @DisplayName("An exception occurred while getting apiInterface list")
    void list_exception_test() {
        ApiInterface apiInterface = ApiInterface.builder().projectId(PROJECT_ID).build();
        doThrow(new RuntimeException()).when(apiInterfaceRepository).findAll(Example.of(apiInterface));
        assertThatThrownBy(() -> apiInterfaceService.list(PROJECT_ID))
            .isInstanceOf(ApiTestPlatformException.class)
            .extracting("code").isEqualTo(GET_API_INTERFACE_LIST_ERROR.getCode());
    }

    @Test
    @DisplayName("An exception occurred while getting apiInterface page")
    void page_exception_test() {
        ApiInterface apiInterface = ApiInterface.builder().projectId(PROJECT_ID).build();
        PageDto pageDto = PageDto.builder().build();
        Sort sort = Sort.by(Direction.fromString(pageDto.getOrder()), pageDto.getSort());
        Pageable pageable = PageRequest.of(
            pageDto.getPageNumber(), pageDto.getPageSize(), sort);
        doThrow(new RuntimeException()).when(apiInterfaceRepository).findAll(Example.of(apiInterface), pageable);
        assertThatThrownBy(() -> apiInterfaceService.page(pageDto, PROJECT_ID))
            .isInstanceOf(ApiTestPlatformException.class)
            .extracting("code").isEqualTo(GET_API_INTERFACE_PAGE_ERROR.getCode());
    }

    @Test
    @DisplayName("An exception occurred while adding apiInterface")
    void add_exception_test() {
        doThrow(new RuntimeException()).when(apiInterfaceRepository).insert(any(ApiInterface.class));
        assertThatThrownBy(() -> apiInterfaceService.add(ApiInterfaceDto.builder().build()))
            .isInstanceOf(ApiTestPlatformException.class)
            .extracting("code").isEqualTo(ADD_API_INTERFACE_ERROR.getCode());
    }

    @Test
    @DisplayName("An exception occurred while edit apiInterface")
    void edit_exception_test() {
        when(apiInterfaceRepository.findById(ID)).thenReturn(Optional.of(ApiInterface.builder().id(ID).build()));
        doThrow(new RuntimeException()).when(apiInterfaceRepository).save(argThat(t -> true));
        assertThatThrownBy(() -> apiInterfaceService.edit(ApiInterfaceDto.builder().id(ID).build()))
            .isInstanceOf(ApiTestPlatformException.class)
            .extracting("code").isEqualTo(EDIT_API_INTERFACE_ERROR.getCode());
    }

    @Test
    @DisplayName("An exception occurred while adding apiInterface through uploaded files")
    void addByFile_exception_test() {
        doThrow(new RuntimeException()).when(apiInterfaceRepository).insert(anyList());
        assertThatThrownBy(() -> apiInterfaceService.save(URL, DOCUMENT_TYPE_SWAGGER, PROJECT_ID))
            .isInstanceOf(ApiTestPlatformException.class)
            .extracting("code").isEqualTo(PARSE_FILE_AND_SAVE_AS_APIINTERFACE_ERROR.getCode());
    }

    @Test
    @DisplayName("An exception occurred while getting apiInterface by id")
    void getApiInterface_exception_test() {
        doThrow(new RuntimeException()).when(apiInterfaceRepository).findById(anyString());
        assertThatThrownBy(() -> apiInterfaceService.getApiInterfaceById(anyString()))
            .isInstanceOf(ApiTestPlatformException.class)
            .extracting("code").isEqualTo(GET_API_INTERFACE_BY_ID_ERROR.getCode());
    }

    @Test
    @DisplayName("An exception occurred while deleting apiInterface")
    void delete_exception_test() {
        doThrow(new RuntimeException()).when(apiInterfaceRepository).deleteById(anyString());
        assertThatThrownBy(() -> apiInterfaceService.deleteById(anyString()))
            .isInstanceOf(ApiTestPlatformException.class)
            .extracting("code").isEqualTo(DELETE_API_INTERFACE_BY_ID_ERROR.getCode());
    }
}