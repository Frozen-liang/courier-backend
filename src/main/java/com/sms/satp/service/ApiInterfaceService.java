package com.sms.satp.service;

import com.sms.satp.entity.ApiInterface;

import java.util.List;

public interface ApiInterfaceService {

    void save(String url, String documentType);

    ApiInterface getApiInterfaceById(String id);

    void deleteById(String id);

    List<ApiInterface> list();
}