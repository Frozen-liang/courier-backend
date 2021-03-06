package com.sms.courier.initialize.impl;

import static com.sms.courier.initialize.constant.Initializer.FAIL;
import static com.sms.courier.initialize.constant.Initializer.PREFIX;
import static com.sms.courier.initialize.constant.Initializer.SUCCESS;
import static com.sms.courier.initialize.constant.Initializer.SUFFIX;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sms.courier.entity.system.SystemVersionEntity;
import com.sms.courier.initialize.DataInitializer;
import com.sms.courier.initialize.constant.Order;
import com.sms.courier.initialize.enums.JsonType;
import com.sms.courier.repository.SystemVersionRepository;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.Objects;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.info.BuildProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class DataInitializerImpl implements DataInitializer {

    @Override
    @SuppressFBWarnings("NP_NULL_ON_SOME_PATH_FROM_RETURN_VALUE")
    public void init(ApplicationContext applicationContext) {
        BuildProperties buildProperties = applicationContext.getBean(BuildProperties.class);
        SystemVersionRepository systemVersionRepository = applicationContext.getBean(SystemVersionRepository.class);
        ObjectMapper objectMapper = applicationContext.getBean(ObjectMapper.class);
        MongoTemplate mongoTemplate = applicationContext.getBean(MongoTemplate.class);
        try {
            LocalDateTime buildTime = LocalDateTime.ofInstant(buildProperties.getTime(), ZoneId.systemDefault());
            String version = buildProperties.getVersion();
            String name = buildProperties.getName();
            String group = buildProperties.getGroup();
            log.info("version:{},name:{},group:{},buildTime:{}", version, name, group, buildTime);
            Objects.requireNonNull(name);
            Objects.requireNonNull(version);
            SystemVersionEntity systemVersion = systemVersionRepository.findByVersion(version);
            if (checkInitialized(systemVersion, version)) {
                systemVersion = Objects.requireNonNullElse(systemVersion, SystemVersionEntity.builder().build());
                systemVersion.setVersion(version);
                systemVersion.setGroup(group);
                systemVersion.setBuildTime(buildTime);
                systemVersion.setName(name);
                String pattern = PREFIX + version + SUFFIX;
                Resource[] resources = new PathMatchingResourcePatternResolver().getResources(pattern);
                if (resources.length == 0) {
                    log.info("The files not exists. pattern:{}", pattern);
                    systemVersion.setInitialized(false);
                    systemVersion.setStatus(FAIL);
                    systemVersionRepository.save(systemVersion);
                    return;
                }
                for (Resource resource : resources) {
                    String filename = resource.getFilename();
                    InputStream inputStream = resource.getInputStream();
                    String entityName = filename.substring(filename.indexOf("-") + 1, filename.lastIndexOf("."));
                    JsonType jsonType = JsonType.valueOf(entityName);
                    List<?> list = (List<?>) objectMapper.readValue(inputStream, jsonType.getTypeReference());
                    list.forEach(mongoTemplate::save);
                    log.info("Initialize {} success.", filename);
                }
                systemVersion.setInitialized(true);
                systemVersion.setStatus(SUCCESS);
                systemVersionRepository.save(systemVersion);
            }
        } catch (Exception e) {
            log.error("Initialize data error.", e);
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
