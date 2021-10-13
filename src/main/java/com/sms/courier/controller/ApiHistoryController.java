package com.sms.courier.controller;

import static com.sms.courier.common.constant.Constants.API_HISTORY_PATH;

import com.sms.courier.dto.response.ApiHistoryDetailResponse;
import com.sms.courier.dto.response.ApiHistoryListResponse;
import com.sms.courier.service.ApiHistoryService;
import java.util.List;
import org.bson.types.ObjectId;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(API_HISTORY_PATH)
public class ApiHistoryController {

    private final ApiHistoryService apiHistoryService;

    public ApiHistoryController(ApiHistoryService apiHistoryService) {
        this.apiHistoryService = apiHistoryService;
    }

    @GetMapping("/findByApiId")
    public List<ApiHistoryListResponse> findByApiId(@RequestParam ObjectId apiId) {
        return apiHistoryService.findByApiId(apiId);
    }

    @GetMapping("/findById")
    public ApiHistoryDetailResponse findById(String id) {
        return apiHistoryService.findById(id);
    }

}
