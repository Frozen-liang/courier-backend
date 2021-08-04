package com.sms.courier.controller;

import com.sms.courier.common.constant.Constants;
import com.sms.courier.common.validate.InsertGroup;
import com.sms.courier.common.validate.UpdateGroup;
import com.sms.courier.dto.request.ApiTagListRequest;
import com.sms.courier.dto.request.ApiTagRequest;
import com.sms.courier.dto.response.ApiTagResponse;
import com.sms.courier.service.ApiTagService;
import java.util.List;
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
@RequestMapping(Constants.API_TAG_PATH)
public class ApiTagController {

    private final ApiTagService apiTagService;

    public ApiTagController(ApiTagService apiTagService) {
        this.apiTagService = apiTagService;
    }

    @GetMapping("/{id}")
    public ApiTagResponse getById(@PathVariable String id) {
        return apiTagService.findById(id);
    }

    @GetMapping("/list")
    @PreAuthorize("hasRoleOrAdmin(@role.TAG_QUERY_ALL)")
    public List<ApiTagResponse> list(@Validated ApiTagListRequest apiTagListRequest) {
        return apiTagService.list(apiTagListRequest);
    }

    @PostMapping
    @PreAuthorize("hasRoleOrAdmin(@role.TAG_CRE_UPD_DEL)")
    public Boolean add(@Validated(InsertGroup.class) @RequestBody ApiTagRequest apiTagRequest) {
        return apiTagService.add(apiTagRequest);
    }

    @PutMapping
    @PreAuthorize("hasRoleOrAdmin(@role.TAG_CRE_UPD_DEL)")
    public Boolean edit(@Validated(UpdateGroup.class) @RequestBody ApiTagRequest apiTagRequest) {
        return apiTagService.edit(apiTagRequest);
    }

    @DeleteMapping("/{ids}")
    @PreAuthorize("hasRoleOrAdmin(@role.TAG_CRE_UPD_DEL)")
    public Boolean delete(@PathVariable List<String> ids) {
        return apiTagService.delete(ids);
    }

}