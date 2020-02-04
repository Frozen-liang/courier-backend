package com.sms.satp.parser;

import com.sms.satp.parser.model.ApiDocument;
import com.sms.satp.parser.model.ApiInfo;
import com.sms.satp.parser.model.ApiPath;
import com.sms.satp.parser.model.ApiTag;
import com.sms.satp.parser.schema.ApiSchema;
import java.util.List;
import java.util.Map;

public interface DocumentTransformer<T> extends DocumentReader<T> {

    default ApiDocument build(String location) {
        T document = read(location);
        return ApiDocument.builder().info(transformInfo(document)).paths(transformPaths(document))
            .schemas(transformSchemas(document))
            .tags(transformTags(document)).build();

    }

    public ApiInfo transformInfo(T t);

    public List<ApiTag> transformTags(T t);

    public List<ApiPath> transformPaths(T t);

    public Map<String, ApiSchema> transformSchemas(T t);
}
