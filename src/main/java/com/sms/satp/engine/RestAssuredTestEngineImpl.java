package com.sms.satp.engine;

import static io.restassured.RestAssured.given;

import com.sms.satp.engine.model.ApiUnitRequest;
import com.sms.satp.engine.model.ApiUnitResponse;
import com.sms.satp.engine.model.MultiPart;
import com.sms.satp.engine.model.impl.DefaultApiUnitResponse;
import com.sms.satp.parser.common.HttpMethod;
import io.restassured.builder.MultiPartSpecBuilder;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.response.Response;
import io.restassured.specification.RequestSender;
import io.restassured.specification.RequestSpecification;
import java.io.File;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Function;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class RestAssuredTestEngineImpl implements TestEngine {

    private static final Function<ApiUnitRequest, RequestSpecification> REQUEST_SPEC_BUILDER_CONVERTER =
        apply();
    private static final ResponseSpecBuilder RESPONSE_SPEC_BUILDER =
        new ResponseSpecBuilder().log(LogDetail.ALL);


    @Override
    public ApiUnitResponse execute(ApiUnitRequest request) {
        RequestSpecification requestSpecification = REQUEST_SPEC_BUILDER_CONVERTER.apply(request);
        Response response = dispatch(requestSpecification, request.getPath(), request.getHttpMethod());
        if (log.isDebugEnabled()) {

            log.debug("API {} execution result, {}", request.getPath(), response.getBody().asString());
        }

        return new DefaultApiUnitResponse(response);
    }

    private Response dispatch(RequestSpecification requestSpecification, String path, HttpMethod method) {

        RequestSender sender = given(requestSpecification, RESPONSE_SPEC_BUILDER.build());
        log.debug("API {} begin execution", path);
        return method.getExecutor().unchecked().apply(path, sender);
    }

    private static Function<ApiUnitRequest, RequestSpecification> apply() {
        return apiUnitRequest -> {
            final RequestSpecBuilder builder = new RequestSpecBuilder();
            ifNeedAdd(apiUnitRequest.getServerAddress(), builder::setBaseUri);
            ifNeedAdd(apiUnitRequest.getCookies(), builder::addCookies);
            ifNeedAdd(apiUnitRequest.getQueryParams(), builder::addQueryParams);
            ifNeedAdd(apiUnitRequest.getFormParams(), builder::addFormParams);
            ifNeedAdd(apiUnitRequest.getPathParams(), builder::addPathParams);
            ifNeedAdd(apiUnitRequest.getMultiParts(), (multiParts) -> addMultiPartSpecification(multiParts,
                builder));
            builder.log(LogDetail.ALL);
            RequestSpecification requestSpecification = builder.build();
            ifNeedAdd(apiUnitRequest.getBody(), (body) -> buildBody(requestSpecification, body));
            ifNeedAdd(apiUnitRequest.getHeaders(), requestSpecification::headers);
            return requestSpecification;
        };
    }

    private static void buildBody(RequestSpecification requestSpecification, Object body) {
        if (body instanceof File) {
            requestSpecification.body((File) body);
        } else {
            requestSpecification.body(body.toString());
        }
    }

    private static <T> void ifNeedAdd(T t, Consumer<T> consumer) {
        if (Objects.nonNull(t)) {
            consumer.accept(t);
        }
    }

    private static void addMultiPartSpecification(List<MultiPart> multiParts, RequestSpecBuilder builder) {
        for (MultiPart multiPart : multiParts) {
            MultiPartSpecBuilder multiPartSpecBuilder = new MultiPartSpecBuilder(multiPart.getFile());
            if (StringUtils.isNotBlank(multiPart.getControlName())) {
                multiPartSpecBuilder.controlName(multiPart.getControlName());
            }
            Charset charset = Objects.nonNull(multiPart.getCharset()) ? multiPart.getCharset() : StandardCharsets.UTF_8;
            multiPartSpecBuilder.charset(charset);
            String mimeType = StringUtils.isNotBlank(multiPart.getMimeType()) ? multiPart.getMimeType() : "*";
            multiPartSpecBuilder.mimeType(mimeType);
            multiPartSpecBuilder.fileName(multiPart.getFile().getName());
            builder.addMultiPart(multiPartSpecBuilder.build());
        }
    }


}
