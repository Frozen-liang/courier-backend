package com.sms.courier.utils;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;

import com.sms.courier.common.aspect.annotation.LogRecord;
import java.util.List;
import java.util.Map;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.Expression;
import org.springframework.expression.common.TemplateParserContext;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;

@DisplayName("Tests for SpelUtil")
public class SpelUtilsTest {

    private final EvaluationContext evaluationContext = new StandardEvaluationContext();
    private static final String PREFIX = "{{";
    private static final String SUFFIX = "}}";
    private static final TemplateParserContext templateParserContext = new TemplateParserContext(PREFIX, SUFFIX);
    private static final SpelExpressionParser spelExpressionParser = new SpelExpressionParser();
    private final LogRecord logRecord = mock(LogRecord.class);
    private static final String ID = ObjectId.get().toString();

    @Test
    @DisplayName("Test the getValue method in the SpelUtil")
    public void getValue_null_test() {
        assertThat(SpelUtils.getValue(evaluationContext, null, Class.class)).isNull();
    }

    @Test
    public void test() {
        List<Map<String, String>> result = List.of(Map.of("apiName", "test01"), Map.of("apiName", "test02"));
        evaluationContext.setVariable("result", result);
        Expression expression = spelExpressionParser
            .parseExpression("{{#result?.![#this.apiName]}}", templateParserContext);
        System.out.println(expression.getValue(evaluationContext, String.class));
    }

}
