package com.sms.courier.generator;

import com.sms.courier.common.exception.ApiTestPlatformException;
import com.sms.courier.entity.generator.CodeTemplate;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayName("Test for TemplateEngine")
public class TemplateEngineTest {

    private final TemplateEngine templateEngine = new MustacheTemplateEngine();

    @Test
    @DisplayName("Test the getRendered method for TemplateEngine")
    public void getRendered_test() {
        CodeTemplate codeTemplate = CodeTemplate.builder().value("test").build();
        BaseCodegen baseCodegen = BaseCodegen.builder().build();
        String str = templateEngine.getRendered(baseCodegen, codeTemplate);
        assertThat(str).isNotNull();
    }

    @Test
    @DisplayName("Test the getRendered method for TemplateEngine")
    public void getRendered_test_thenException() {
        CodeTemplate codeTemplate = CodeTemplate.builder().build();
        BaseCodegen baseCodegen = BaseCodegen.builder().build();
        assertThatThrownBy(()->templateEngine.getRendered(baseCodegen, codeTemplate))
            .isInstanceOf(ApiTestPlatformException.class);
    }
}
