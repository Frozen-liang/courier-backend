package com.sms.courier.service;

import com.sms.courier.dto.request.CodeGenRequest;
import java.io.OutputStream;

public interface GeneratorService {

    void generator(OutputStream outputStream, CodeGenRequest request);
}
