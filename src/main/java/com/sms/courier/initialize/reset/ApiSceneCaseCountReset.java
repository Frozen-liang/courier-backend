package com.sms.courier.initialize.reset;

import com.sms.courier.common.field.ApiField;
import com.sms.courier.entity.project.ProjectEntity;
import com.sms.courier.entity.workspace.WorkspaceEntity;
import com.sms.courier.initialize.ApiCaseCount;
import com.sms.courier.initialize.DataInitializer;
import com.sms.courier.repository.ProjectRepository;
import com.sms.courier.repository.WorkspaceRepository;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class ApiSceneCaseCountReset implements DataInitializer {

    private static final String RESET_TYPE = "API_SCENE_COUNT";

    @Override
    public void init(ApplicationContext applicationContext) {
        ResetSettingRepository resetSettingRepository = applicationContext.getBean(ResetSettingRepository.class);
        ResetSetting resetSetting = resetSettingRepository.findByResetType(RESET_TYPE);
        try {
            if (Objects.isNull(resetSetting) || !resetSetting.isReset()) {
                resetSetting = Objects.requireNonNullElse(resetSetting, new ResetSetting());
                WorkspaceRepository workspaceRepository = applicationContext.getBean(WorkspaceRepository.class);
                ProjectRepository projectRepository = applicationContext.getBean(ProjectRepository.class);
                ApiResetRepository apiResetService = applicationContext.getBean(ApiResetRepository.class);

                List<WorkspaceEntity> workspaceEntityList = workspaceRepository.findAll();
                for (WorkspaceEntity workspaceEntity : workspaceEntityList) {
                    List<ProjectEntity> projectEntityList = projectRepository
                        .findAllByWorkspaceId(workspaceEntity.getId());

                    List<String> projectIds = projectEntityList.stream().map(ProjectEntity::getId)
                        .collect(Collectors.toList());

                    if (CollectionUtils.isNotEmpty(projectIds)) {
                        apiResetService.resetSceneCaseCount(projectIds);
                        for (ProjectEntity projectEntity : projectEntityList) {
                            List<ApiCaseCount> caseCountList = apiResetService
                                .findApiCountByProjectIdAndGroupByApiId(projectEntity.getId(), Boolean.FALSE,
                                    Boolean.TRUE);

                            apiResetService
                                .updateCountFieldByIds(caseCountList, ApiField.SCENE_CASE_COUNT, Boolean.TRUE);

                            List<String> caseTemplateIds = apiResetService
                                .findCaseTemplateIdByProjectIdAndRemoved(projectEntity.getId(), Boolean.FALSE);

                            for (String caseTemplateId : caseTemplateIds) {
                                List<ApiCaseCount> templateCaseCount = apiResetService
                                    .findApiCountByProjectIdAndCaseTemplateIdAndGroupByApiId(caseTemplateId,
                                        projectEntity.getId(), Boolean.TRUE);

                                apiResetService.updateCountFieldByIds(templateCaseCount, ApiField.SCENE_CASE_COUNT,
                                    Boolean.TRUE);

                                //otherProjectSceneCaseCount
                                List<ApiCaseCount> otherTemplateCaseCount = apiResetService
                                    .findApiCountByProjectIdAndCaseTemplateIdAndGroupByApiId(caseTemplateId,
                                        projectEntity.getId(), Boolean.FALSE);

                                apiResetService.updateCountFieldByIds(otherTemplateCaseCount,
                                    ApiField.OTHER_PROJECT_SCENE_CASE_COUNT, Boolean.TRUE);
                            }

                            //otherProjectSceneCaseCount

                            List<ApiCaseCount> otherSceneCaseCount = apiResetService
                                .findApiCountByProjectIdAndGroupByApiId(projectEntity.getId(), Boolean.FALSE,
                                    Boolean.FALSE);

                            apiResetService
                                .updateCountFieldByIds(otherSceneCaseCount, ApiField.OTHER_PROJECT_SCENE_CASE_COUNT,
                                    Boolean.TRUE);

                        }
                    }
                }
                resetSetting.setReset(true);
                resetSetting.setResetType(RESET_TYPE);
                resetSettingRepository.save(resetSetting);
            }
        } catch (Exception e) {
            resetSetting.setReset(false);
            resetSetting.setResetType(RESET_TYPE);
            resetSettingRepository.save(resetSetting);
            log.error("Initialize Api sceneCaseCount error!", e);
        }
    }

    @Override
    public int getOrder() {
        return 12;
    }

}
