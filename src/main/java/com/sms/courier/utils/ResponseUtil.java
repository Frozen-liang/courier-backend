package com.sms.courier.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sms.courier.common.response.Response;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Objects;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.core.io.InputStreamSource;
import org.springframework.http.MediaType;

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

    public static void write(HttpServletResponse response, InputStream inputStream) {
        if (Objects.isNull(inputStream) || Objects.isNull(response)) {
            log.warn("Response data was null.");
            return;
        }
        try {
            ServletOutputStream os = response.getOutputStream();
            IOUtils.copy(inputStream, os);
            os.flush();
        } catch (IOException e) {
            log.error("Write io stream error!", e);
        } finally {
            close(inputStream);
        }
    }

    public static void export(HttpServletResponse response, Workbook workbook, String filename) {
        if (Objects.isNull(workbook) || Objects.isNull(response)) {
            log.warn("Response data was null.");
            return;
        }
        checkFilename(filename);
        try {
            ServletOutputStream outputStream = response.getOutputStream();
            setHeader(response, filename);
            workbook.write(outputStream);
            outputStream.flush();
        } catch (Exception e) {
            log.error("[Exception] ResponseUtil-out: ", e);
        } finally {
            close(workbook);
        }
    }

    public static void export(HttpServletResponse response, InputStream inputStream, String filename) {
        if (Objects.isNull(inputStream) || Objects.isNull(response)) {
            log.warn("Response data was null.");
            return;
        }
        checkFilename(filename);
        try {
            ServletOutputStream os = response.getOutputStream();
            setHeader(response, filename);
            IOUtils.copy(inputStream, os);
            os.flush();
        } catch (IOException e) {
            log.error("Write io stream error!", e);
        } finally {
            close(inputStream);
        }
    }

    public static void export(HttpServletResponse response, InputStream inputStream, String filename,
        String contentType) {
        if (Objects.isNull(inputStream) || Objects.isNull(response)) {
            log.warn("Response data was null.");
            return;
        }
        checkFilename(filename);
        try {
            ServletOutputStream os = response.getOutputStream();
            setHeader(response, filename, contentType);
            IOUtils.copy(inputStream, os);
            os.flush();
        } catch (IOException e) {
            log.error("Write io stream error!", e);
        } finally {
            close(inputStream);
        }
    }

    public static InputStream getInputStream(InputStreamSource inputStreamResource) {
        try {
            return inputStreamResource.getInputStream();
        } catch (IOException e) {
            return null;
        }
    }


    private static void setHeader(HttpServletResponse response, String filename) {
        setHeader(response, filename, null);
    }

    private static void setHeader(HttpServletResponse response, String filename, String contextType) {
        contextType = StringUtils.defaultString(contextType, MediaType.APPLICATION_OCTET_STREAM_VALUE);
        response.setContentType(contextType);
        response.setHeader("Content-Disposition",
            "attachment;filename=" + URLEncoder.encode(filename, StandardCharsets.UTF_8));
    }

    private static void checkFilename(String filename) {
        Assert.isFalse(StringUtils.isBlank(filename), "The filename must not empty!");
    }

    public static void close(Closeable stream) {
        if (stream != null) {
            try {
                stream.close();
            } catch (IOException e) {
                log.error("Close stream error!");
            }
        }
    }
}