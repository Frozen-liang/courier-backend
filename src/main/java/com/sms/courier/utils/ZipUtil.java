package com.sms.courier.utils;

import com.sms.courier.common.exception.ErrorCode;
import com.sms.courier.generator.pojo.StringFile;
import com.sms.courier.generator.pojo.StringFiles;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Objects;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ZipUtil {

    private ZipUtil() {
    }

    public static void compressionFileByString(OutputStream outputStream, List<StringFiles> stringFiles,
        String packageName) {
        try (ZipOutputStream zos = new ZipOutputStream(outputStream)) {
            packageName = Objects.nonNull(packageName) ? packageName + File.separator : null;
            ZipEntry zipEntry = null;
            for (StringFiles files : stringFiles) {
                for (StringFile file : files.getFileList()) {
                    zipEntry = new ZipEntry(
                        packageName + files.getFilePackageName()
                            + File.separator + file.getFileName());
                    zos.putNextEntry(zipEntry);
                    byte[] strBuffer = file.getFileContents().getBytes(StandardCharsets.UTF_8);
                    zos.write(strBuffer, 0, strBuffer.length);
                }
            }
            zos.closeEntry();
        } catch (IOException e) {
            log.error("File compression error!");
            throw ExceptionUtils.mpe(ErrorCode.FILE_COMPRESSION_ERROE, e);
        }
    }

}
