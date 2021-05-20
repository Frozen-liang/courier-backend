package com.sms.satp.utils;

import com.sms.satp.common.aspect.annotation.LogRecord;
import com.sms.satp.common.enums.OperationType;
import java.lang.reflect.Method;
import java.util.Objects;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.core.LocalVariableTableParameterNameDiscoverer;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.EvaluationException;
import org.springframework.expression.Expression;
import org.springframework.expression.common.TemplateParserContext;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;

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

    private SpelUtils() {
    }

    public static Object getValue(EvaluationContext context, String name) {
        try {
            Expression expression = spelExpressionParser.parseExpression(SYMBOL + name);
            return expression.getValue(context);
        } catch (Exception e) {
            log.error("Parse expression:{} error", name);
        }
        return null;
    }

    public static String getProjectId(EvaluationContext context, LogRecord logRecord, Method method) {
        try {
            Expression expression;
            String value = null;
            String exp;
            if (logRecord.operationType() == OperationType.DELETE) {
                if (!logRecord.enhance().enable()) {
                    return null;
                }
                String resultKey = logRecord.enhance().queryResultKey();
                exp = createObjectExpression(resultKey, logRecord.projectId());
                expression = spelExpressionParser.parseExpression(exp, templateParserContext);
                try {
                    value = expression.getValue(context, String.class);
                } catch (EvaluationException e) {
                    log.warn("The expression:{} cannot get the value.", exp);
                }
                if (StringUtils.isEmpty(value)) {
                    exp = createListExpression(resultKey, logRecord.projectId());
                    expression = spelExpressionParser.parseExpression(exp, templateParserContext);
                    return expression.getValue(context, String.class);
                }
            }

            String[] parameterNames = parameterNameDiscoverer.getParameterNames(method);
            Objects.requireNonNull(parameterNames);
            for (String parameterName : parameterNames) {
                exp = createObjectExpression(parameterName, logRecord.projectId());
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
        } catch (Exception e) {
            log.error("Parse expression:{} error,methodName:{}", logRecord.projectId(), method);
        }
        return null;
    }

    public static <T> T getValue(EvaluationContext context, String template, Class<T> clazz) {
        try {
            Expression expression = spelExpressionParser.parseExpression(template, templateParserContext);
            return expression.getValue(context, clazz);
        } catch (Exception e) {
            log.error("Parse expression:{} error", template);
        }
        return null;
    }

    public static EvaluationContext getContext(Object[] arguments, Method signatureMethod) {
        StandardEvaluationContext context = new StandardEvaluationContext();
        try {
            String[] parameterNames = parameterNameDiscoverer.getParameterNames(signatureMethod);
            if (parameterNames == null || parameterNames.length == 0) {
                return context;
            }
            for (int i = 0; i < arguments.length; i++) {
                context.setVariable(parameterNames[i], arguments[i]);
            }
        } catch (Exception e) {
            log.error("Get EvaluationContext error.");
        }
        return context;
    }

    private static String createObjectExpression(String prefixName, String name) {
        return PREFIX + SYMBOL + prefixName + DOT + name + SUFFIX;
    }

    private static String createListExpression(String prefixName, String name) {
        return PREFIX + SYMBOL + prefixName + "[0]" + DOT + name + SUFFIX;
    }

}
