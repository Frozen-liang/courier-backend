package com.sms.satp.controller;

import com.sms.satp.common.constant.Constants;
import com.sms.satp.common.response.Response;
import com.sms.satp.entity.dto.ApiInterfaceDto;
import com.sms.satp.entity.dto.PageDto;
import com.sms.satp.service.ApiInterfaceService;
import java.util.List;
import javax.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(Constants.INTERFACE_PATH)
public class ApiInterfaceController {

    private final ApiInterfaceService apiInterfaceService;

    public ApiInterfaceController(ApiInterfaceService apiInterfaceService) {
        this.apiInterfaceService = apiInterfaceService;
    }

    @GetMapping("/page/{projectId}")
    public Response<Page<ApiInterfaceDto>> page(PageDto pageDto, @PathVariable String projectId) {
        return Response.ok(apiInterfaceService.page(pageDto, projectId));
    }

    @GetMapping("/{id}")
    public Response<ApiInterfaceDto> getInfoById(@PathVariable String id) {
        return Response.ok(apiInterfaceService.getApiInterfaceById(id));
    }

    @PostMapping()
    public Response add(@Valid @RequestBody ApiInterfaceDto apiInterfaceDto) {
        apiInterfaceService.add(apiInterfaceDto);
        return Response.ok().build();
    }

    @DeleteMapping("/{ids}")
    public Response delete(@PathVariable String[] ids) {
        for (String id :ids) {
            apiInterfaceService.deleteById(id);
        }
        return Response.ok().build();
    }

}