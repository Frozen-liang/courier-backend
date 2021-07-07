package com.sms.satp.service;

import com.sms.satp.entity.project.ImportSourceVo;

public interface AsyncService {

    void importApi(ImportSourceVo importSource);
}
