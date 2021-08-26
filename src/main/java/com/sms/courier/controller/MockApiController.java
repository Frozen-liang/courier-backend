package com.sms.courier.controller;

import com.sms.courier.common.constant.Constants;
import com.sms.courier.common.validate.InsertGroup;
import com.sms.courier.dto.request.MockApiPageRequest;
import com.sms.courier.dto.request.MockApiRequest;
import com.sms.courier.dto.response.PageMockApiResponse;
import com.sms.courier.service.MockApiService;
import org.bson.types.ObjectId;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(Constants.MOCK_API_PATH)
public class MockApiController {

    private final MockApiService mockApiService;

    public MockApiController(MockApiService mockApiService) {
        this.mockApiService = mockApiService;
    }

    @PostMapping
    @PreAuthorize("hasRoleOrAdmin(@role.MOCK_API_GRE_UPD_DEL)")
    public Boolean add(@Validated(InsertGroup.class) @RequestBody MockApiRequest request) {
        return mockApiService.add(request);
    }

    @GetMapping("/page/{apiId}")
    public PageMockApiResponse page(@PathVariable ObjectId apiId, MockApiPageRequest pageRequest) {
        return mockApiService.page(apiId, pageRequest);
    }

}
