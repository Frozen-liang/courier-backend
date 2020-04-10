package com.sms.satp.controller;

import com.mongodb.client.result.UpdateResult;
import com.sms.satp.common.constant.Constants;
import com.sms.satp.common.response.Response;
import com.sms.satp.entity.dto.ApiInterfaceDto;
import com.sms.satp.entity.dto.InterfaceGroupDto;
import com.sms.satp.entity.dto.PageDto;
import com.sms.satp.service.ApiInterfaceService;
import com.sms.satp.service.InterfaceGroupService;
import java.util.Arrays;
import java.util.List;
import javax.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(Constants.INTERFACE_PATH)
public class ApiInterfaceController {

    private final ApiInterfaceService apiInterfaceService;
    private final InterfaceGroupService interfaceGroupService;

    public ApiInterfaceController(ApiInterfaceService apiInterfaceService,
        InterfaceGroupService interfaceGroupService) {
        this.apiInterfaceService = apiInterfaceService;
        this.interfaceGroupService = interfaceGroupService;
    }

    @GetMapping("/page/{projectId}/{groupId}")
    public Response<Page<ApiInterfaceDto>> page(PageDto pageDto, @PathVariable("projectId") String projectId,
        @PathVariable("groupId") String groupId, String tag) {
        return Response.ok(apiInterfaceService.page(pageDto, projectId, groupId, tag));
    }

    @GetMapping("/{id}")
    public Response<ApiInterfaceDto> getInfoById(@PathVariable String id) {
        return Response.ok(apiInterfaceService.findById(id));
    }

    @PostMapping()
    public Response add(@Valid @RequestBody ApiInterfaceDto apiInterfaceDto) {
        apiInterfaceService.add(apiInterfaceDto);
        return Response.ok().build();
    }

    @PutMapping()
    public Response edit(@Valid @RequestBody ApiInterfaceDto apiInterfaceDto) {
        apiInterfaceService.edit(apiInterfaceDto);
        return Response.ok().build();
    }

    @PatchMapping("{ids}")
    public Response<UpdateResult> updateGroupIdById(@PathVariable String[] ids, @RequestParam String groupId) {
        return Response.ok(apiInterfaceService.updateGroupById(Arrays.asList(ids), groupId));

    }

    @DeleteMapping("/{ids}")
    public Response delete(@PathVariable String[] ids) {
        for (String id :ids) {
            apiInterfaceService.deleteById(id);
        }
        return Response.ok().build();
    }

    @GetMapping("/group/list/{projectId}")
    public Response<List<InterfaceGroupDto>> getGroupList(@PathVariable String projectId) {
        return Response.ok(interfaceGroupService.getGroupList(projectId));
    }

    @PostMapping("/group")
    public Response addGroup(@Valid @RequestBody InterfaceGroupDto interfaceGroupDto) {
        interfaceGroupService.addGroup(interfaceGroupDto);
        return Response.ok().build();
    }

    @PutMapping("/group")
    public Response editGroup(@Valid @RequestBody InterfaceGroupDto interfaceGroupDto) {
        interfaceGroupService.editGroup(interfaceGroupDto);
        return Response.ok().build();
    }

    @DeleteMapping("/group/{ids}")
    public Response deleteGroups(@PathVariable String[] ids) {
        for (String id :ids) {
            interfaceGroupService.deleteGroup(id);
        }
        return Response.ok().build();
    }

}