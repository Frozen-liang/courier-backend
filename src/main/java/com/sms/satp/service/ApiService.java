package com.sms.satp.service;

import com.sms.satp.dto.ApiImportRequest;
import org.springframework.stereotype.Service;

@Service
public interface ApiService {

    boolean importDocument(ApiImportRequest apiImportRequest);
}
