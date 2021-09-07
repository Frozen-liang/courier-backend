package com.sms.courier.common.exception;

import com.sms.courier.common.response.Response;
import java.util.Objects;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.validation.BindException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {


    @ExceptionHandler(ApiTestPlatformException.class)
    public Response<?> customExceptionHandler(HttpServletRequest request, final ApiTestPlatformException e,
        HttpServletResponse response) {
        response.setStatus(HttpStatus.BAD_REQUEST.value());
        log.error("[Application=API Testing Platform][Exception Level=BUSINESS_ERROR]:", e);
        return Response.error(e.getCode(), e.getMessage());
    }


    @ExceptionHandler(RuntimeException.class)
    public Response<?> runtimeExceptionHandler(final Exception e,
        HttpServletResponse response) {
        response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
        log.error("[Application = API Testing Platform][Exception Level=INTERNAL_SERVER_ERROR]:", e);
        return Response.error(Integer.toString(HttpStatus.INTERNAL_SERVER_ERROR.value()),
            "Server error");
    }

    @ExceptionHandler(AccessDeniedException.class)
    public Response<?> runtimeExceptionHandler(final AccessDeniedException e,
        HttpServletResponse response) {
        response.setStatus(HttpStatus.FORBIDDEN.value());
        return Response.error(Integer.toString(HttpStatus.FORBIDDEN.value()),
            "Forbidden");
    }


    @Override
    @NonNull
    protected ResponseEntity<Object> handleExceptionInternal(@Nullable Exception ex, @Nullable Object body,
        @NonNull HttpHeaders headers, @NonNull HttpStatus status, @Nullable WebRequest request) {
        if (ex instanceof MethodArgumentNotValidException) {
            MethodArgumentNotValidException exception = (MethodArgumentNotValidException) ex;
            return new ResponseEntity<>(Response.error(Integer.toString(Objects.requireNonNull(status).value()),
                exception.getBindingResult().getAllErrors().get(0).getDefaultMessage()), status);
        }
        if (ex instanceof BindException) {
            BindException exception = (BindException) ex;
            return new ResponseEntity<>(Response.error(Integer.toString(Objects.requireNonNull(status).value()),
                exception.getBindingResult().getAllErrors().get(0).getDefaultMessage()), status);
        }
        if (ex instanceof MethodArgumentTypeMismatchException) {
            log.error("[Application=API Testing Platform][Exception Level=METHOD_ARGUMENT_ERROR]:", ex);
            return new ResponseEntity<>(
                Response.error(Integer.toString(status.value()), "Failed to convert argument"),
                status);
        }
        if (ex instanceof MissingServletRequestParameterException) {
            String parameterName = ((MissingServletRequestParameterException) ex).getParameterName();
            return new ResponseEntity<>(Response.error(Integer.toString(Objects.requireNonNull(status).value()),
                String.format("The %s must not be empty.", parameterName)), status);
        }
        log.error("[Application=API Testing Platform][Exception Level=INTERNAL_SERVER_ERROR]:", ex);
        return new ResponseEntity<>(
            Response.error(Integer.toString(status.value()), "Server error"),
            status);
    }

}
