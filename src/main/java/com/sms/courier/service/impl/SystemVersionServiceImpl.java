package com.sms.courier.service.impl;

import com.sms.courier.dto.response.SystemVersionResponse;
import com.sms.courier.repository.SystemVersionRepository;
import com.sms.courier.service.SystemVersionService;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class SystemVersionServiceImpl implements SystemVersionService {

    private final SystemVersionRepository systemVersionRepository;

    public SystemVersionServiceImpl(SystemVersionRepository systemVersionRepository) {
        this.systemVersionRepository = systemVersionRepository;
    }

    @Override
    public List<SystemVersionResponse> findAll() {
        return systemVersionRepository.findAllByOrderByVersionDesc();
    }
}
