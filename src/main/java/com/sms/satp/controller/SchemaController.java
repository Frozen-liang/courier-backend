package com.sms.satp.controller;

import com.sms.satp.common.constant.Constants;
import com.sms.satp.common.response.Response;
import com.sms.satp.entity.dto.PageDto;
import com.sms.satp.entity.dto.SchemaDto;
import com.sms.satp.service.SchemaService;
import com.sms.satp.utils.PageDtoConverter;
import javax.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(Constants.SCHEMA_PATH)
public class SchemaController {

    private final SchemaService schemaService;

    public SchemaController(SchemaService schemaService) {
        this.schemaService = schemaService;
    }

    @GetMapping("/page/{projectId}")
    public Response<Page<SchemaDto>> page(PageDto pageDto, @PathVariable String projectId) {
        PageDtoConverter.frontMapping(pageDto);
        return Response.ok(schemaService.page(pageDto, projectId));
    }

    @GetMapping("/{id}")
    public Response<SchemaDto> getById(@PathVariable String id) {
        return Response.ok(schemaService.findById(id));
    }

    @PostMapping()
    public Response add(@Valid @RequestBody SchemaDto wikiDto) {
        schemaService.add(wikiDto);
        return Response.ok().build();
    }

    @PutMapping()
    public Response edit(@Valid @RequestBody SchemaDto wikiDto) {
        schemaService.edit(wikiDto);
        return Response.ok().build();
    }

    @DeleteMapping("/{ids}")
    public Response delete(@PathVariable String[] ids) {
        for (String id : ids) {
            schemaService.deleteById(id);
        }
        return Response.ok().build();
    }

}