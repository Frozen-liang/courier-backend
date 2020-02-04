package com.sms.satp.engine;

import static io.restassured.RestAssured.given;

import com.sms.satp.common.ApiTestPlatformException;
import com.sms.satp.common.ErrorCode;
import com.sms.satp.engine.model.ApiUnitRequest;
import com.sms.satp.engine.model.ApiUnitResponse;
import com.sms.satp.engine.model.MultiPart;
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
import java.util.function.Function;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
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
        return ApiUnitResponse.builder().time(response.time()).build();
    }

    private Response dispatch(RequestSpecification requestSpecification, String path, HttpMethod method) {

        RequestSender sender = given(requestSpecification, RESPONSE_SPEC_BUILDER.build());
        log.debug("API {} begin execution", path);

        switch (method) {
            case GET:
                return sender.get(path);
            case POST:
                return sender.post(path);
            case PUT:
                return sender.put(path);
            case DELETE:
                return sender.delete(path);
            default:
                throw new ApiTestPlatformException(ErrorCode.NOT_SUPPORT_METHOD);
        }
    }

    private static Function<ApiUnitRequest, RequestSpecification> apply() {
        return apiUnitRequest -> {
            RequestSpecBuilder builder = new RequestSpecBuilder();
            if (StringUtils.isNotBlank(apiUnitRequest.getServerAddress())) {
                builder.setBaseUri(apiUnitRequest.getServerAddress());
            }

            if (MapUtils.isNotEmpty(apiUnitRequest.getCookies())) {
                builder.addCookies(apiUnitRequest.getCookies());
            }
            if (MapUtils.isNotEmpty(apiUnitRequest.getQueryParams())) {
                builder.addQueryParams(apiUnitRequest.getQueryParams());
            }
            if (MapUtils.isNotEmpty(apiUnitRequest.getFormParams())) {
                builder.addFormParams(apiUnitRequest.getFormParams());
            }
            if (MapUtils.isNotEmpty(apiUnitRequest.getPathParams())) {
                builder.addPathParams(apiUnitRequest.getPathParams());
            }
            if (CollectionUtils.isNotEmpty(apiUnitRequest.getMultiParts())) {
                addMultiPartSpecification(apiUnitRequest.getMultiParts(), builder);
            }
            builder.log(LogDetail.ALL);
            RequestSpecification requestSpecification = builder.build();
            Object body = apiUnitRequest.getBody();
            if (Objects.nonNull(body)) {
                if (body instanceof File) {
                    requestSpecification.body((File) body);
                } else {
                    requestSpecification.body(body.toString());
                }
            }
            if (MapUtils.isNotEmpty(apiUnitRequest.getHeaders())) {
                requestSpecification.headers(apiUnitRequest.getHeaders());

            }
            return requestSpecification;
        };
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
