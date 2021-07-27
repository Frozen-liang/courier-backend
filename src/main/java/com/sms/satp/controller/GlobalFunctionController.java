package com.sms.satp.controller;

import com.sms.satp.common.constant.Constants;
import com.sms.satp.common.validate.InsertGroup;
import com.sms.satp.common.validate.UpdateGroup;
import com.sms.satp.dto.request.GlobalFunctionRequest;
import com.sms.satp.dto.response.GlobalFunctionResponse;
import com.sms.satp.service.GlobalFunctionService;
import java.util.List;
import java.util.Map;
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
    @PreAuthorize("hasRoleOrAdmin(@role.GLOBAL_FUN_QUERY_ALL)")
    public List<GlobalFunctionResponse> list(String workspaceId, String functionKey,
        String functionName) {
        return globalFunctionService.list(workspaceId, functionKey, functionName);
    }

    @GetMapping("/find-all")
    @PreAuthorize("hasRole(@role.GLOBAL_FUN_FIND_ALL)")
    public Map<String, List<GlobalFunctionResponse>> findAll() {
        // Query all global function. Used for engine.
        return globalFunctionService.findAll();
    }

    @PostMapping
    @PreAuthorize("hasRoleOrAdmin(@role.GLOBAL_FUN_CREATE)")
    public Boolean add(@Validated(InsertGroup.class) @RequestBody GlobalFunctionRequest globalFunctionRequest) {
        return globalFunctionService.add(globalFunctionRequest);
    }

    @PutMapping
    @PreAuthorize("hasRoleOrAdmin(@role.GLOBAL_FUN_UPDATE)")
    public Boolean edit(@Validated(UpdateGroup.class) @RequestBody GlobalFunctionRequest globalFunctionRequest) {
        return globalFunctionService.edit(globalFunctionRequest);
    }

    @DeleteMapping("/{ids}")
    @PreAuthorize("hasRoleOrAdmin(@role.GLOBAL_FUN_DELETE)")
    public Boolean delete(@PathVariable List<String> ids) {
        return globalFunctionService.delete(ids);
    }

    @GetMapping("/pull/{ids}")
    @PreAuthorize("hasRole(@role.GLOBAL_FUNCTION_PULL)")
    public List<GlobalFunctionResponse> pullFunction(@PathVariable("ids") List<String> ids) {
        return globalFunctionService.pullFunction(ids);
    }
}