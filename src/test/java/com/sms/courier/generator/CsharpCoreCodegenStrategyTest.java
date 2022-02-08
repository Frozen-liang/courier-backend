package com.sms.courier.generator;

import com.sms.courier.common.enums.ParamType;
import com.sms.courier.common.enums.TemplateType;
import com.sms.courier.common.exception.ApiTestPlatformException;
import com.sms.courier.common.exception.ErrorCode;
import com.sms.courier.dto.request.CodeGenRequest;
import com.sms.courier.dto.response.ApiResponse;
import com.sms.courier.dto.response.ParamInfoResponse;
import com.sms.courier.dto.response.StructureRefResponse;
import com.sms.courier.entity.generator.CodeTemplate;
import com.sms.courier.entity.generator.GeneratorTemplateEntity;
import com.sms.courier.generator.csharp.impl.CsharpCoreCodegenStrategy;
import com.sms.courier.generator.pojo.CodeEntityParamVo;
import com.sms.courier.generator.pojo.FilePackageVo;
import com.sms.courier.mapper.CodegenMapper;
import java.util.List;
import org.assertj.core.util.Lists;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@DisplayName("Test for CsharpCoreCodegenStrategy")
public class CsharpCoreCodegenStrategyTest {

    private final CodegenMapper codegenMapper = mock(CodegenMapper.class);
    private final TemplateEngine templateEngine = mock(TemplateEngine.class);
    private final CsharpCoreCodegenStrategy csharpCoreCodegenStrategy = new CsharpCoreCodegenStrategy(templateEngine,
        codegenMapper);

    private final String MOCK_ID = new ObjectId().toString();
    private final String MOCK_DES = "test";
    private final Integer MOCK_TYPE = 0;

    private final CodeGenRequest request = CodeGenRequest.builder().templateType(Lists.newArrayList(0, 1, 2, 3))
        .templateId(MOCK_ID).apiId(MOCK_ID)
        .build();
    private final ApiResponse apiResponse = ApiResponse.builder().id(MOCK_ID)
        .requestParams(Lists.newArrayList(
            ParamInfoResponse.builder()
                .ref(Boolean.TRUE)
                .customStructs(
                    Lists.newArrayList(ParamInfoResponse.builder()
                        .paramId(MOCK_ID).key(MOCK_DES).paramType(MOCK_TYPE).description(MOCK_DES)
                        .childParam(Lists.newArrayList(ParamInfoResponse.builder().build()))
                        .build()))
                .structureRef(StructureRefResponse.builder()
                    .struct(Lists.newArrayList(ParamInfoResponse.builder().paramId(MOCK_ID).build()))
                    .build())
                .build(), ParamInfoResponse.builder().paramType(13).build()
        ))
        .responseParams(Lists.newArrayList(ParamInfoResponse.builder().paramType(13).build()))
        .restfulParams(Lists.newArrayList(ParamInfoResponse.builder().paramType(MOCK_TYPE).build()))
        .pathParams(Lists.newArrayList(ParamInfoResponse.builder().paramType(MOCK_TYPE).build()))
        .build();
    private final GeneratorTemplateEntity templateEntity =
        GeneratorTemplateEntity.builder().id(MOCK_ID)
            .codeTemplates(Lists.newArrayList(
                CodeTemplate.builder().templateType(TemplateType.ENTITY).value(MOCK_DES).build(),
                CodeTemplate.builder().templateType(TemplateType.CONTROLLER).value(MOCK_DES).build(),
                CodeTemplate.builder().templateType(TemplateType.SERVICE).value(MOCK_DES).build(),
                CodeTemplate.builder().templateType(TemplateType.SERVICE_IMPL).value(MOCK_DES).build()
            ))
            .build();

    private final CodeEntityParamVo objectVo = CodeEntityParamVo.builder()
        .paramType("13")
        .key(MOCK_DES)
        .oldParamType(ParamType.OBJECT)
        .childParam(
            Lists.newArrayList(CodeEntityParamVo.builder().paramType("0").oldParamType(ParamType.STRING).build(),
                CodeEntityParamVo.builder().paramType("13").key(MOCK_DES).oldParamType(ParamType.OBJECT)
                    .childParam(Lists.newArrayList()).build()
            ))
        .build();
    private final CodeEntityParamVo arrayVo =
        CodeEntityParamVo.builder().key(MOCK_DES).paramType("12").oldParamType(ParamType.ARRAY)
            .childParam(Lists
                .newArrayList(
                    CodeEntityParamVo.builder().paramType("0").oldParamType(ParamType.STRING).build(), objectVo))
            .build();

    private final List<CodeEntityParamVo> requestParams = Lists.newArrayList(
        objectVo,
        arrayVo,
        CodeEntityParamVo.builder().paramType("0").oldParamType(ParamType.STRING).build());

    @Test
    @DisplayName("Test the generate method for CsharpCoreCodegenStrategy")
    public void generate_test() {
        when(codegenMapper.toParamModelList(any())).thenReturn(requestParams);
        List<FilePackageVo> filePackageVos = csharpCoreCodegenStrategy.generate(request, apiResponse, templateEntity);
        assertThat(filePackageVos).isNotEmpty();
    }

    @Test
    @DisplayName("Test the generate method for CsharpCoreCodegenStrategy thrown Exception")
    public void generate_test_thenSmsException() {
        when(templateEngine.getRendered(any(), any()))
            .thenThrow(new ApiTestPlatformException(ErrorCode.MUSTACHE_RENDERED_ERROR));
        assertThatThrownBy(() -> csharpCoreCodegenStrategy.generate(request, apiResponse, templateEntity))
            .isInstanceOf(ApiTestPlatformException.class);
    }

    @Test
    @DisplayName("Test the generate method for CsharpCoreCodegenStrategy thrown Exception")
    public void generate_test_thenException() {
        when(templateEngine.getRendered(any(), any()))
            .thenThrow(new RuntimeException());
        assertThatThrownBy(() -> csharpCoreCodegenStrategy.generate(request, apiResponse, templateEntity))
            .isInstanceOf(ApiTestPlatformException.class);
    }

}
