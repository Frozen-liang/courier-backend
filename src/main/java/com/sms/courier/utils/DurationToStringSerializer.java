package com.sms.courier.utils;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import java.io.IOException;
import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.Objects;
import org.springframework.boot.convert.DurationUnit;

public class DurationToStringSerializer extends JsonSerializer<Duration> {


    @Override
    public void serialize(Duration duration, JsonGenerator jsonGenerator, SerializerProvider serializerProvider)
        throws IOException {
        DurationUnit durationUnit = duration.getClass().getAnnotation(DurationUnit.class);
        ChronoUnit unit = Objects.nonNull(durationUnit) ? durationUnit.value() : ChronoUnit.MILLIS;
        jsonGenerator.writeString(String.valueOf(duration.get(unit)));

    }
}