package com.sms.satp.common.aspect.log;

import static com.sms.satp.common.field.CommonFiled.ID;

import com.sms.satp.common.aspect.annotation.Enhance;
import com.sms.satp.common.aspect.annotation.LogRecord;
import com.sms.satp.common.enums.OperationModule;
import com.sms.satp.common.enums.OperationType;
import com.sms.satp.entity.log.LogEntity;
import com.sms.satp.service.FileService;
import com.sms.satp.service.LogService;
import com.sms.satp.utils.SecurityUtil;
import com.sms.satp.utils.SpelUtils;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.Objects;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.stereotype.Component;

@Component
@Aspect
@Slf4j
public class LogAspect {

    private final LogService logService;
    private final MongoTemplate mongoTemplate;
    private final FileService fileService;
    private final ThreadLocal<EvaluationContext> threadLocal = new ThreadLocal<>();

    public LogAspect(LogService logService, MongoTemplate mongoTemplate,
        FileService fileService) {
        this.logService = logService;
        this.mongoTemplate = mongoTemplate;
        this.fileService = fileService;
    }

    @Pointcut(value = "@annotation(com.sms.satp.common.aspect.annotation.LogRecord)")
    public void pointCut() {
    }

    @Around("pointCut() && @annotation(logRecord)")
    public Object around(ProceedingJoinPoint jp, LogRecord logRecord) throws Throwable {
        MethodSignature signature = (MethodSignature) jp.getSignature();
        Method method = signature.getMethod();
        Object[] args = jp.getArgs();
        OperationType operationType = logRecord.operationType();
        OperationModule operationModule = logRecord.operationModule();
        Enhance enhance = logRecord.enhance();
        EvaluationContext context = threadLocal.get();
        if (Objects.isNull(context)) {
            context = new StandardEvaluationContext();
            threadLocal.set(context);
        }
        SpelUtils.addVariable(context, args, method);
        enhance(enhance, context, operationModule, method);
        Object result = jp.proceed();
        String operationDesc = SpelUtils.getValue(context, logRecord.template(), String.class);
        String projectId = SpelUtils.getProjectId(context, logRecord, method, args);
        if (StringUtils.isEmpty(operationDesc)) {
            log.warn("The operationDesc is empty,please check the method: {} template:{}",
                method, logRecord.template());
        }
        LogEntity logEntity = LogEntity.builder().operationType(operationType).operationModule(operationModule)
            .operationDesc(operationDesc).operator(SecurityUtil.getCurrentUser().getUsername()).projectId(projectId)
            .build();
        logService.add(logEntity);
        return result;
    }

    @After("pointCut()")
    public void after() {
        threadLocal.remove();
    }

    private void enhance(Enhance enhance, EvaluationContext context, OperationModule operationModule, Method method) {
        if (enhance.enable()) {
            Object value = context.lookupVariable(enhance.primaryKey());
            if (Objects.nonNull(value)) {
                Object queryByIdResult;
                if (value instanceof Collection) {
                    Query query = Query.query(Criteria.where(ID.getFiled()).in((Collection) value));
                    queryByIdResult = mongoTemplate.find(query, Object.class, operationModule.getCollectionName());
                } else {
                    if (operationModule == OperationModule.TEST_FILE) {
                        queryByIdResult = fileService.findById((String) value);
                    } else {
                        queryByIdResult = mongoTemplate
                            .findById(value, Object.class, operationModule.getCollectionName());
                    }
                }
                context.setVariable(enhance.queryResultKey(), queryByIdResult);
            } else {
                log.error("The method:{} parameterNames not exist the primaryKey:{}.",
                    method, enhance.primaryKey());
            }
        }
    }

}
