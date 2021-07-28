package com.sms.satp.utils;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;

import com.sms.satp.common.aspect.annotation.LogRecord;
import java.lang.reflect.Method;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.expression.EvaluationContext;

@DisplayName("Tests for SpelUtil")
public class SpelUtilsTest {

    private final EvaluationContext evaluationContext = mock(EvaluationContext.class);
    private final LogRecord logRecord = mock(LogRecord.class);
    private final Method method = mock(Method.class);
    private static final String ID = ObjectId.get().toString();

    @Test
    @DisplayName("Test the getValue method in the SpelUtil")
    public void getValue_null_test() {
        assertThat(SpelUtils.getValue(evaluationContext, null, Class.class)).isNull();
    }

    @Test
    @DisplayName("Test the getValue method in the SpelUtil")
    public void getValue_exception_null_test() {
        assertThat(SpelUtils.getValue(evaluationContext, "test", Class.class)).isNull();
    }

    @Test
    @DisplayName("Test the getValue method in the SpelUtil")
    public void getValue_test() {
    }

    @Test
    @DisplayName("Test the getProjectId_test method in the SpelUtil")
    public void getProjectId_null_test() {
    }

    @Test
    @DisplayName("Test the getProjectId_test method in the SpelUtil")
    public void getProjectId_test() {
    }

    @Test
    @DisplayName("Test the addVariable_test method in the SpelUtil")
    public void addVariable_test() {

    }
}
