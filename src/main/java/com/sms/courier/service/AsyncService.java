package com.sms.courier.service;

import com.sms.courier.entity.project.ImportSourceVo;

public interface AsyncService {

    void importApi(ImportSourceVo importSource);
}
