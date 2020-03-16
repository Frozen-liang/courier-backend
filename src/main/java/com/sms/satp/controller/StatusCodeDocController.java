package com.sms.satp.controller;

import com.sms.satp.common.constant.Constants;
import com.sms.satp.common.response.Response;
import com.sms.satp.entity.dto.PageDto;
import com.sms.satp.entity.dto.StatusCodeDocDto;
import com.sms.satp.service.StatusCodeDocService;
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
@RequestMapping(Constants.STATUS_CODE_DOC_PATH)
public class StatusCodeDocController {

    private final StatusCodeDocService statusCodeDocService;

    public StatusCodeDocController(StatusCodeDocService statusCodeDocService) {
        this.statusCodeDocService = statusCodeDocService;
    }

    @GetMapping("/page/{projectId}")
    public Response<Page<StatusCodeDocDto>> page(PageDto pageDto, @PathVariable String projectId) {
        PageDtoConverter.frontMapping(pageDto);
        return Response.ok(statusCodeDocService.page(pageDto, projectId));
    }

    @GetMapping("/{id}")
    public Response<StatusCodeDocDto> getById(@PathVariable String id) {
        return Response.ok(statusCodeDocService.findById(id));
    }

    @PostMapping()
    public Response add(@Valid @RequestBody StatusCodeDocDto statusCodeDocDto) {
        statusCodeDocService.add(statusCodeDocDto);
        return Response.ok().build();
    }

    @PutMapping()
    public Response edit(@Valid @RequestBody StatusCodeDocDto statusCodeDocDto) {
        statusCodeDocService.edit(statusCodeDocDto);
        return Response.ok().build();
    }

    @DeleteMapping("/{ids}")
    public Response delete(@PathVariable String[] ids) {
        for (String id : ids) {
            statusCodeDocService.deleteById(id);
        }
        return Response.ok().build();
    }

}