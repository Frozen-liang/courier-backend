package com.sms.courier.controller;

import com.sms.courier.common.constant.Constants;
import com.sms.courier.common.validate.InsertGroup;
import com.sms.courier.common.validate.UpdateGroup;
import com.sms.courier.dto.request.MockApiPageRequest;
import com.sms.courier.dto.request.MockApiRequest;
import com.sms.courier.dto.response.MockApiResponseList;
import com.sms.courier.dto.response.MockApiResponsePage;
import com.sms.courier.service.MockApiService;
import java.util.List;
import org.bson.types.ObjectId;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
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
    @PreAuthorize("hasRoleOrAdmin(@role.MOCK_API_QUERY_ALL)")
    public MockApiResponsePage page(@PathVariable ObjectId apiId, MockApiPageRequest pageRequest) {
        return mockApiService.page(apiId, pageRequest);
    }

    @GetMapping("/list/{apiId}")
    @PreAuthorize("hasRoleOrAdmin(@role.MOCK_API_QUERY_ALL)")
    public MockApiResponseList list(@PathVariable ObjectId apiId, Boolean isEnable) {
        return mockApiService.list(apiId, isEnable);
    }

    @GetMapping("/mock/list/{apiId}")
    @PreAuthorize("hasRoleOrAdmin(@role.MOCK_API_MOCK_QUERY_ALL)")
    public MockApiResponseList mockList(@PathVariable ObjectId apiId, Boolean isEnable) {
        return mockApiService.list(apiId, isEnable);
    }

    @PutMapping
    @PreAuthorize("hasRoleOrAdmin(@role.MOCK_API_GRE_UPD_DEL)")
    public Boolean edit(@Validated(UpdateGroup.class) @RequestBody MockApiRequest request) {
        return mockApiService.edit(request);
    }

    @DeleteMapping("/delete/{ids}")
    @PreAuthorize("hasRoleOrAdmin(@role.MOCK_API_GRE_UPD_DEL)")
    public Boolean deleteByIds(@PathVariable List<String> ids) {
        return mockApiService.deleteByIds(ids);
    }

}
