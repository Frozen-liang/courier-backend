package com.sms.courier.controller;

import static com.sms.courier.common.constant.Constants.MOCK_PATH;

import com.sms.courier.service.MockService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(MOCK_PATH)
public class MockController {

    private final MockService mockService;

    public MockController(MockService mockService) {
        this.mockService = mockService;
    }

    @PostMapping("/create")
    @PreAuthorize("hasRoleOrAdmin(@role.ADMIN)")
    public Boolean createMock() {
        return mockService.createMock();
    }

    @PostMapping("/restart")
    @PreAuthorize("hasRoleOrAdmin(@role.ADMIN)")
    public Boolean restartMock() {
        return mockService.restartMock();
    }

    @DeleteMapping("/delete")
    @PreAuthorize("hasRoleOrAdmin(@role.ADMIN)")
    public Boolean deleteMock() {
        return mockService.deleteMock();
    }

}
