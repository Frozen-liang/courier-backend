package com.sms.satp.utils;

import java.lang.reflect.Method;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.LocalVariableTableParameterNameDiscoverer;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.Expression;
import org.springframework.expression.common.TemplateParserContext;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;

@Slf4j
public class SpelUtils {

    private static final TemplateParserContext templateParserContext = new TemplateParserContext("{{", "}}");
    private static final SpelExpressionParser spelExpressionParser = new SpelExpressionParser();
    private static final LocalVariableTableParameterNameDiscoverer parameterNameDiscoverer =
        new LocalVariableTableParameterNameDiscoverer();

    private SpelUtils() {
    }

    public static Object getValue(EvaluationContext context, String name) {
        try {
            Expression expression = spelExpressionParser.parseExpression("#" + name);
            return expression.getValue(context);
        } catch (Exception e) {
            log.error("Parse expression:{} error", name);
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

}
