package com.sms.courier.service;

import com.sms.courier.dto.request.CodeGenRequest;
import javax.servlet.http.HttpServletResponse;

public interface GeneratorService {

    void generator(HttpServletResponse response, CodeGenRequest request);
}
