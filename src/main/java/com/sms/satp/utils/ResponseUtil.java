package com.sms.satp.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sms.satp.common.response.Response;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import java.io.PrintWriter;
import java.util.Objects;
import javax.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ResponseUtil {

    @SuppressFBWarnings
    public static void out(HttpServletResponse response, Response responseData, ObjectMapper mapper) {
        if (Objects.isNull(responseData) || Objects.isNull(mapper) || Objects.isNull(response)) {
            log.warn("Response data was null.");
            return;
        }
        try (PrintWriter out = response.getWriter()) {
            response.setCharacterEncoding("UTF-8");
            response.setContentType("application/json;charset=UTF-8");
            out.write(mapper.writeValueAsString(responseData));
            out.flush();
        } catch (Exception e) {
            log.error("[Exception] ResponseUtil-out: ", e);
        }
    }

}