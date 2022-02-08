package com.sms.courier.generator;

import com.jayway.jsonpath.Configuration;
import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;
import com.jayway.jsonpath.Option;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Objects;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.compress.utils.Lists;

public class ReplaceHelper {

    static final Configuration conf = Configuration.defaultConfiguration().addOptions(Option.DEFAULT_PATH_LEAF_TO_NULL);
    private final DocumentContext documentContext;

    public ReplaceHelper(String content) {
        this.documentContext = JsonPath.using(conf).parse(content);
    }

    public ReplaceHelper replaceCustomStruct() {
        List<LinkedHashMap<String, Object>> customStructs = documentContext.read("$.[*]..customStructs[?(@.isRef)]");
        List<String> paramIds = Lists.newArrayList();
        List<LinkedHashMap<String, Object>> customParamInfo = Lists.newArrayList();
        for (LinkedHashMap<String, Object> paramInfo : customStructs) {
            if (paramIds.contains(paramInfo.get("paramId").toString())) {
                continue;
            }
            customParamInfo.add(paramInfo);
            paramIds.add(paramInfo.get("paramId").toString());
        }
        if (CollectionUtils.isNotEmpty(customParamInfo)) {
            for (LinkedHashMap<String, Object> paramInfo : customParamInfo) {
                String paramId = paramInfo.get("paramId").toString();

                //查找替换
                documentContext
                    .map("$.[*]..struct[?(@.paramId=='" + paramId + "')]",
                        ((currentValue, configuration) -> {
                            DocumentContext documentContext = JsonPath.parse(currentValue);
                            LinkedHashMap<String, Object> infoList = documentContext.json();
                            resetParamValue(infoList, paramInfo);
                            return infoList;

                        }));
            }
        }
        return this;
    }

    private void resetParamValue(LinkedHashMap<String, Object> info, LinkedHashMap<String, Object> customInfo) {
        if (Objects.equals(customInfo.get("paramId"), info.get("paramId"))) {
            info.put("key",
                Objects.nonNull(customInfo.get("key")) ? customInfo.get("key").toString() : info.get("key"));
            info.put("paramType", Objects.nonNull(customInfo.get("paramType"))
                ? Integer.parseInt(customInfo.get("paramType").toString()) : info.get("paramType"));
            info.put("description", Objects.nonNull(customInfo.get("description"))
                ? customInfo.get("description").toString() : info.get("description"));
            info.put("childParam", CollectionUtils.isNotEmpty((List) customInfo.get("childParam"))
                ? customInfo.get("childParam") : info.get("childParam"));
        }
    }

    public String toInfoList() {
        return documentContext.jsonString();
    }

}
