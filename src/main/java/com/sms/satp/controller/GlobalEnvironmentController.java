package com.sms.satp.controller;

import com.sms.satp.common.constant.Constants;
import com.sms.satp.common.validate.InsertGroup;
import com.sms.satp.common.validate.UpdateGroup;
import com.sms.satp.dto.request.GlobalEnvironmentRequest;
import com.sms.satp.dto.response.GlobalEnvironmentResponse;
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
    public GlobalEnvironmentResponse getById(@PathVariable("id") String id) {
        return
            globalEnvironmentService.findById(id);
    }

    @PostMapping
    public Boolean add(@Validated(InsertGroup.class) @RequestBody GlobalEnvironmentRequest globalEnvironmentRequest) {
        return globalEnvironmentService.add(globalEnvironmentRequest);
    }

    @PutMapping
    public Boolean edit(@Validated(UpdateGroup.class) @RequestBody GlobalEnvironmentRequest globalEnvironmentRequest) {
        return globalEnvironmentService.edit(globalEnvironmentRequest);
    }

    @GetMapping("/list")
    public List<GlobalEnvironmentResponse> list() {
        return globalEnvironmentService.list();
    }

    @DeleteMapping("/{ids}")
    public Boolean delete(@PathVariable("ids") List<String> ids) {
        return globalEnvironmentService.delete(ids);
    }

}
