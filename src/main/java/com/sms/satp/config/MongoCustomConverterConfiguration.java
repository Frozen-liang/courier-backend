package com.sms.satp.config;

import com.sms.satp.common.enums.ApiBindingStatus;
import com.sms.satp.common.enums.ApiJsonType;
import com.sms.satp.common.enums.ApiProtocol;
import com.sms.satp.common.enums.ApiRequestParamType;
import com.sms.satp.common.enums.ApiStatus;
import com.sms.satp.common.enums.ApiTagType;
import com.sms.satp.common.enums.ApiType;
import com.sms.satp.common.enums.DocumentType;
import com.sms.satp.common.enums.EnumCommon;
import com.sms.satp.common.enums.GroupImportType;
import com.sms.satp.common.enums.MatchType;
import com.sms.satp.common.enums.OperationModule;
import com.sms.satp.common.enums.OperationType;
import com.sms.satp.common.enums.ParamType;
import com.sms.satp.common.enums.RequestMethod;
import com.sms.satp.common.enums.SaveMode;
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
                IntegerToApiTagTypeConverter.INSTANCE, IntegerToApiJsonTypeConverter.INSTANCE,
                IntegerToSaveModeConverter.INSTANCE, IntegerToDocumentTypeConverter.INSTANCE,
                IntegerToGroupImportTypeConverter.INSTANCE, IntegerToOperationTypeConverter.INSTANCE,
                IntegerToOperationModuleConverter.INSTANCE, IntegerToGroupImportTypeConverter.INSTANCE,
                IntegerToApiTypeConverter.INSTANCE, IntegerToMatchTypeConverter.INSTANCE,
                IntegerToApiBindingStatusConverter.INSTANCE);
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
    enum IntegerToApiTagTypeConverter implements Converter<Integer, ApiTagType> {
        INSTANCE;

        public ApiTagType convert(@NotNull Integer code) {
            return ApiTagType.getType(code);
        }
    }

    @ReadingConverter
    enum IntegerToApiJsonTypeConverter implements Converter<Integer, ApiJsonType> {
        INSTANCE;

        public ApiJsonType convert(@NotNull Integer code) {
            return ApiJsonType.getType(code);
        }
    }

    @ReadingConverter
    enum IntegerToSaveModeConverter implements Converter<Integer, SaveMode> {
        INSTANCE;

        public SaveMode convert(@NotNull Integer code) {
            return SaveMode.getType(code);
        }
    }

    @ReadingConverter
    enum IntegerToDocumentTypeConverter implements Converter<Integer, DocumentType> {
        INSTANCE;

        public DocumentType convert(@NotNull Integer code) {
            return DocumentType.getType(code);
        }
    }

    @ReadingConverter
    enum IntegerToGroupImportTypeConverter implements Converter<Integer, GroupImportType> {
        INSTANCE;

        public GroupImportType convert(@NotNull Integer code) {
            return GroupImportType.getType(code);
        }
    }

    @ReadingConverter
    enum IntegerToOperationTypeConverter implements Converter<Integer, OperationType> {
        INSTANCE;

        public OperationType convert(@NotNull Integer code) {
            return OperationType.getType(code);
        }
    }

    @ReadingConverter
    enum IntegerToOperationModuleConverter implements Converter<Integer, OperationModule> {
        INSTANCE;

        public OperationModule convert(@NotNull Integer code) {
            return OperationModule.getType(code);
        }
    }

    @ReadingConverter
    enum IntegerToApiTypeConverter implements Converter<Integer, ApiType> {
        INSTANCE;

        public ApiType convert(@NotNull Integer code) {
            return ApiType.getApiType(code);
        }
    }

    @ReadingConverter
    enum IntegerToMatchTypeConverter implements Converter<Integer, MatchType> {
        INSTANCE;

        public MatchType convert(@NotNull Integer code) {
            return MatchType.getMatchType(code);
        }
    }

    @ReadingConverter
    enum IntegerToApiBindingStatusConverter implements Converter<Integer, ApiBindingStatus> {
        INSTANCE;

        public ApiBindingStatus convert(@NotNull Integer code) {
            return ApiBindingStatus.getApiBindingStatus(code);
        }
    }
}
