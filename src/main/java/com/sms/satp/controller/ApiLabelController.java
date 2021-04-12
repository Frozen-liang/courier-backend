package com.sms.satp.controller;

import com.sms.satp.common.constant.Constants;
import com.sms.satp.common.response.Response;
import com.sms.satp.entity.dto.ApiLabelDto;
import com.sms.satp.service.ApiLabelService;
import java.util.List;
import javax.validation.Valid;
import org.bson.types.ObjectId;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(Constants.API_LABEL_PATH)
public class ApiLabelController {

    private final ApiLabelService apiLabelService;

    public ApiLabelController(ApiLabelService apiLabelService) {
        this.apiLabelService = apiLabelService;
    }


    @GetMapping("/{id}")
    public Response<ApiLabelDto> getById(@PathVariable ObjectId id) {
        return Response.ok(apiLabelService.findById(id));
    }

    @GetMapping("/list/{projectId}")
    public Response<List<ApiLabelDto>> list(@PathVariable("projectId") String projectId, String labelName,
        Short labelType) {
        return Response.ok(apiLabelService.list(projectId, labelName, labelType));
    }

    @PostMapping
    public Response<Boolean> add(@Valid @RequestBody ApiLabelDto apiLabelDto) {
        apiLabelService.add(apiLabelDto);
        return Response.ok(Boolean.TRUE);
    }

    @PutMapping
    public Response<Boolean> edit(@Valid @RequestBody ApiLabelDto apiLabelDto) {
        apiLabelService.edit(apiLabelDto);
        return Response.ok(Boolean.TRUE);
    }

    @DeleteMapping("/{ids}")
    public Response<Boolean> delete(@PathVariable ObjectId[] ids) {
        for (ObjectId id : ids) {
            apiLabelService.delete(id);
        }
        return Response.ok(Boolean.TRUE);
    }


}