package com.sms.courier.controller;

import com.sms.courier.common.constant.Constants;
import com.sms.courier.common.validate.InsertGroup;
import com.sms.courier.common.validate.UpdateGroup;
import com.sms.courier.dto.request.GeneratorTemplateRequest;
import com.sms.courier.dto.response.GeneratorTemplateResponse;
import com.sms.courier.dto.response.GeneratorTemplateTypeResponse;
import com.sms.courier.service.GeneratorTemplateService;
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
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(Constants.GENERATOR_TEMPLATE_PATH)
public class GeneratorTemplateController {

    private final GeneratorTemplateService generatorTemplateService;

    public GeneratorTemplateController(GeneratorTemplateService generatorTemplateService) {
        this.generatorTemplateService = generatorTemplateService;
    }

    @GetMapping("/list/{projectId}")
    @PreAuthorize("hasRoleOrAdmin(@role.GENERATOR_TEMPLATE_QUERY_ALL)")
    public List<GeneratorTemplateResponse> list(@PathVariable String projectId) {
        return generatorTemplateService.list(projectId);
    }

    @PostMapping
    @PreAuthorize("hasRoleOrAdmin(@role.GENERATOR_TEMPLATE_CRE_UPD_DEL)")
    public Boolean add(@Validated(InsertGroup.class) @RequestBody GeneratorTemplateRequest generatorTemplateRequest) {
        return generatorTemplateService.add(generatorTemplateRequest);
    }

    @DeleteMapping("/{ids}")
    @PreAuthorize("hasRoleOrAdmin(@role.GENERATOR_TEMPLATE_CRE_UPD_DEL)")
    public Boolean deleteByIds(@PathVariable List<String> ids) {
        return generatorTemplateService.deleteByIds(ids);
    }

    @PutMapping
    @PreAuthorize("hasRoleOrAdmin(@role.GENERATOR_TEMPLATE_CRE_UPD_DEL)")
    public Boolean edit(
        @Validated(UpdateGroup.class) @RequestBody GeneratorTemplateRequest updateGeneratorTemplateRequest) {
        return generatorTemplateService.edit(updateGeneratorTemplateRequest);
    }

    @GetMapping("/type")
    public List<GeneratorTemplateTypeResponse> getAllType() {
        return generatorTemplateService.getAllType();
    }
}
