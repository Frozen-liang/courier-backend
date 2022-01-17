package com.sms.courier.utils;

import com.github.mustachejava.DefaultMustacheFactory;
import com.github.mustachejava.Mustache;
import com.github.mustachejava.MustacheFactory;
import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.io.Writer;
import java.util.Objects;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class MustacheUtils {

    private static final MustacheFactory MUSTACHE_FACTORY = new DefaultMustacheFactory();

    private MustacheUtils() {
    }

    public static String getContent(String template, Object scope) {
        Mustache mustache = MUSTACHE_FACTORY.compile(new StringReader(template), "courier");
        Writer result = mustache.execute(new StringWriter(), scope);
        try {
            return result.toString().replace("&quot;","\"");
        } finally {
            if (Objects.nonNull(result)) {
                try {
                    result.close();
                } catch (IOException e) {
                    log.error("Close mustache string writer error!", e);
                }
            }
        }
    }
}
