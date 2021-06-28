package com.sms.satp.controller;

import com.sms.satp.common.constant.Constants;
import com.sms.satp.common.validate.InsertGroup;
import com.sms.satp.common.validate.UpdateGroup;
import com.sms.satp.dto.request.GlobalFunctionRequest;
import com.sms.satp.dto.response.GlobalFunctionResponse;
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
    public GlobalFunctionResponse getById(@PathVariable("id") String id) {
        return globalFunctionService.findById(id);
    }

    @GetMapping("/list")
    public List<GlobalFunctionResponse> list(String workspaceId, String functionKey,
        String functionName) {
        return globalFunctionService.list(workspaceId, functionKey, functionName);
    }

    @PostMapping
    public Boolean add(@Validated(InsertGroup.class) @RequestBody GlobalFunctionRequest globalFunctionRequest) {
        return globalFunctionService.add(globalFunctionRequest);
    }

    @PutMapping
    public Boolean edit(@Validated(UpdateGroup.class) @RequestBody GlobalFunctionRequest globalFunctionRequest) {
        return globalFunctionService.edit(globalFunctionRequest);
    }

    @DeleteMapping("/{ids}")
    public Boolean delete(@PathVariable List<String> ids) {
        return globalFunctionService.delete(ids);
    }
}