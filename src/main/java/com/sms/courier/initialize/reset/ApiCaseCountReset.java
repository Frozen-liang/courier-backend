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
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class ApiCaseCountReset implements DataInitializer {

    private static final String RESET_TYPE = "API_CASE_COUNT";

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
                    for (ProjectEntity projectEntity : projectEntityList) {
                        List<ApiCaseCount> caseCountList =
                            apiResetService.findProjectIdAndGroupByApiId(projectEntity.getId(), Boolean.FALSE);
                        apiResetService.updateCountFieldByIds(caseCountList, ApiField.CASE_COUNT, Boolean.FALSE);
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
            log.error("Initialize Api caseCount error!", e);
        }
    }

    @Override
    public int getOrder() {
        return 11;
    }

}
