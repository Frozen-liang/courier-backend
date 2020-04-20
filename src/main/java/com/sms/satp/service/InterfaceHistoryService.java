package com.sms.satp.service;

import com.sms.satp.entity.ApiInterface;
import com.sms.satp.entity.InterfaceHistory;
import com.sms.satp.entity.InterfaceShowInHistory;
import java.util.List;

public interface InterfaceHistoryService {

    void saveAsHistory(ApiInterface apiInterface);

    InterfaceHistory getHistoryById(String id);

    List<InterfaceShowInHistory> getHistoryList(String projectId, String method, String path);

}
