package com.sms.courier.initialize.impl;

import static com.sms.courier.initialize.constant.Initializer.FAIL;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sms.courier.entity.system.SystemVersionEntity;
import com.sms.courier.initialize.DataInitializer;
import com.sms.courier.initialize.constant.Order;
import com.sms.courier.initialize.vo.Convert;
import com.sms.courier.repository.SystemRoleRepository;
import com.sms.courier.repository.SystemVersionRepository;
import java.io.File;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Objects;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.info.BuildProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.ResourceUtils;

@Slf4j
@Component
public class RoleInitializer implements DataInitializer {

    @Override
    public void init(ApplicationContext applicationContext) {
        BuildProperties buildProperties = applicationContext.getBean(BuildProperties.class);
        SystemRoleRepository systemRoleRepository = applicationContext.getBean(SystemRoleRepository.class);
        SystemVersionRepository systemVersionRepository = applicationContext.getBean(SystemVersionRepository.class);
        MongoTemplate mongoTemplate = applicationContext.getBean(MongoTemplate.class);
        ObjectMapper objectMapper = applicationContext.getBean(ObjectMapper.class);
        try {
            LocalDateTime buildTime = LocalDateTime.ofInstant(buildProperties.getTime(), ZoneId.systemDefault());
            String version = buildProperties.getVersion();
            String name = buildProperties.getName();
            String group = buildProperties.getGroup();
            log.info("version:{},name:{},group:{},buildTime:{}", version, name, group, buildTime);
            Objects.requireNonNull(name);
            Objects.requireNonNull(version);
            SystemVersionEntity systemVersion = systemVersionRepository.findByVersion(version);

            File dbDirectory = ResourceUtils.getFile("classpath:db");
            File[] dbFile = dbDirectory
                .listFiles((file, filename) -> Objects.nonNull(file) && filename.startsWith(version));
            if (dbFile == null || dbFile.length == 0) {
                log.info("No initialization files.");
                return;
            }
            for (int i = 0; i < dbFile.length; i++) {
                File file = dbFile[i];
                String filename = file.getName();
                String className = filename.substring(filename.indexOf("-") + 1, filename.lastIndexOf("."));
                Convert<?> convert = (Convert<?>) objectMapper.readValue(file, Class.forName(className));
                System.out.println(convert.getData());
            }

//            mongoTemplate.insert(o.getData());
            /*if (checkInitialized(systemVersion, version)) {
                String path = String.join("", PREFIX, version, SUFFIX);
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
            }*/
        } catch (Exception e) {
            log.error("Initialize role error.", e);
        }
    }

    private boolean checkInitialized(SystemVersionEntity systemVersion, String version) {
        return null == systemVersion || !version.equals(systemVersion.getVersion()) || !systemVersion.isInitialized()
            || FAIL == systemVersion.getStatus();
    }

    @Override
    public int getOrder() {
        return Order.ROLE;
    }
}
