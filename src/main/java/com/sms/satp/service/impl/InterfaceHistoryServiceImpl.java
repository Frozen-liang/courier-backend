package com.sms.satp.service.impl;

import static com.sms.satp.common.ErrorCode.GET_INTERFACE_HISTORY_BY_ID_ERROR;
import static com.sms.satp.common.ErrorCode.GET_INTERFACE_HISTORY_LIST_ERROR;
import static com.sms.satp.common.ErrorCode.SAVE_INTERFACE_HISTORY_ERROR;

import com.sms.satp.common.ApiTestPlatformException;
import com.sms.satp.common.enums.RequestMethod;
import com.sms.satp.entity.ApiInterface;
import com.sms.satp.entity.InterfaceHistory;
import com.sms.satp.entity.InterfaceShowInHistory;
import com.sms.satp.mapper.InterfaceHistoryMapper;
import com.sms.satp.repository.InterfaceHistoryRepository;
import com.sms.satp.service.InterfaceHistoryService;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class InterfaceHistoryServiceImpl implements InterfaceHistoryService {

    private final InterfaceHistoryRepository interfaceHistoryRepository;
    private final InterfaceHistoryMapper interfaceHistoryMapper;

    public InterfaceHistoryServiceImpl(InterfaceHistoryRepository interfaceHistoryRepository,
        InterfaceHistoryMapper interfaceHistoryMapper) {
        this.interfaceHistoryRepository = interfaceHistoryRepository;
        this.interfaceHistoryMapper = interfaceHistoryMapper;
    }

    @Override
    public void saveAsHistory(ApiInterface apiInterface) {
        log.info(
            "InterfaceHistoryService-saveAsHistory()-params: [ApiInterface]={}", apiInterface);
        try {
            InterfaceHistory interfaceHistory = interfaceHistoryMapper.convertToHistory(apiInterface);
            interfaceHistoryRepository.save(interfaceHistory);
        } catch (Exception e) {
            log.error("Failed to save the Interface history!", e);
            throw new ApiTestPlatformException(SAVE_INTERFACE_HISTORY_ERROR);
        }
    }

    @Override
    public InterfaceHistory getHistoryById(String id) {
        try {
            Optional<InterfaceHistory> interfaceHistoryOptional = interfaceHistoryRepository.findById(id);
            return interfaceHistoryOptional.orElse(null);
        } catch (Exception e) {
            log.error("Failed to get the Interface history by id!", e);
            throw new ApiTestPlatformException(GET_INTERFACE_HISTORY_BY_ID_ERROR);
        }
    }

    @Override
    public List<InterfaceShowInHistory> getHistoryList(String projectId, String method, String path) {
        try {
            RequestMethod requestMethod = RequestMethod.resolve(method.toUpperCase(Locale.US));
            return interfaceHistoryRepository.findByProjectIdAndMethodAndPath(projectId, requestMethod, path);
        } catch (Exception e) {
            log.error("Failed to get the Interface history list!", e);
            throw new ApiTestPlatformException(GET_INTERFACE_HISTORY_LIST_ERROR);
        }
    }
}