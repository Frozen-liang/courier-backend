package com.sms.courier.utils;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import java.io.IOException;
import java.time.Duration;
import org.springframework.boot.convert.ApplicationConversionService;

public class DurationToStringSerializer extends JsonSerializer<Duration> {


    @Override
    public void serialize(Duration duration, JsonGenerator jsonGenerator, SerializerProvider serializerProvider)
        throws IOException {
        jsonGenerator.writeString(
            String.valueOf(ApplicationConversionService.getSharedInstance().convert(duration, Long.class)));

    }
}