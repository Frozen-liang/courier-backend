package com.sms.courier.generator;

import com.sms.courier.common.enums.ParamType;
import com.sms.courier.entity.generator.CodeTemplate;
import com.sms.courier.generator.enums.CodeFileName;
import com.sms.courier.generator.pojo.CodeEntityParamVo;
import com.sms.courier.generator.pojo.CodegenEntityVo;
import com.sms.courier.generator.pojo.StringFile;
import com.sms.courier.utils.CustomStringUtil;
import java.util.List;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.compress.utils.Lists;
import org.apache.commons.lang3.StringUtils;


@Slf4j
public abstract class AbstractCodegen {

    private final TemplateEngine templateEngine;
    private final Map<ParamType, String> typeMapping;
    private final String suffix;

    protected AbstractCodegen(TemplateEngine templateEngine,
        Map<ParamType, String> typeMapping, String suffix) {
        this.templateEngine = templateEngine;
        this.typeMapping = typeMapping;
        this.suffix = suffix;
    }

    public List<StringFile> generateParamObject(List<CodeEntityParamVo> infoList, String className, String packageName,
        CodeTemplate codeTemplate) {
        List<StringFile> fileList = Lists.newArrayList();
        for (CodeEntityParamVo paramModel : infoList) {
            if (ParamType.ARRAY.equals(paramModel.getOldParamType())) {
                paramModel.setParamType(replaceDefaultList(typeMapping.get(paramModel.getOldParamType())));
                setArrayModel(paramModel, fileList, packageName, codeTemplate);
            } else if (ParamType.OBJECT.equals(paramModel.getOldParamType())) {
                paramModel.setParamType(CustomStringUtil.formatFirstUpperCase(paramModel.getKey()));
                setObjectModel(paramModel, fileList, packageName, codeTemplate);
            } else {
                paramModel.setParamType(typeMapping.get(paramModel.getOldParamType()));
            }
        }
        //最外层model
        CodegenEntityVo entityModel = CodegenEntityVo.builder()
            .packageName(packageName)
            .classPackage(CodeFileName.ENTITY.getName())
            .className(CustomStringUtil.formatFirstUpperCase(className))
            .params(infoList)
            .build();

        setFile(entityModel, fileList, codeTemplate);
        return fileList;
    }

    private void setArrayModel(CodeEntityParamVo paramModel, List<StringFile> fileList, String packageName,
        CodeTemplate codeTemplate) {
        for (CodeEntityParamVo paramInfo : paramModel.getChildParam()) {
            if (ParamType.OBJECT.equals(paramInfo.getOldParamType())) {
                paramInfo.setKey(StringUtils.isBlank(paramInfo.getKey())
                    ? org.thymeleaf.util.StringUtils.randomAlphanumeric(3)
                    : CustomStringUtil.formatFirstUpperCase(paramInfo.getKey()));
                paramInfo.setParamType(paramModel.getKey());
                paramModel.setParamType(CustomStringUtil.formatFirstUpperCase(
                    String.format(typeMapping.get(paramModel.getOldParamType()), paramInfo.getKey())));
                setObjectModel(paramInfo, fileList, packageName, codeTemplate);
            } else {
                paramModel
                    .setParamType(String.format(typeMapping.get(paramModel.getOldParamType()),
                        replaceString(typeMapping.get(paramInfo.getOldParamType()))));
            }
        }
    }

    private void setObjectModel(CodeEntityParamVo paramModel, List<StringFile> fileList, String packageName,
        CodeTemplate codeTemplate) {
        List<CodeEntityParamVo> paramModelList = Lists.newArrayList();
        for (CodeEntityParamVo paramInfo : paramModel.getChildParam()) {
            if (ParamType.OBJECT.equals(paramInfo.getOldParamType())) {
                paramInfo.setKey(StringUtils.isBlank(paramInfo.getKey())
                    ? org.thymeleaf.util.StringUtils.randomAlphanumeric(5)
                    : CustomStringUtil.formatFirstUpperCase(paramInfo.getKey()));
                paramModel.setKey(CustomStringUtil.formatFirstUpperCase(paramInfo.getKey()));
                paramModel.setParamType(CustomStringUtil.formatFirstUpperCase(String.format(paramModel.getParamType(),
                    paramInfo.getKey())));
                setObjectModel(paramInfo, fileList, packageName, codeTemplate);
            } else if (ParamType.ARRAY.equals(paramInfo.getOldParamType())) {
                setArrayModel(paramInfo, fileList, packageName, codeTemplate);
            } else {
                paramInfo.setParamType(typeMapping.get(paramInfo.getOldParamType()));
            }
            paramModelList.add(paramInfo);
        }
        CodegenEntityVo entityModel = CodegenEntityVo.builder()
            .packageName(packageName)
            .classPackage(CodeFileName.ENTITY.getName())
            .className(CustomStringUtil.formatFirstUpperCase(paramModel.getKey()))
            .params(paramModelList)
            .build();
        setFile(entityModel, fileList, codeTemplate);
    }

    protected void setFile(BaseCodegen entityModel, List<StringFile> fileList, CodeTemplate codeTemplate) {
        String contents = templateEngine.getRendered(entityModel, codeTemplate);
        fileList.add(StringFile.builder().fileName(entityModel.getClassName() + suffix).fileContents(contents).build());
    }

    protected String replaceString(String paramType) {
        return paramType.replace("?", "");
    }

    protected String replaceDefaultList(String paramType) {
        return paramType.replace("<%s>", "");
    }


}
