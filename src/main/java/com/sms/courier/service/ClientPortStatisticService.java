package com.sms.courier.service;

import com.sms.courier.dto.response.CountStatisticsResponse;
import java.util.List;

public interface ClientPortStatisticService {

    Boolean add(String ip, String host);

    List<CountStatisticsResponse> groupDayCount(Integer day);
}
