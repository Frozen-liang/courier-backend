package com.sms.satp.service;

import com.sms.satp.entity.dto.TestCaseDto;

public interface TestCaseService {

    TestCaseDto getDtoById(String id);

    void add(TestCaseDto testCaseDto);

    void edit(TestCaseDto testCaseDto);

    void deleteById(String id);
}
