package com.sms.courier.service.impl;

import com.sms.courier.dto.request.UserEnvConnRequest;
import com.sms.courier.dto.response.UserEnvConnResponse;
import com.sms.courier.entity.env.UserEnvConnEntity;
import com.sms.courier.mapper.UserEnvConnMapper;
import com.sms.courier.repository.UserEnvRepository;
import com.sms.courier.service.UserEnvConnService;
import com.sms.courier.utils.SecurityUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class UserEnvConnServiceImpl implements UserEnvConnService {

    private final UserEnvRepository userEnvRepository;
    private final UserEnvConnMapper userEnvConnMapper;

    public UserEnvConnServiceImpl(UserEnvRepository userEnvRepository,
        UserEnvConnMapper userEnvConnMapper) {
        this.userEnvRepository = userEnvRepository;
        this.userEnvConnMapper = userEnvConnMapper;
    }

    @Override
    public UserEnvConnResponse userEnv(String projectId) {
        return userEnvRepository.findByProjectIdAndCreateUserId(projectId, SecurityUtil.getCurrUserId()).orElse(null);
    }

    @Override
    public UserEnvConnResponse userEnvConn(UserEnvConnRequest request) {
        UserEnvConnEntity userEnvConnEntity = userEnvRepository.save(userEnvConnMapper.toEntity(request));
        return userEnvConnMapper.toResponse(userEnvConnEntity);
    }
}
