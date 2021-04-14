package com.sms.satp.controller;

import com.sms.satp.common.constant.Constants;
import com.sms.satp.common.response.Response;
import com.sms.satp.entity.dto.GlobalFunctionDto;
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
    public Response<GlobalFunctionDto> getById(@PathVariable("id") String id) {
        return Response.ok(globalFunctionService.findById(id));
    }

    @GetMapping("/list")
    public Response<List<GlobalFunctionDto>> list(String functionDesc, String functionName) {
        return Response.ok(globalFunctionService.list(functionDesc,functionName));
    }

    @PostMapping
    public Response<Boolean> add(@Validated @RequestBody GlobalFunctionDto globalFunctionDto) {
        globalFunctionService.add(globalFunctionDto);
        return Response.ok(Boolean.TRUE);
    }

    @PutMapping
    public Response<Boolean> edit(@Validated @RequestBody GlobalFunctionDto globalFunctionDto) {
        globalFunctionService.edit(globalFunctionDto);
        return Response.ok(Boolean.TRUE);
    }

    @DeleteMapping("/{ids}")
    public Response<Boolean> delete(@PathVariable String[] ids) {
        for (String id : ids) {
            globalFunctionService.delete(id);
        }
        return Response.ok(Boolean.TRUE);
    }
}