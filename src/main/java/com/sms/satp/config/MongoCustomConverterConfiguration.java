package com.sms.satp.config;

import com.sms.satp.common.enums.ApiLabelType;
import com.sms.satp.common.enums.ApiProtocol;
import com.sms.satp.common.enums.ApiRequestParamType;
import com.sms.satp.common.enums.ApiStatus;
import com.sms.satp.common.enums.EnumCommon;
import com.sms.satp.common.enums.ParamType;
import com.sms.satp.common.enums.RequestMethod;
import com.sun.istack.NotNull;
import java.util.List;
import java.util.Optional;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.convert.ReadingConverter;
import org.springframework.data.convert.WritingConverter;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.mongodb.core.convert.MongoCustomConversions;

@Configuration
public class MongoCustomConverterConfiguration {

    @Bean
    MongoCustomConversions mongoCustomConversions() {
        List<Converter<?, ?>> converters = List
            .of(EnumCommonToIntegerConverter.INSTANCE, IntegerToApiProtocolConverter.INSTANCE,
                IntegerToApiRequestParamTypeConverter.INSTANCE, IntegerToApiStatusConverter.INSTANCE,
                IntegerToParamTypeConverter.INSTANCE, IntegerToRequestMethodConverter.INSTANCE,
                IntegerToApiLabelTypeConverter.INSTANCE);
        return new MongoCustomConversions(converters);
    }

    @Bean
    AuditorAware<Long> auditorAware() {
        // get createUserId and modifyUserId
        return () -> Optional.of(1L);
    }

    @WritingConverter
    enum EnumCommonToIntegerConverter implements Converter<EnumCommon, Integer> {
        INSTANCE;

        public Integer convert(EnumCommon enumCommon) {
            return enumCommon.getCode();
        }
    }

    @ReadingConverter
    enum IntegerToApiProtocolConverter implements Converter<Integer, ApiProtocol> {
        INSTANCE;

        public ApiProtocol convert(@NotNull Integer code) {
            return ApiProtocol.getType(code);
        }
    }

    @ReadingConverter
    enum IntegerToApiRequestParamTypeConverter implements Converter<Integer, ApiRequestParamType> {
        INSTANCE;

        public ApiRequestParamType convert(@NotNull Integer code) {
            return ApiRequestParamType.getType(code);
        }
    }


    @ReadingConverter
    enum IntegerToApiStatusConverter implements Converter<Integer, ApiStatus> {
        INSTANCE;

        public ApiStatus convert(@NotNull Integer code) {
            return ApiStatus.getType(code);
        }
    }


    @ReadingConverter
    enum IntegerToParamTypeConverter implements Converter<Integer, ParamType> {
        INSTANCE;

        public ParamType convert(@NotNull Integer code) {
            return ParamType.getType(code);
        }
    }

    @ReadingConverter
    enum IntegerToRequestMethodConverter implements Converter<Integer, RequestMethod> {
        INSTANCE;

        public RequestMethod convert(@NotNull Integer code) {
            return RequestMethod.getType(code);
        }
    }

    @ReadingConverter
    enum IntegerToApiLabelTypeConverter implements Converter<Integer, ApiLabelType> {
        INSTANCE;

        public ApiLabelType convert(@NotNull Integer code) {
            return ApiLabelType.getType(code);
        }
    }

}
