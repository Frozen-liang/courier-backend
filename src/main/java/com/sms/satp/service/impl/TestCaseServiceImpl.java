package com.sms.satp.service.impl;

import static com.sms.satp.common.ErrorCode.ADD_TEST_CASE_ERROR;
import static com.sms.satp.common.ErrorCode.DELETE_TEST_CASE_ERROR;
import static com.sms.satp.common.ErrorCode.EDIT_TEST_CASE_ERROR;
import static com.sms.satp.common.ErrorCode.GET_TEST_CASE_BY_ID_ERROR;

import com.sms.satp.common.ApiTestPlatformException;
import com.sms.satp.entity.dto.TestCaseDto;
import com.sms.satp.entity.test.TestCase;
import com.sms.satp.mapper.TestCaseMapper;
import com.sms.satp.repository.TestCaseRepository;
import com.sms.satp.service.TestCaseService;
import java.time.LocalDateTime;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class TestCaseServiceImpl implements TestCaseService {

    private final TestCaseRepository testCaseRepository;
    private final TestCaseMapper testCaseMapper;

    public TestCaseServiceImpl(TestCaseRepository testCaseRepository,
        TestCaseMapper testCaseMapper) {
        this.testCaseRepository = testCaseRepository;
        this.testCaseMapper = testCaseMapper;
    }

    @Override
    public TestCaseDto getDtoById(String id) {
        try {
            Optional<TestCase> testCaseOptional = testCaseRepository.findById(id);
            return testCaseMapper.toDto(testCaseOptional.orElse(null));
        } catch (Exception e) {
            log.error("Failed to get the TestCase By id!", e);
            throw new ApiTestPlatformException(GET_TEST_CASE_BY_ID_ERROR);
        }
    }

    @Override
    public void add(TestCaseDto testCaseDto) {
        log.info("TestCaseService-add()-params: [TestCase]={}", testCaseDto.toString());
        try {
            TestCase testCase = testCaseMapper.toEntity(testCaseDto);
            testCase.setId(new ObjectId().toString());
            testCase.setCreateDateTime(LocalDateTime.now());
            testCaseRepository.insert(testCase);
        } catch (Exception e) {
            log.error("Failed to add the TestCase!", e);
            throw new ApiTestPlatformException(ADD_TEST_CASE_ERROR);
        }
    }

    @Override
    public void edit(TestCaseDto testCaseDto) {
        log.info("TestCaseService-edit()-params: [TestCase]={}", testCaseDto.toString());
        try {
            TestCase testCase = testCaseMapper.toEntity(testCaseDto);
            Optional<TestCase> testCaseOptional = testCaseRepository.findById(testCase.getId());
            testCaseOptional.ifPresent(testCaseFindById -> {
                testCase.setCreateDateTime(testCaseFindById.getCreateDateTime());
                testCase.setModifyDateTime(LocalDateTime.now());
                testCaseRepository.save(testCase);
            });
        } catch (Exception e) {
            log.error("Failed to edit the TestCase!", e);
            throw new ApiTestPlatformException(EDIT_TEST_CASE_ERROR);
        }
    }

    @Override
    public void deleteById(String id) {
        try {
            testCaseRepository.deleteById(id);
        } catch (Exception e) {
            log.error("Failed to delete the TestCase!", e);
            throw new ApiTestPlatformException(DELETE_TEST_CASE_ERROR);
        }
    }

}