package com.sms.satp.controller;

import com.sms.satp.common.constant.Constants;
import com.sms.satp.common.response.Response;
import com.sms.satp.dto.GlobalEnvironmentRequest;
import com.sms.satp.dto.GlobalEnvironmentResponse;
import com.sms.satp.service.GlobalEnvironmentService;
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
@RequestMapping(Constants.GLOBAL_ENVIRONMENT_PATH)
public class GlobalEnvironmentController {

    private final GlobalEnvironmentService globalEnvironmentService;

    public GlobalEnvironmentController(GlobalEnvironmentService globalEnvironmentService) {
        this.globalEnvironmentService = globalEnvironmentService;
    }

    @GetMapping("/{id}")
    public Response<GlobalEnvironmentResponse> getById(@PathVariable("id") String id) {
        return Response.ok(globalEnvironmentService.findById(id));
    }

    @PostMapping
    public Response<Boolean> add(@Validated @RequestBody GlobalEnvironmentRequest globalEnvironmentRequest) {
        globalEnvironmentService.add(globalEnvironmentRequest);
        return Response.ok(Boolean.TRUE);
    }

    @PutMapping
    public Response<Boolean> edit(@Validated @RequestBody GlobalEnvironmentRequest globalEnvironmentRequest) {
        globalEnvironmentService.edit(globalEnvironmentRequest);
        return Response.ok(Boolean.TRUE);
    }

    @GetMapping("/list")
    public Response<List<GlobalEnvironmentResponse>> list() {
        return Response.ok(globalEnvironmentService.list());
    }

    @DeleteMapping("/{ids}")
    public Response<Boolean> delete(@PathVariable("ids") String[] ids) {
        globalEnvironmentService.delete(ids);
        return Response.ok(Boolean.TRUE);
    }

}
