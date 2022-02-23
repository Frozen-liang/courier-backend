package com.sms.courier.common.aspect.log;

import static com.sms.courier.common.field.CommonField.ID;

import com.sms.courier.common.aspect.annotation.Enhance;
import com.sms.courier.common.aspect.annotation.LogRecord;
import com.sms.courier.common.enums.OperationModule;
import com.sms.courier.common.enums.OperationType;
import com.sms.courier.entity.log.LogEntity;
import com.sms.courier.service.FileService;
import com.sms.courier.service.LogService;
import com.sms.courier.utils.SecurityUtil;
import com.sms.courier.utils.SpelUtils;
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

    @Pointcut(value = "@annotation(com.sms.courier.common.aspect.annotation.LogRecord)")
    public void pointCut() {
    }

    @Around("pointCut() && @annotation(logRecord)")
    public Object around(ProceedingJoinPoint jp, LogRecord logRecord) throws Throwable {
        final MethodSignature signature = (MethodSignature) jp.getSignature();
        final Method method = signature.getMethod();
        final Object[] args = jp.getArgs();
        final OperationType operationType = logRecord.operationType();
        final OperationModule operationModule = logRecord.operationModule();
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
        String sourceId = SpelUtils.getValue(context, logRecord.sourceId(), String.class);
        String projectId = SpelUtils.getProjectId(context, logRecord, method, args);
        if (StringUtils.isEmpty(operationDesc)) {
            log.warn("The operationDesc is empty,please check the method: {} template:{}",
                method, logRecord.template());
        }
        try {
            LogEntity logEntity = LogEntity.builder().operationType(operationType).operationModule(operationModule)
                .operationDesc(operationDesc).operator(SecurityUtil.getCurrentUser().getUsername()).refId(projectId)
                .sourceId(sourceId).build();
            logService.add(logEntity);
        } catch (Exception e) {
            log.error("Insert log error!", e);
        }
        return result;
    }

    @After("pointCut()")
    public void after() {
        threadLocal.remove();
    }

    private void enhance(Enhance enhance, EvaluationContext context, OperationModule operationModule, Method method) {
        try {
            if (enhance.enable()) {
                Object value = context.lookupVariable(enhance.primaryKey());
                if (Objects.nonNull(value)) {
                    Object queryByIdResult;
                    queryByIdResult = getQueryResult(operationModule, value);
                    context.setVariable(enhance.queryResultKey(), queryByIdResult);
                } else {
                    log.error("The method:{} parameterNames not exist the primaryKey:{}.",
                        method, enhance.primaryKey());
                }
            }
        } catch (Exception e) {
            log.error("Log enhance error!", e);
        }
    }

    private Object getQueryResult(OperationModule operationModule, Object value) {
        if (value instanceof Collection) {
            Query query = Query.query(Criteria.where(ID.getName()).in((Collection) value));
            return mongoTemplate.find(query, operationModule.getEntityClass());
        }
        if (operationModule == OperationModule.TEST_FILE) {
            return fileService.findById((String) value);
        }
        return mongoTemplate.findById(value, operationModule.getEntityClass());

    }

}
