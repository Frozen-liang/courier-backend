package com.sms.courier.utils;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import java.io.IOException;
import java.time.Duration;

public class StringToDurationSerializer extends JsonDeserializer<Duration> {


    @Override
    public Duration deserialize(JsonParser jsonParser, DeserializationContext deserializationContext)
        throws IOException, JsonProcessingException {

        return Duration.ofMillis(jsonParser.getValueAsLong());
    }


}