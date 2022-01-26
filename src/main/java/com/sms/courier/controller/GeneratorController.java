package com.sms.courier.controller;

import com.sms.courier.common.constant.Constants;
import com.sms.courier.common.exception.ErrorCode;
import com.sms.courier.dto.request.CodeGenRequest;
import com.sms.courier.generator.pojo.FilePackageVo;
import com.sms.courier.generator.pojo.FileVo;
import com.sms.courier.service.GeneratorService;
import com.sms.courier.utils.ExceptionUtils;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Objects;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;
import javax.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping(Constants.GENERATE_CODE)
public class GeneratorController {

    private final GeneratorService generatorService;

    public GeneratorController(GeneratorService generatorService) {
        this.generatorService = generatorService;
    }


    @GetMapping("/download")
    public void downLoadCode(HttpServletResponse response, @Validated CodeGenRequest request) {
        List<FilePackageVo> fileVoList = generatorService.generator(request);
        zipFile(response, fileVoList, request.getPackageName());
    }

    private void zipFile(HttpServletResponse response, List<FilePackageVo> fileVoList, String packageName) {
        ZipOutputStream zos = null;
        try {
            zos = new ZipOutputStream(response.getOutputStream());
            packageName = Objects.nonNull(packageName) ? packageName + File.separator : null;
            ZipEntry zipEntry = null;
            for (FilePackageVo filePackageVo : fileVoList) {
                for (FileVo file : filePackageVo.getFileList()) {
                    zipEntry = new ZipEntry(
                        packageName + filePackageVo.getFilePackageName()
                            + File.separator + file.getFileName());
                    zos.putNextEntry(zipEntry);
                    byte[] strBuffer = file.getFileContents().getBytes(StandardCharsets.UTF_8);
                    zos.write(strBuffer, 0, strBuffer.length);
                }
            }
            response.setContentType("APPLICATION/OCTET-STREAM");
            response.setHeader("Content-Disposition", String.format("attachment; filename=\"%s-generated.zip\"",
                "code"));
        } catch (IOException e) {
            log.error("File compression error!");
            throw ExceptionUtils.mpe(ErrorCode.FILE_COMPRESSION_ERROE, e);
        } finally {
            if (Objects.nonNull(zos)) {
                try {
                    zos.closeEntry();
                    zos.close();
                } catch (IOException e) {
                    log.error("File compression error!", e);
                }
            }
        }
    }


}
