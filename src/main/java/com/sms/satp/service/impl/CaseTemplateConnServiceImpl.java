package com.sms.satp.service.impl;

import static com.sms.satp.common.exception.ErrorCode.ADD_CASE_TEMPLATE_CONN_ERROR;
import static com.sms.satp.common.exception.ErrorCode.DELETE_CASE_TEMPLATE_CONN_ERROR;
import static com.sms.satp.common.exception.ErrorCode.EDIT_CASE_TEMPLATE_CONN_ERROR;
import static com.sms.satp.common.exception.ErrorCode.EDIT_LIST_CASE_TEMPLATE_CONN_ERROR;
import static com.sms.satp.common.exception.ErrorCode.GET_CASE_TEMPLATE_CONN_LIST_ERROR;

import com.google.common.collect.Lists;
import com.sms.satp.common.exception.ApiTestPlatformException;
import com.sms.satp.dto.request.AddCaseTemplateConnRequest;
import com.sms.satp.entity.scenetest.CaseTemplateApi;
import com.sms.satp.entity.scenetest.CaseTemplateApiConn;
import com.sms.satp.entity.scenetest.CaseTemplateConn;
import com.sms.satp.entity.scenetest.SceneCaseApi;
import com.sms.satp.mapper.CaseTemplateConnMapper;
import com.sms.satp.repository.CaseTemplateConnRepository;
import com.sms.satp.repository.CustomizedSceneCaseApiRepository;
import com.sms.satp.service.CaseTemplateApiService;
import com.sms.satp.service.CaseTemplateConnService;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class CaseTemplateConnServiceImpl implements CaseTemplateConnService {

    private final CaseTemplateConnRepository caseTemplateConnRepository;
    private final CaseTemplateConnMapper caseTemplateConnMapper;
    private final CustomizedSceneCaseApiRepository customizedSceneCaseApiRepository;
    private final CaseTemplateApiService caseTemplateApiService;

    public CaseTemplateConnServiceImpl(CaseTemplateConnRepository caseTemplateConnRepository,
        CaseTemplateConnMapper caseTemplateConnMapper,
        CustomizedSceneCaseApiRepository customizedSceneCaseApiRepository,
        CaseTemplateApiService caseTemplateApiService) {
        this.caseTemplateConnRepository = caseTemplateConnRepository;
        this.caseTemplateConnMapper = caseTemplateConnMapper;
        this.customizedSceneCaseApiRepository = customizedSceneCaseApiRepository;
        this.caseTemplateApiService = caseTemplateApiService;
    }

    @Override
    public Boolean deleteById(String id) {
        log.info("CaseTemplateConnService-deleteById()-params: [id]={}", id);
        try {
            Optional<CaseTemplateConn> optional = caseTemplateConnRepository.findById(id);
            optional.ifPresent(conn -> caseTemplateConnRepository.deleteById(conn.getId()));
            return Boolean.TRUE;
        } catch (Exception e) {
            log.error("Failed to deleteById the CaseTemplateConn!", e);
            throw new ApiTestPlatformException(DELETE_CASE_TEMPLATE_CONN_ERROR);
        }
    }

    @Override
    public Boolean deleteByIds(List<String> ids) {
        log.info("CaseTemplateConnService-deleteByIds()-params: [ids]={}", ids);
        try {
            caseTemplateConnRepository.deleteAllByIdIsIn(ids);
            return Boolean.TRUE;
        } catch (Exception e) {
            log.error("Failed to deleteByIds the CaseTemplateConn!", e);
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

    @Override
    public List<CaseTemplateConn> listBySceneCaseId(String sceneCaseId, boolean remove) {
        try {
            CaseTemplateConn conn = CaseTemplateConn.builder().sceneCaseId(sceneCaseId).removed(remove).build();
            Example<CaseTemplateConn> example = Example.of(conn);
            return caseTemplateConnRepository.findAll(example);
        } catch (Exception e) {
            log.error("Failed to listBySceneCaseId the CaseTemplateConn!", e);
            throw new ApiTestPlatformException(GET_CASE_TEMPLATE_CONN_LIST_ERROR);
        }
    }

    @Override
    public List<CaseTemplateConn> listByCassTemplateId(String caseTemplateId) {
        try {
            Example<CaseTemplateConn> example = Example
                .of(CaseTemplateConn.builder().caseTemplateId(caseTemplateId).build());
            return caseTemplateConnRepository.findAll(example);
        } catch (Exception e) {
            log.error("Failed to listByCassTemplateId the CaseTemplateConn!", e);
            throw new ApiTestPlatformException(GET_CASE_TEMPLATE_CONN_LIST_ERROR);
        }
    }

    @Override
    public Boolean edit(CaseTemplateConn caseTemplateConn) {
        log.info("CaseTemplateConnService-edit()-params: [CaseTemplateConn]={}", caseTemplateConn.toString());
        try {
            caseTemplateConnRepository.save(caseTemplateConn);
            return Boolean.TRUE;
        } catch (Exception e) {
            log.error("Failed to edit the CaseTemplateConn!", e);
            throw new ApiTestPlatformException(EDIT_CASE_TEMPLATE_CONN_ERROR);
        }
    }

    @Override
    public Boolean editList(List<CaseTemplateConn> caseTemplateConn) {
        log.info("CaseTemplateConnService-editList()-params: [CaseTemplateConn]={}", caseTemplateConn.toString());
        try {
            caseTemplateConnRepository.saveAll(caseTemplateConn);
            return Boolean.TRUE;
        } catch (Exception e) {
            log.error("Failed to editList the CaseTemplateConn!", e);
            throw new ApiTestPlatformException(EDIT_LIST_CASE_TEMPLATE_CONN_ERROR);
        }
    }

    @Override
    public Boolean add(AddCaseTemplateConnRequest addCaseTemplateConnRequest) {
        try {
            CaseTemplateConn caseTemplateConn = caseTemplateConnMapper.toCaseTemplateConn(addCaseTemplateConnRequest);
            caseTemplateConnRepository.insert(caseTemplateConn);
            return Boolean.TRUE;
        } catch (Exception e) {
            log.error("Failed to add the CaseTemplateConn!", e);
            throw new ApiTestPlatformException(ADD_CASE_TEMPLATE_CONN_ERROR);
        }
    }

    @Override
    public CaseTemplateConn addByIds(String caseTemplateId, String sceneCaseId) {
        try {
            List<CaseTemplateApi> caseTemplateApiList = caseTemplateApiService.getApiByCaseTemplateId(caseTemplateId,
                false);
            SceneCaseApi sceneCaseApi = customizedSceneCaseApiRepository.findMaxOrderBySceneCaseId(sceneCaseId);
            List<CaseTemplateApiConn> apiConnList = Lists.newArrayList();
            int index = Objects.isNull(sceneCaseApi) ? 1 : sceneCaseApi.getOrder() + 1;
            for (CaseTemplateApi api : caseTemplateApiList) {
                apiConnList.add(CaseTemplateApiConn.builder().caseTemplateApiId(api.getId()).order(index)
                    .isExecute(Boolean.TRUE).build());
                index++;
            }
            CaseTemplateConn conn =
                CaseTemplateConn.builder().sceneCaseId(sceneCaseId).caseTemplateId(caseTemplateId)
                    .caseTemplateApiConnList(apiConnList).build();
            caseTemplateConnRepository.insert(conn);
            return conn;
        } catch (Exception e) {
            log.error("Failed to addByIds the CaseTemplateConn!", e);
            throw new ApiTestPlatformException(ADD_CASE_TEMPLATE_CONN_ERROR);
        }
    }

}
