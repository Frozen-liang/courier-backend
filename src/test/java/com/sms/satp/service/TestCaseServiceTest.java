package com.sms.satp.service;

import static com.sms.satp.common.ErrorCode.ADD_TEST_CASE_ERROR;
import static com.sms.satp.common.ErrorCode.DELETE_TEST_CASE_ERROR;
import static com.sms.satp.common.ErrorCode.EDIT_TEST_CASE_ERROR;
import static com.sms.satp.common.ErrorCode.GET_TEST_CASE_BY_ID_ERROR;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.sms.satp.ApplicationTests;
import com.sms.satp.common.ApiTestPlatformException;
import com.sms.satp.entity.dto.TestCaseDto;
import com.sms.satp.entity.test.TestCase;
import com.sms.satp.mapper.TestCaseMapper;
import com.sms.satp.repository.TestCaseRepository;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;

@SpringBootTest(classes = ApplicationTests.class)
@DisplayName("Test cases for TestCaseService")
class TestCaseServiceTest {

    @MockBean
    private TestCaseRepository testCaseRepository;
    
    @SpyBean
    private TestCaseService testCaseService;
    
    @SpyBean
    private TestCaseMapper testCaseMapper;

    private final static String ID = "25";
    private final static String NOT_EXIST_ID = "30";
    private final static String PROJECT_ID = "25";
    private final static String NAME = "name";
    

    @Test
    @DisplayName("Test the add method in the TestCase service")
    void add_test() {
        TestCaseDto testCaseDto = TestCaseDto.builder().build();
        TestCase testCase = testCaseMapper.toEntity(testCaseDto);
        when(testCaseRepository.insert(testCase)).thenReturn(testCase);
        testCaseService.add(testCaseDto);
        verify(testCaseRepository, times(1)).insert(any(TestCase.class));
    }

    @Test
    @DisplayName("Test the edit method in the TestCase service")
    void edit_test() {
        TestCaseDto testCaseDto = TestCaseDto.builder().id(ID).build();
        TestCase testCase = testCaseMapper.toEntity(testCaseDto);
        when(testCaseRepository.findById(ID)).thenReturn(Optional.of(TestCase.builder().build()));
        when(testCaseRepository.save(testCase)).thenReturn(testCase);
        testCaseService.edit(testCaseDto);
        verify(testCaseRepository, times(1)).save(any(TestCase.class));
    }

    @Test
    @DisplayName("Test the method of querying the TestCase by id")
    void findById_test() {
        TestCase statusCodeDoc = TestCase.builder()
            .name(NAME)
            .build();
        Optional<TestCase> statusCodeDocOptional = Optional.ofNullable(statusCodeDoc);
        when(testCaseRepository.findById(ID)).thenReturn(statusCodeDocOptional);
        TestCaseDto result1 = testCaseService.getDtoById(ID);
        TestCaseDto result2 = testCaseService.getDtoById(NOT_EXIST_ID);
        assertThat(result1.getName()).isEqualTo(NAME);
        assertThat(result2).isNull();
    }

    @Test
    @DisplayName("Test the delete method in the TestCase service")
    void delete_test() {
        doNothing().when(testCaseRepository).deleteById(PROJECT_ID);
        testCaseService.deleteById(PROJECT_ID);
        verify(testCaseRepository, times(1)).deleteById(PROJECT_ID);
    }


    @Test
    @DisplayName("An exception occurred while adding testCase")
    void add_exception_test() {
        doThrow(new RuntimeException()).when(testCaseRepository).insert(any(TestCase.class));
        assertThatThrownBy(() -> testCaseService.add(TestCaseDto.builder().build()))
            .isInstanceOf(ApiTestPlatformException.class)
            .extracting("code").isEqualTo(ADD_TEST_CASE_ERROR.getCode());
    }

    @Test
    @DisplayName("An exception occurred while edit testCase")
    void edit_exception_test() {
        when(testCaseRepository.findById(ID)).thenReturn(Optional.of(TestCase.builder().build()));
        doThrow(new RuntimeException()).when(testCaseRepository).save(argThat(t -> true));
        assertThatThrownBy(() -> testCaseService.edit(TestCaseDto.builder().id(ID).build()))
            .isInstanceOf(ApiTestPlatformException.class)
            .extracting("code").isEqualTo(EDIT_TEST_CASE_ERROR.getCode());
    }

    @Test
    @DisplayName("An exception occurred while getting statusCodeDoc by id")
    void getStatusCodeDoc_exception_test() {
        doThrow(new RuntimeException()).when(testCaseRepository).findById(anyString());
        assertThatThrownBy(() -> testCaseService.getDtoById(anyString()))
            .isInstanceOf(ApiTestPlatformException.class)
            .extracting("code").isEqualTo(GET_TEST_CASE_BY_ID_ERROR.getCode());
    }

    @Test
    @DisplayName("An exception occurred while delete testCase")
    void delete_exception_test() {
        doThrow(new RuntimeException()).when(testCaseRepository).deleteById(anyString());
        assertThatThrownBy(() -> testCaseService.deleteById(anyString()))
            .isInstanceOf(ApiTestPlatformException.class)
            .extracting("code").isEqualTo(DELETE_TEST_CASE_ERROR.getCode());
    }

}