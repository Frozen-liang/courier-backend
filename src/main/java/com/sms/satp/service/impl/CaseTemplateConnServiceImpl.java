package com.sms.satp.service.impl;

import static com.sms.satp.common.ErrorCode.DELETE_CASE_TEMPLATE_CONN_ERROR;
import static com.sms.satp.common.ErrorCode.GET_CASE_TEMPLATE_CONN_LIST_ERROR;

import com.sms.satp.common.ApiTestPlatformException;
import com.sms.satp.entity.scenetest.CaseTemplateConn;
import com.sms.satp.repository.CaseTemplateConnRepository;
import com.sms.satp.service.CaseTemplateConnService;
import java.util.List;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class CaseTemplateConnServiceImpl implements CaseTemplateConnService {

    private final CaseTemplateConnRepository caseTemplateConnRepository;

    public CaseTemplateConnServiceImpl(CaseTemplateConnRepository caseTemplateConnRepository) {
        this.caseTemplateConnRepository = caseTemplateConnRepository;
    }

    @Override
    public void deleteById(String id) {
        log.info("CaseTemplateConnService-deleteById()-params: [id]={}", id);
        try {
            Optional<CaseTemplateConn> optional = caseTemplateConnRepository.findById(id);
            optional.ifPresent(conn -> caseTemplateConnRepository.deleteById(conn.getId()));
        } catch (Exception e) {
            log.error("Failed to deleteById the CaseTemplateConn!", e);
            throw new ApiTestPlatformException(DELETE_CASE_TEMPLATE_CONN_ERROR);
        }
    }

    @Override
    public List<CaseTemplateConn> listBySceneCaseId(String sceneCaseId) {
        try {
            Example<CaseTemplateConn> example = Example.of(CaseTemplateConn.builder().sceneCaseId(sceneCaseId).build());
            return caseTemplateConnRepository.findAll(example);
        } catch (Exception e) {
            log.error("Failed to listBySceneCaseId the CaseTemplateConn!", e);
            throw new ApiTestPlatformException(GET_CASE_TEMPLATE_CONN_LIST_ERROR);
        }
    }

}
