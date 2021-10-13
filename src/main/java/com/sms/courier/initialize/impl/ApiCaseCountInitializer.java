package com.sms.courier.initialize.impl;

import com.sms.courier.common.field.ApiField;
import com.sms.courier.entity.project.ProjectEntity;
import com.sms.courier.entity.workspace.WorkspaceEntity;
import com.sms.courier.initialize.ApiCaseCount;
import com.sms.courier.initialize.ApiCountProperties;
import com.sms.courier.initialize.DataInitializer;
import com.sms.courier.initialize.constant.Order;
import com.sms.courier.repository.CustomizedApiRepository;
import com.sms.courier.repository.CustomizedApiTestCaseRepository;
import com.sms.courier.repository.ProjectRepository;
import com.sms.courier.repository.WorkspaceRepository;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.BooleanUtils;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class ApiCaseCountInitializer implements DataInitializer {

    private final ApiCountProperties apiCountProperties;

    public ApiCaseCountInitializer(ApiCountProperties apiCountProperties) {
        this.apiCountProperties = apiCountProperties;
    }

    @Override
    public void init(ApplicationContext applicationContext) {
        try {
            if (BooleanUtils.isTrue(apiCountProperties.getCaseCount())) {
                WorkspaceRepository workspaceRepository = applicationContext.getBean(WorkspaceRepository.class);
                ProjectRepository projectRepository = applicationContext.getBean(ProjectRepository.class);
                CustomizedApiTestCaseRepository apiTestCaseRepository =
                    applicationContext.getBean(CustomizedApiTestCaseRepository.class);
                CustomizedApiRepository apiRepository = applicationContext.getBean(CustomizedApiRepository.class);

                List<WorkspaceEntity> workspaceEntityList = workspaceRepository.findAll();
                for (WorkspaceEntity workspaceEntity : workspaceEntityList) {
                    List<ProjectEntity> projectEntityList = projectRepository
                        .findAllByWorkspaceId(workspaceEntity.getId());
                    for (ProjectEntity projectEntity : projectEntityList) {
                        List<ApiCaseCount> caseCountList =
                            apiTestCaseRepository.findProjectIdAndGroupByApiId(projectEntity.getId(), Boolean.FALSE);
                        apiRepository.updateCountFieldByIds(caseCountList, ApiField.CASE_COUNT);
                    }
                }
            }
        } catch (Exception e) {
            log.error("Initialize Api caseCount error!", e);
        }
    }

    @Override
    public int getOrder() {
        return Order.API_CASE_COUNT_REST;
    }

}
