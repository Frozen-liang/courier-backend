package com.sms.courier.generator;

import com.github.mustachejava.DefaultMustacheFactory;
import com.github.mustachejava.Mustache;
import com.github.mustachejava.MustacheFactory;
import com.sms.courier.common.exception.ErrorCode;
import com.sms.courier.entity.generator.CodeTemplate;
import com.sms.courier.utils.ExceptionUtils;
import java.io.StringReader;
import java.io.StringWriter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class MustacheTemplateEngine implements TemplateEngine {

    @Override
    public String getRendered(BaseCodegen codegenModel, CodeTemplate codeTemplate) {
        String response = "";
        StringWriter writer = new StringWriter();
        try {
            MustacheFactory mf = new DefaultMustacheFactory();
            StringReader stringReader = new StringReader(codeTemplate.getValue());
            Mustache mustache = mf.compile(stringReader, null);
            mustache.execute(writer, codegenModel).flush();
            response = writer.toString();
        } catch (Exception e) {
            log.error("Mustache Rendering error!", e);
            throw ExceptionUtils.mpe(ErrorCode.MUSTACHE_RENDERED_ERROR);
        }
        return response;
    }

}
