
package com.sms.satp.engine.auth.filter;

import static io.restassured.RestAssured.given;

import com.google.common.base.Joiner;
import com.sms.satp.engine.auth.ServiceMeshAuthConfig;
import io.restassured.config.LogConfig;
import io.restassured.filter.FilterContext;
import io.restassured.filter.log.LogDetail;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.http.Header;
import io.restassured.response.Response;
import io.restassured.specification.FilterableRequestSpecification;
import io.restassured.specification.FilterableResponseSpecification;
import io.restassured.specification.RequestSpecification;
import io.restassured.spi.AuthFilter;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import lombok.SneakyThrows;
import org.apache.commons.codec.binary.Base64;

public class ServiceMeshAuthFilter implements AuthFilter {

    public static final String AUTHORIZATION_HEADER = "Authorization";
    private final ServiceMeshAuthConfig serviceMeshAuthConfig;

    public ServiceMeshAuthFilter(ServiceMeshAuthConfig serviceMeshAuthConfig) {
        this.serviceMeshAuthConfig = serviceMeshAuthConfig;
    }


    @SneakyThrows
    @Override
    public Response filter(FilterableRequestSpecification requestSpec, FilterableResponseSpecification responseSpec,
        FilterContext ctx) {
        String userName = serviceMeshAuthConfig.getUserName();
        String password = serviceMeshAuthConfig.getPassword();
        String formAction = serviceMeshAuthConfig.getFormAction();

        formAction = formAction.startsWith("/") ? formAction : "/" + formAction;
        Header basicHeader = createBasicHeader(userName, password);
        RequestSpecification loginRequestSpec = given().auth().none().header(basicHeader);

        URI uri = new URI(requestSpec.getURI());
        String loginUri =
            uri.getScheme() + "://" + uri.getHost() + (uri.getPort() == -1 ? "" : ":" + uri.getPort()) + formAction;

        if (serviceMeshAuthConfig.isLoggingEnabled()) {
            LogConfig logConfig = new LogConfig();
            LogDetail logDetail = LogDetail.ALL;
            loginRequestSpec.filter(
                new RequestLoggingFilter(logDetail, logConfig.isPrettyPrintingEnabled(), logConfig.defaultStream(),
                    logConfig.shouldUrlEncodeRequestUri()));

            loginRequestSpec.filter(new ResponseLoggingFilter(logDetail, logConfig.isPrettyPrintingEnabled(),
                logConfig.defaultStream()));
        }

        final Response loginResponse = loginRequestSpec.post(loginUri);
        // Don't send the detailed cookies because they contain too many detail (such as Path which is a reserved token)
        // requestSpec.header()
        requestSpec.header(createJwtHeader(loginResponse.asString()));
        return ctx.next(requestSpec, responseSpec);
    }

    private static Header createBasicHeader(String userName, String password) {
        String auth = Joiner.on(":").join(userName, password);
        byte[] encodedAuth = Base64.encodeBase64(auth.getBytes(StandardCharsets.US_ASCII));
        String basic = Joiner.on(" ").join("Basic", new String(encodedAuth));
        return new Header(AUTHORIZATION_HEADER, basic);

    }

    private static Header createJwtHeader(String token) {
        String jwtToken = Joiner.on(" ").join("Bearer", token);
        return new Header(AUTHORIZATION_HEADER, jwtToken);
    }
}