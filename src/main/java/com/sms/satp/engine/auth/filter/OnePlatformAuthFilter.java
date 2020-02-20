
package com.sms.satp.engine.auth.filter;

import static com.sms.satp.engine.auth.OnePlatformAuthConfig.IS_SUPPORT_DST;
import static com.sms.satp.engine.auth.OnePlatformAuthConfig.UTC_OFFSET;
import static io.restassured.RestAssured.given;
import static io.restassured.path.xml.XmlPath.CompatibilityMode.HTML;
import static java.lang.String.format;

import com.google.common.collect.Lists;
import com.sms.satp.engine.auth.ApiAuth;
import com.sms.satp.engine.auth.AuthType;
import com.sms.satp.engine.auth.OnePlatformAuthConfig;
import io.restassured.config.LogConfig;
import io.restassured.filter.FilterContext;
import io.restassured.filter.log.LogDetail;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.path.xml.XmlPath;
import io.restassured.response.Response;
import io.restassured.specification.FilterableRequestSpecification;
import io.restassured.specification.FilterableResponseSpecification;
import io.restassured.specification.RequestSpecification;
import io.restassured.spi.AuthFilter;
import io.vavr.Tuple2;
import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;
import lombok.SneakyThrows;
import org.apache.commons.lang3.StringUtils;

@ApiAuth(type = AuthType.ONE_PLATFORM)
public class OnePlatformAuthFilter implements AuthFilter {

    private static final String FIND_INPUT_VALUE_OF_INPUT_TAG_WITH_NAME = "html.depthFirst().grep { it.name() == "
        + "'input' && it.@name"
        + " == '%s' }.collect { it.@value }";

    private final OnePlatformAuthConfig formAuthConfig;

    public OnePlatformAuthFilter(OnePlatformAuthConfig formAuthConfig) {
        this.formAuthConfig = formAuthConfig;
    }


    @SneakyThrows
    @Override
    public Response filter(FilterableRequestSpecification requestSpec, FilterableResponseSpecification responseSpec,
        FilterContext ctx) {
        String userNameInputField = formAuthConfig.getUserNameInputField();
        String passwordInputField = formAuthConfig.getPasswordInputField();
        String userName = formAuthConfig.getUserName();
        String password = formAuthConfig.getPassword();
        String formAction = formAuthConfig.getFormAction();

        List<Tuple2<String, String>> additionalInputFields = Lists.newArrayList();
        Response response = ctx.send(given().spec(requestSpec).auth().none());
        XmlPath html = new XmlPath(HTML, response.asString());
        if (formAuthConfig.hasAdditionalInputFieldNames()) {
            additionalInputFields = formAuthConfig.getAdditionalInputFieldNames().stream().parallel()
                .map(name -> parserAdditionalFields(html, name))
                .collect(Collectors.toList());
        }
        formAction = formAction.startsWith("/") ? formAction :
            "/" + formAction;

        RequestSpecification loginRequestSpec = given().auth().none().and().cookies(response.cookies())
            .formParams(userNameInputField, userName, passwordInputField, password);
        URI uri = new URI(requestSpec.getURI());
        String loginUri =
            uri.getScheme() + "://" + uri.getHost() + (uri.getPort() == -1 ? "" : ":" + uri.getPort()) + formAction;

        if (formAuthConfig.isLoggingEnabled()) {
            LogConfig logConfig = new LogConfig();
            LogDetail logDetail = LogDetail.ALL;
            loginRequestSpec.filter(
                new RequestLoggingFilter(logDetail, logConfig.isPrettyPrintingEnabled(), logConfig.defaultStream(),
                    logConfig.shouldUrlEncodeRequestUri()));

            loginRequestSpec.filter(new ResponseLoggingFilter(logDetail, logConfig.isPrettyPrintingEnabled(),
                logConfig.defaultStream()));
        }

        additionalInputFields.forEach(tuple -> loginRequestSpec.formParam(tuple._1(), tuple._2()));

        final Response loginResponse = loginRequestSpec.post(loginUri);
        // Don't send the detailed cookies because they contain too many detail (such as Path which is a reserved token)
        requestSpec.cookies(loginResponse.getCookies());
        return ctx.next(requestSpec, responseSpec);
    }

    private Tuple2<String, String> parserAdditionalFields(XmlPath html, String name) {
        String value = html.getString(format(FIND_INPUT_VALUE_OF_INPUT_TAG_WITH_NAME, name));
        if (StringUtils.isBlank(value)) {
            if (name.equals(IS_SUPPORT_DST)) {
                value = String.valueOf(formAuthConfig.isSupportDst());
            } else if (name.equals(UTC_OFFSET)) {
                value = formAuthConfig.getUtcOffset();
            }
        }
        return new Tuple2<>(name, value);

    }


}