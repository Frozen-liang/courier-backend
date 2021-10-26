package com.sms.courier.initialize.reset;

import com.sms.courier.entity.api.ApiEntity;
import com.sms.courier.entity.project.ProjectEntity;
import com.sms.courier.initialize.DataInitializer;
import com.sms.courier.repository.ApiRepository;
import com.sms.courier.repository.ProjectRepository;
import com.sms.courier.utils.MD5Util;
import java.util.List;
import java.util.Objects;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationContext;
import org.springframework.data.util.Streamable;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class ApiReset implements DataInitializer {

    private static final String RESET_TYPE = "API_MD5";

    @Override
    public void init(ApplicationContext applicationContext) {
        ResetSettingRepository resetSettingRepository = applicationContext.getBean(ResetSettingRepository.class);
        ResetSetting resetSetting = resetSettingRepository.findByResetType(RESET_TYPE);
        try {
            ApiRepository apiRepository = applicationContext.getBean(ApiRepository.class);
            ProjectRepository projectRepository = applicationContext.getBean(ProjectRepository.class);
            if (Objects.isNull(resetSetting) || !resetSetting.isReset()) {
                resetSetting = Objects.requireNonNullElse(resetSetting, new ResetSetting());
                List<ProjectEntity> projectEntities = projectRepository.findAll();
                for (ProjectEntity projectEntity : projectEntities) {
                    Streamable<ApiEntity> apiEntities = apiRepository
                        .findApiEntitiesByProjectIdAndSwaggerIdNotNull(projectEntity.getId());
                    apiEntities.forEach((e) -> e.setMd5(MD5Util.getMD5(e)));
                    log.info("Reset {} api", projectEntity.getName());
                    apiRepository.saveAll(apiEntities);
                }
                resetSetting.setReset(true);
                resetSetting.setResetType(RESET_TYPE);
                resetSettingRepository.save(resetSetting);
            }
        } catch (Exception e) {
            resetSetting.setReset(false);
            resetSetting.setResetType(RESET_TYPE);
            resetSettingRepository.save(resetSetting);
            log.error("Api reset error.", e);
        }
    }

    @Override
    public int getOrder() {
        return 10;
    }
}
