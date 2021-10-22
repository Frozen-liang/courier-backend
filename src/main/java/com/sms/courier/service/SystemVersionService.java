package com.sms.courier.service;

import com.sms.courier.dto.response.SystemVersionResponse;
import java.util.List;

public interface SystemVersionService {

    List<SystemVersionResponse> findAll();
}
