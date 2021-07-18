package com.sms.satp.config;

import com.sms.satp.common.enums.ApiBindingStatus;
import com.sms.satp.common.enums.ApiJsonType;
import com.sms.satp.common.enums.ApiProtocol;
import com.sms.satp.common.enums.ApiRequestParamType;
import com.sms.satp.common.enums.ApiStatus;
import com.sms.satp.common.enums.ApiTagType;
import com.sms.satp.common.enums.ApiType;
import com.sms.satp.common.enums.DocumentType;
import com.sms.satp.common.enums.DocumentUrlType;
import com.sms.satp.common.enums.EnumCommon;
import com.sms.satp.common.enums.GroupImportType;
import com.sms.satp.common.enums.JobStatus;
import com.sms.satp.common.enums.MatchType;
import com.sms.satp.common.enums.OperationModule;
import com.sms.satp.common.enums.OperationType;
import com.sms.satp.common.enums.ParamType;
import com.sms.satp.common.enums.ProjectType;
import com.sms.satp.common.enums.RequestMethod;
import com.sms.satp.common.enums.ResponseParamsExtractionType;
import com.sms.satp.common.enums.ResultVerificationType;
import com.sms.satp.common.enums.SaveMode;
import java.util.List;
import java.util.Optional;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.convert.ReadingConverter;
import org.springframework.data.convert.WritingConverter;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.mongodb.MongoDatabaseFactory;
import org.springframework.data.mongodb.core.convert.MongoConverter;
import org.springframework.data.mongodb.core.convert.MongoCustomConversions;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;
import org.springframework.lang.NonNull;

@Configuration
public class MongoCustomConverterConfiguration {

    private static final String BUCKET = "TestFile";

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
                IntegerToApiBindingStatusConverter.INSTANCE, IntegerToGroupImportTypeConverter.INSTANCE,
                IntegerToDocumentUrlTypeConverter.INSTANCE, IntegerToJobStatusConverter.INSTANCE,
                IntegerToProjectTypeConverter.INSTANCE, IntegerToResultVerificationTypeConverter.INSTANCE,
                IntegerToResponseParamsExtractionTypeConverter.INSTANCE);
        return new MongoCustomConversions(converters);
    }

    @Bean
    AuditorAware<String> auditorAware() {
        // get createUserId and modifyUserId
        return () -> Optional.of("1");
    }


    @Bean
    public GridFsTemplate gridFsTemplate(MongoDatabaseFactory dbFactory, MongoConverter mongoConverter) {
        return new GridFsTemplate(dbFactory, mongoConverter, BUCKET);
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

        public ApiProtocol convert(@NonNull Integer code) {
            return ApiProtocol.getType(code);
        }
    }

    @ReadingConverter
    enum IntegerToApiRequestParamTypeConverter implements Converter<Integer, ApiRequestParamType> {
        INSTANCE;

        public ApiRequestParamType convert(@NonNull Integer code) {
            return ApiRequestParamType.getType(code);
        }
    }


    @ReadingConverter
    enum IntegerToApiStatusConverter implements Converter<Integer, ApiStatus> {
        INSTANCE;

        public ApiStatus convert(@NonNull Integer code) {
            return ApiStatus.getType(code);
        }
    }


    @ReadingConverter
    enum IntegerToParamTypeConverter implements Converter<Integer, ParamType> {
        INSTANCE;

        public ParamType convert(@NonNull Integer code) {
            return ParamType.getType(code);
        }
    }

    @ReadingConverter
    enum IntegerToRequestMethodConverter implements Converter<Integer, RequestMethod> {
        INSTANCE;

        public RequestMethod convert(@NonNull Integer code) {
            return RequestMethod.getType(code);
        }
    }

    @ReadingConverter
    enum IntegerToApiTagTypeConverter implements Converter<Integer, ApiTagType> {
        INSTANCE;

        public ApiTagType convert(@NonNull Integer code) {
            return ApiTagType.getType(code);
        }
    }

    @ReadingConverter
    enum IntegerToApiJsonTypeConverter implements Converter<Integer, ApiJsonType> {
        INSTANCE;

        public ApiJsonType convert(@NonNull Integer code) {
            return ApiJsonType.getType(code);
        }
    }

    @ReadingConverter
    enum IntegerToSaveModeConverter implements Converter<Integer, SaveMode> {
        INSTANCE;

        public SaveMode convert(@NonNull Integer code) {
            return SaveMode.getType(code);
        }
    }

    @ReadingConverter
    enum IntegerToDocumentTypeConverter implements Converter<Integer, DocumentType> {
        INSTANCE;

        public DocumentType convert(@NonNull Integer code) {
            return DocumentType.getType(code);
        }
    }

    @ReadingConverter
    enum IntegerToGroupImportTypeConverter implements Converter<Integer, GroupImportType> {
        INSTANCE;

        public GroupImportType convert(@NonNull Integer code) {
            return GroupImportType.getType(code);
        }
    }

    @ReadingConverter
    enum IntegerToOperationTypeConverter implements Converter<Integer, OperationType> {
        INSTANCE;

        public OperationType convert(@NonNull Integer code) {
            return OperationType.getType(code);
        }
    }

    @ReadingConverter
    enum IntegerToOperationModuleConverter implements Converter<Integer, OperationModule> {
        INSTANCE;

        public OperationModule convert(@NonNull Integer code) {
            return OperationModule.getType(code);
        }
    }

    @ReadingConverter
    enum IntegerToApiTypeConverter implements Converter<Integer, ApiType> {
        INSTANCE;

        public ApiType convert(@NonNull Integer code) {
            return ApiType.getApiType(code);
        }
    }

    @ReadingConverter
    enum IntegerToMatchTypeConverter implements Converter<Integer, MatchType> {
        INSTANCE;

        public MatchType convert(@NonNull Integer code) {
            return MatchType.getMatchType(code);
        }
    }

    @ReadingConverter
    enum IntegerToDocumentUrlTypeConverter implements Converter<Integer, DocumentUrlType> {
        INSTANCE;

        public DocumentUrlType convert(@NonNull Integer code) {
            return DocumentUrlType.getType(code);
        }
    }


    @ReadingConverter
    enum IntegerToApiBindingStatusConverter implements Converter<Integer, ApiBindingStatus> {
        INSTANCE;

        public ApiBindingStatus convert(@NonNull Integer code) {
            return ApiBindingStatus.getApiBindingStatus(code);
        }
    }

    @ReadingConverter
    enum IntegerToJobStatusConverter implements Converter<Integer, JobStatus> {
        INSTANCE;

        public JobStatus convert(@NonNull Integer code) {
            return JobStatus.getType(code);
        }
    }

    @ReadingConverter
    enum IntegerToProjectTypeConverter implements Converter<Integer, ProjectType> {
        INSTANCE;

        public ProjectType convert(@NonNull Integer code) {
            return ProjectType.getType(code);
        }
    }

    @ReadingConverter
    enum IntegerToResultVerificationTypeConverter implements Converter<Integer, ResultVerificationType> {
        INSTANCE;

        public ResultVerificationType convert(@NonNull Integer code) {
            return ResultVerificationType.getType(code);
        }
    }

    @ReadingConverter
    enum IntegerToResponseParamsExtractionTypeConverter implements Converter<Integer, ResponseParamsExtractionType> {
        INSTANCE;

        public ResponseParamsExtractionType convert(@NonNull Integer code) {
            return ResponseParamsExtractionType.getType(code);
        }
    }

}

