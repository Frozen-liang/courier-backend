package com.sms.courier.controller;

import static com.sms.courier.common.constant.Constants.CLIENT_PORT_STATISTIC;

import com.sms.courier.dto.response.CountStatisticsResponse;
import com.sms.courier.service.ClientPortStatisticService;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(CLIENT_PORT_STATISTIC)
public class ClientPortStatisticController {

    private final ClientPortStatisticService clientPortStatisticService;

    public ClientPortStatisticController(ClientPortStatisticService clientPortStatisticService) {
        this.clientPortStatisticService = clientPortStatisticService;
    }

    @PostMapping
    public Boolean add(HttpServletRequest request) {
        String ip = request.getRemoteAddr();
        String host = request.getRemoteHost();
        return clientPortStatisticService.add(ip, host);
    }

    @GetMapping("/count/{day}")
    public List<CountStatisticsResponse> groupDayCount(@PathVariable Integer day) {
        return clientPortStatisticService.groupDayCount(day);
    }
}
