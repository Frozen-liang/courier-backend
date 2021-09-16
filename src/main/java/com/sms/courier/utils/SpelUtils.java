package com.sms.courier.utils;

import static com.sms.courier.common.enums.OperationType.DELETE;
import static com.sms.courier.common.enums.OperationType.RECOVER;
import static com.sms.courier.common.enums.OperationType.REMOVE;
import static com.sms.courier.utils.Assert.isTrue;

import com.sms.courier.common.aspect.annotation.LogRecord;
import com.sms.courier.common.enums.OperationType;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.core.LocalVariableTableParameterNameDiscoverer;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.EvaluationException;
import org.springframework.expression.Expression;
import org.springframework.expression.common.TemplateParserContext;
import org.springframework.expression.spel.standard.SpelExpressionParser;

@Slf4j
public class SpelUtils {

    private static final String PREFIX = "{{";
    private static final String SUFFIX = "}}";
    private static final String DOT = ".";
    private static final String SYMBOL = "#";
    private static final TemplateParserContext templateParserContext = new TemplateParserContext(PREFIX, SUFFIX);
    private static final SpelExpressionParser spelExpressionParser = new SpelExpressionParser();
    private static final LocalVariableTableParameterNameDiscoverer parameterNameDiscoverer =
        new LocalVariableTableParameterNameDiscoverer();
    private static final List<OperationType> OPERATION_TYPES = List.of(DELETE, REMOVE, RECOVER);

    private SpelUtils() {
    }

    public static <T> T getValue(EvaluationContext context, String template, Class<T> clazz) {
        if (StringUtils.isEmpty(template)) {
            return null;
        }
        try {
            Expression expression = spelExpressionParser.parseExpression(template, templateParserContext);
            return expression.getValue(context, clazz);
        } catch (Exception e) {
            log.error("Parse expression:{} error", template);
        }
        return null;
    }

    @SuppressFBWarnings("NP_NULL_ON_SOME_PATH_FROM_RETURN_VALUE")
    public static String getProjectId(EvaluationContext context, LogRecord logRecord, Method method, Object[] args) {
        try {
            Objects.requireNonNull(logRecord.refId());
            // 如果是删除操作 则从result获取projectId或workspaceId
            if (OPERATION_TYPES.contains(logRecord.operationType())) {
                isTrue(logRecord.enhance().enable(), "The enhance enable is false.");
                return getProjectByResult(context, logRecord);
            }
            // 从请求参数中获取projectId或workspaceId
            return getProjectIdByParams(context, logRecord, method, args);
        } catch (Exception e) {
            log.error("Parse expression:{} error,methodName:{}, message :{}", logRecord.refId(), method,
                e.getMessage());
        }
        return null;
    }

    @SuppressFBWarnings("NP_NULL_ON_SOME_PATH_FROM_RETURN_VALUE")
    private static String getProjectIdByParams(EvaluationContext context, LogRecord logRecord, Method method,
        Object[] args) {
        String exp;
        Expression expression;
        String value = null;
        String[] parameterNames = parameterNameDiscoverer.getParameterNames(method);
        Objects.requireNonNull(parameterNames);
        for (int i = 0; i < parameterNames.length; i++) {
            if (args[i] instanceof Collection) {
                exp = createListExpression(parameterNames[i], logRecord.refId());
            } else {
                exp = createObjectExpression(parameterNames[i], logRecord.refId());
            }
            expression = spelExpressionParser.parseExpression(exp, templateParserContext);
            try {
                value = expression.getValue(context, String.class);
            } catch (EvaluationException e) {
                log.warn("The expression:{} cannot get the value.", exp);
            }
            if (Objects.nonNull(value)) {
                return value;
            }
        }
        return null;
    }

    private static String getProjectByResult(EvaluationContext context, LogRecord logRecord) {
        String value = null;
        String exp;
        Expression expression;
        String resultKey = logRecord.enhance().queryResultKey();
        exp = createListExpression(resultKey, logRecord.refId());
        expression = spelExpressionParser.parseExpression(exp, templateParserContext);
        try {
            value = expression.getValue(context, String.class);
        } catch (EvaluationException e) {
            log.warn("The expression:{} cannot get the value.", exp);
        }
        if (StringUtils.isBlank(value)) {
            exp = createObjectExpression(resultKey, logRecord.refId());
            expression = spelExpressionParser.parseExpression(exp, templateParserContext);
            return expression.getValue(context, String.class);
        }
        return value;
    }

    public static void addVariable(EvaluationContext context, Object[] arguments, Method signatureMethod) {
        try {
            String[] parameterNames = parameterNameDiscoverer.getParameterNames(signatureMethod);
            if (parameterNames == null || parameterNames.length == 0) {
                return;
            }
            for (int i = 0; i < arguments.length; i++) {
                context.setVariable(parameterNames[i], arguments[i]);
            }
        } catch (Exception e) {
            log.error("EvaluationContext set variable error.");
        }
    }

    // 创建对象表达式
    private static String createObjectExpression(String prefixName, String name) {
        // PREFIX + SYMBOL + prefixName + DOT + name + SUFFIX;
        return String.join("", PREFIX, SYMBOL, prefixName, DOT, name, SUFFIX);
    }

    // 创建集合表达式
    private static String createListExpression(String prefixName, String name) {
        // PREFIX + SYMBOL + prefixName + "[0]" + DOT + name + SUFFIX
        return String.join("", PREFIX, SYMBOL, prefixName, "[0]", DOT, name, SUFFIX);
    }

}
