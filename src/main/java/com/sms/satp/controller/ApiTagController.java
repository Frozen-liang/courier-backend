package com.sms.satp.controller;

import com.sms.satp.common.constant.Constants;
import com.sms.satp.common.enums.ApiTagType;
import com.sms.satp.common.response.Response;
import com.sms.satp.dto.ApiTagRequest;
import com.sms.satp.dto.ApiTagResponse;
import com.sms.satp.service.ApiTagService;
import java.util.List;
import javax.validation.Valid;
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
    public Response<ApiTagResponse> getById(@PathVariable String id) {
        return Response.ok(apiTagService.findById(id));
    }

    @GetMapping("/list/{projectId}")
    public Response<List<ApiTagResponse>> list(@PathVariable("projectId") String projectId, String tagName,
        ApiTagType tagType) {
        return Response.ok(apiTagService.list(projectId, tagName, tagType));
    }

    @PostMapping
    public Response<Boolean> add(@Valid @RequestBody ApiTagRequest apiTagRequest) {
        apiTagService.add(apiTagRequest);
        return Response.ok(Boolean.TRUE);
    }

    @PutMapping
    public Response<Boolean> edit(@Valid @RequestBody ApiTagRequest apiTagRequest) {
        apiTagService.edit(apiTagRequest);
        return Response.ok(Boolean.TRUE);
    }

    @DeleteMapping("/{ids}")
    public Response<Boolean> delete(@PathVariable String[] ids) {
        for (String id : ids) {
            apiTagService.delete(id);
        }
        return Response.ok(Boolean.TRUE);
    }


}