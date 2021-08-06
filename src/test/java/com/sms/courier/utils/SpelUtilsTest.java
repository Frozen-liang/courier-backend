package com.sms.courier.utils;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;

import com.sms.courier.common.aspect.annotation.LogRecord;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Encoders;
import io.jsonwebtoken.security.Keys;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.expression.EvaluationContext;

@DisplayName("Tests for SpelUtil")
public class SpelUtilsTest {

    private final EvaluationContext evaluationContext = mock(EvaluationContext.class);
    private final LogRecord logRecord = mock(LogRecord.class);
    private static final String ID = ObjectId.get().toString();

    @Test
    @DisplayName("Test the getValue method in the SpelUtil")
    public void getValue_null_test() {
        assertThat(SpelUtils.getValue(evaluationContext, null, Class.class)).isNull();
    }

    @Test
    @DisplayName("Test the getValue method in the SpelUtil")
    public void getValue_exception_null_test() {
        System.out.println(Encoders.BASE64.encode(Keys.secretKeyFor(SignatureAlgorithm.HS512).getEncoded()));
        assertThat(SpelUtils.getValue(evaluationContext, "test", Class.class)).isNull();
    }
}
