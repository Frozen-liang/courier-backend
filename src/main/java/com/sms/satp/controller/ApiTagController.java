package com.sms.satp.controller;

import com.sms.satp.common.constant.Constants;
import com.sms.satp.common.validate.InsertGroup;
import com.sms.satp.common.validate.UpdateGroup;
import com.sms.satp.dto.request.ApiTagRequest;
import com.sms.satp.dto.response.ApiTagResponse;
import com.sms.satp.service.ApiTagService;
import java.util.Collections;
import java.util.List;
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

    @GetMapping("/list/{projectId}")
    public List<ApiTagResponse> list(@PathVariable("projectId") String projectId, String tagName,
        Integer tagType) {
        return apiTagService.list(projectId, tagName, tagType);
    }

    @PostMapping
    public Boolean add(@Validated(InsertGroup.class) @RequestBody ApiTagRequest apiTagRequest) {
        return apiTagService.add(apiTagRequest);
    }

    @PutMapping
    public Boolean edit(@Validated(UpdateGroup.class) @RequestBody ApiTagRequest apiTagRequest) {
        return apiTagService.edit(apiTagRequest);
    }

    @DeleteMapping("/{ids}")
    public Boolean delete(@PathVariable List<String> ids) {
        return apiTagService.delete(ids);
    }

}