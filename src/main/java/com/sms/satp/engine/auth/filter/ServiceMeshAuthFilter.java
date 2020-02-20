
package com.sms.satp.engine.auth.filter;

import static io.restassured.RestAssured.given;

import com.sms.satp.engine.auth.ApiAuth;
import com.sms.satp.engine.auth.AuthType;
import com.sms.satp.engine.auth.ServiceMeshAuthConfig;
import io.restassured.config.LogConfig;
import io.restassured.filter.FilterContext;
import io.restassured.filter.log.LogDetail;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.response.Response;
import io.restassured.specification.FilterableRequestSpecification;
import io.restassured.specification.FilterableResponseSpecification;
import io.restassured.specification.RequestSpecification;
import io.restassured.spi.AuthFilter;
import java.net.URI;
import lombok.SneakyThrows;
import org.springframework.http.HttpStatus;

@ApiAuth(type = AuthType.SERVICE_MESH)
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
        RequestSpecification loginRequestSpec = given().auth().preemptive().basic(userName, password);

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
        if (loginResponse.statusCode() == HttpStatus.OK.value()) {
            requestSpec.auth().oauth2(loginResponse.asString());
            return ctx.next(requestSpec, responseSpec);
        } else {
            return loginResponse;
        }
        // Don't send the detailed cookies because they contain too many detail (such as Path which is a reserved token)
        // requestSpec.header()

    }


}