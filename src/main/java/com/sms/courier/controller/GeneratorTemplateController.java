package com.sms.courier.controller;

import com.sms.courier.common.constant.Constants;
import com.sms.courier.dto.request.AddGeneratorTemplateRequest;
import com.sms.courier.dto.request.UpdateGeneratorTemplateRequest;
import com.sms.courier.entity.generator.GeneratorTemplateEntity;
import com.sms.courier.service.GeneratorTemplateService;
import java.util.List;
import javax.validation.Valid;
import org.bson.types.ObjectId;
import org.springframework.security.access.prepost.PreAuthorize;
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

    @GetMapping("/{id}")
    @PreAuthorize("hasRoleOrAdmin(@role.GENERATOR_TEMPLATE_QUERY_ALL)")
    public GeneratorTemplateEntity findById(@PathVariable String id) {
        return generatorTemplateService.findById(id);
    }

    @PostMapping
    @PreAuthorize("hasRoleOrAdmin(@role.GENERATOR_TEMPLATE_CRE_UPD_DEL)")
    public Boolean add(@RequestBody AddGeneratorTemplateRequest addGeneratorTemplateRequest) {
        return generatorTemplateService.add(addGeneratorTemplateRequest);
    }

    @DeleteMapping("/{ids}")
    @PreAuthorize("hasRoleOrAdmin(@role.GENERATOR_TEMPLATE_CRE_UPD_DEL)")
    public Boolean deleteByIds(@PathVariable List<String> ids) {
        return generatorTemplateService.deleteByIds(ids);
    }

    @PutMapping
    @PreAuthorize("hasRoleOrAdmin(@role.GENERATOR_TEMPLATE_CRE_UPD_DEL)")
    private Boolean edit(@Valid @RequestBody UpdateGeneratorTemplateRequest updateGeneratorTemplateRequest) {
        return generatorTemplateService.edit(updateGeneratorTemplateRequest);
    }

    public static void main(String[] args) {
        System.out.println(new ObjectId());
    }
}
