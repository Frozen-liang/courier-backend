package com.sms.satp.common.aspect.log;

import static com.sms.satp.common.field.CommonFiled.ID;

import com.sms.satp.common.aspect.annotation.Enhance;
import com.sms.satp.common.aspect.annotation.LogRecord;
import com.sms.satp.common.enums.OperationModule;
import com.sms.satp.common.enums.OperationType;
import com.sms.satp.entity.log.LogEntity;
import com.sms.satp.service.LogService;
import com.sms.satp.utils.SpelUtils;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.expression.EvaluationContext;
import org.springframework.stereotype.Component;

@Component
@Aspect
@Slf4j
public class LogAspect {

    private final LogService logService;
    private final MongoTemplate mongoTemplate;

    public LogAspect(LogService logService, MongoTemplate mongoTemplate) {
        this.logService = logService;
        this.mongoTemplate = mongoTemplate;
    }

    @Pointcut(value = "@annotation(com.sms.satp.common.aspect.annotation.LogRecord)")
    public void pointCut() {
    }

    @Before("pointCut() && @annotation(logRecord)")
    public void before(JoinPoint jp, LogRecord logRecord) {
        MethodSignature signature = (MethodSignature) jp.getSignature();
        Method method = signature.getMethod();
        Object[] args = jp.getArgs();
        OperationType operationType = logRecord.operationType();
        OperationModule operationModule = logRecord.operationModule();
        Enhance enhance = logRecord.enhance();
        EvaluationContext context = SpelUtils.getContext(args, method);
        enhance(enhance, context, operationModule.getCollectionName(), method);
        String operationDesc = SpelUtils.getValue(context, logRecord.template(), String.class);
        String projectId = SpelUtils.getProjectId(context, logRecord, method, args);
        if (StringUtils.isEmpty(operationDesc)) {
            log.warn("The operationDesc is empty,please check the method: {} template:{}",
                method, logRecord.template());
        }
        LogEntity logEntity = LogEntity.builder().operationType(operationType).operationModule(operationModule)
            .operationDesc(operationDesc).projectId(projectId).build();
        logService.add(logEntity);
    }

    private void enhance(Enhance enhance, EvaluationContext context, String collectionName, Method method) {
        if (enhance.enable()) {
            Object value = SpelUtils.getValue(context, enhance.primaryKey());
            if (Objects.nonNull(value)) {
                Object queryByIdResult;
                if (value instanceof Collection) {
                    Query query = Query.query(Criteria.where(ID.getFiled()).in((Collection) value));
                    queryByIdResult = mongoTemplate.find(query, Object.class, collectionName);
                } else {
                    queryByIdResult = mongoTemplate.findById(value, Object.class, collectionName);
                }
                context.setVariable(enhance.queryResultKey(), queryByIdResult);
            } else {
                log.error("The method:{} parameterNames not exist the primaryKey:{}.",
                    method, enhance.primaryKey());
            }
        }
    }

}
