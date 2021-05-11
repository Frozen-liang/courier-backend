package com.sms.satp.controller;

import com.sms.satp.common.constant.Constants;
import com.sms.satp.common.response.Response;
import com.sms.satp.dto.GlobalFunctionRequest;
import com.sms.satp.dto.GlobalFunctionResponse;
import com.sms.satp.service.GlobalFunctionService;
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
@RequestMapping(Constants.GLOBAL_FUNCTION_PATH)
public class GlobalFunctionController {

    private final GlobalFunctionService globalFunctionService;

    public GlobalFunctionController(GlobalFunctionService globalFunctionService) {
        this.globalFunctionService = globalFunctionService;
    }

    @GetMapping("/{id}")
    public Response<GlobalFunctionResponse> getById(@PathVariable("id") String id) {
        return Response.ok(globalFunctionService.findById(id));
    }

    @GetMapping("/list")
    public Response<List<GlobalFunctionResponse>> list(String functionKey, String functionName) {
        return Response.ok(globalFunctionService.list(functionKey, functionName));
    }

    @PostMapping
    public Response<Boolean> add(@Validated @RequestBody GlobalFunctionRequest globalFunctionRequest) {
        globalFunctionService.add(globalFunctionRequest);
        return Response.ok(Boolean.TRUE);
    }

    @PutMapping
    public Response<Boolean> edit(@Validated @RequestBody GlobalFunctionRequest globalFunctionRequest) {
        globalFunctionService.edit(globalFunctionRequest);
        return Response.ok(Boolean.TRUE);
    }

    @DeleteMapping("/{ids}")
    public Response<Boolean> delete(@PathVariable String[] ids) {
        globalFunctionService.delete(ids);
        return Response.ok(Boolean.TRUE);
    }
}