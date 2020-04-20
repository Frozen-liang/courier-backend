package com.sms.satp.controller;

import com.sms.satp.common.constant.Constants;
import com.sms.satp.common.response.Response;
import com.sms.satp.entity.InterfaceHistory;
import com.sms.satp.entity.InterfaceShowInHistory;
import com.sms.satp.service.InterfaceHistoryService;
import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(Constants.INTERFACE_HISTORY_PATH)
public class InterfaceHistoryController {

    private final InterfaceHistoryService interfaceHistoryService;

    public InterfaceHistoryController(InterfaceHistoryService interfaceHistoryService) {
        this.interfaceHistoryService = interfaceHistoryService;
    }

    @GetMapping("/{id}")
    public Response<InterfaceHistory> get(@PathVariable String id) {
        return Response.ok(interfaceHistoryService.getHistoryById(id));
    }

    @GetMapping("/list/{projectId}")
    public Response<List<InterfaceShowInHistory>> list(@RequestParam String path, @RequestParam String method,
        @PathVariable String projectId) {
        return Response.ok(interfaceHistoryService.getHistoryList(projectId, method, path));
    }

}