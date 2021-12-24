package com.sms.courier.controller;

import com.sms.courier.common.constant.Constants;
import com.sms.courier.common.validate.InsertGroup;
import com.sms.courier.common.validate.UpdateGroup;
import com.sms.courier.dto.request.GlobalFunctionRequest;
import com.sms.courier.dto.response.GlobalFunctionResponse;
import com.sms.courier.dto.response.LoadFunctionResponse;
import com.sms.courier.service.GlobalFunctionService;
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
import org.springframework.web.bind.annotation.RequestParam;
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

    @GetMapping("/load")
    public List<LoadFunctionResponse> loadFunction(@RequestParam String workspaceId) {
        return globalFunctionService.loadFunction(workspaceId);
    }
}