package com.sms.satp.initialize;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sms.satp.entity.system.SystemVersionEntity;
import com.sms.satp.repository.SystemRoleRepository;
import com.sms.satp.repository.SystemVersionRepository;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Objects;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.boot.info.BuildProperties;
import org.springframework.context.ApplicationListener;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.io.ClassPathResource;

@Slf4j
public class RoleInitializer implements ApplicationListener<ApplicationStartedEvent> {

    private static final String SUFFIX = "-SystemRole.json";
    private static final String PREFIX = "db/";
    private static final Integer SUCCESS = 1;
    private static final Integer FAIL = 0;

    @Override
    public void onApplicationEvent(ApplicationStartedEvent event) {
        ConfigurableApplicationContext applicationContext = event.getApplicationContext();
        BuildProperties buildProperties = applicationContext.getBean(BuildProperties.class);
        SystemRoleRepository systemRoleRepository = applicationContext.getBean(SystemRoleRepository.class);
        SystemVersionRepository systemVersionRepository = applicationContext.getBean(SystemVersionRepository.class);
        ObjectMapper objectMapper = applicationContext.getBean(ObjectMapper.class);
        try {
            LocalDateTime buildTime = LocalDateTime.ofInstant(buildProperties.getTime(), ZoneId.systemDefault());
            String version = buildProperties.getVersion();
            String name = buildProperties.getName();
            String group = buildProperties.getGroup();
            log.info("version:{},name:{},group:{},buildTime:{}", version, name, group, buildTime);
            Objects.requireNonNull(name);
            Objects.requireNonNull(version);
            SystemVersionEntity systemVersion = systemVersionRepository.findByName(name);
            if (checkInitialized(systemVersion, version)) {
                String path = PREFIX + version + SUFFIX;
                final ClassPathResource classPathResource = new ClassPathResource(path);
                systemVersion = Objects.requireNonNullElse(systemVersion, SystemVersionEntity.builder().build());
                systemVersion.setVersion(version);
                systemVersion.setBuildTime(buildTime);
                systemVersion.setName(name);
                if (!classPathResource.exists()) {
                    log.info("The file not exists. path:{}", path);
                    systemVersion.setInitialized(false);
                    systemVersion.setStatus(FAIL);
                    systemVersionRepository.save(systemVersion);
                    return;
                }
                RoleConvert roleConvert = objectMapper.readValue(classPathResource.getInputStream(), RoleConvert.class);
                systemRoleRepository.saveAll(roleConvert.getRoles());
                systemVersion.setInitialized(true);
                systemVersion.setStatus(SUCCESS);
                systemVersionRepository.save(systemVersion);
                log.info("Initialize role success");
            }
        } catch (Exception e) {
            log.error("Initialize role error.");
        }
    }

    private boolean checkInitialized(SystemVersionEntity systemVersion, String version) {
        return systemVersion == null || !version.equals(systemVersion.getVersion()) || !systemVersion.isInitialized()
            || FAIL.equals(systemVersion.getStatus());
    }
}
