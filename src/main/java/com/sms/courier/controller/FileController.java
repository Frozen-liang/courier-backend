package com.sms.courier.controller;

import com.sms.courier.common.constant.Constants;
import com.sms.courier.common.enums.Media;
import com.sms.courier.common.validate.InsertGroup;
import com.sms.courier.common.validate.UpdateGroup;
import com.sms.courier.dto.request.TestFileRequest;
import com.sms.courier.dto.response.FileInfoResponse;
import com.sms.courier.service.FileService;
import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import lombok.SneakyThrows;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.bson.types.ObjectId;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.data.mongodb.gridfs.GridFsResource;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping(Constants.FILE_PATH)
public class FileController {

    private final FileService fileService;
    private static final String FILENAME = "数据集导入模板.csv";

    public FileController(FileService fileService) {
        this.fileService = fileService;
    }

    @GetMapping("/list/{projectId}")
    @ResponseBody
    @PreAuthorize("hasRoleOrAdmin(@role.FILE_QUERY_ALL)")
    public List<FileInfoResponse> list(@PathVariable("projectId") ObjectId projectId) {
        return fileService.list(projectId);
    }

    @PostMapping("/upload")
    @ResponseBody
    @PreAuthorize("hasRoleOrAdmin(@role.FILE_CRE_UPD_DEL)")
    public String insertTestFile(@Validated(InsertGroup.class) TestFileRequest testFileRequest) {
        return fileService.insertTestFile(testFileRequest);
    }

    @PutMapping
    @ResponseBody
    @PreAuthorize("hasRoleOrAdmin(@role.FILE_CRE_UPD_DEL)")
    public Boolean updateTestFile(@Validated(UpdateGroup.class) TestFileRequest testFileRequest) {
        return fileService.updateTestFile(testFileRequest);
    }

    @GetMapping(value = "/download/{id}")
    @PreAuthorize("hasRoleOrAdmin(@role.FILE_CRE_UPD_DEL)")
    public void downloadTestFile(@PathVariable("id") String id, HttpServletResponse response) {
        GridFsResource gridFsResource = fileService.downloadTestFile(id);
        String filename = gridFsResource.getFilename();
        response.setContentType(gridFsResource.getContentType());
        response.setHeader("Content-Disposition",
            "attachment;filename=" + URLEncoder
                .encode(StringUtils.isEmpty(filename) ? ObjectId.get().toString() : filename, StandardCharsets.UTF_8));
        writeStream(response, gridFsResource);
    }

    @GetMapping(value = "/download/data-collection-template")
    @PreAuthorize("hasRoleOrAdmin(@role.DATA_COLLECTION_CRE_UPD_DEL)")
    public void downloadDataCollectionTemplate(HttpServletResponse response) {
        final ClassPathResource classPathResource = new ClassPathResource("template/data-collection.csv");
        response.setContentType(Media.APPLICATION_OCTET_STREAM.getType());
        response.setHeader("Content-Disposition",
            "attachment;filename=" + URLEncoder
                .encode(FILENAME, StandardCharsets.UTF_8));
        writeStream(response, classPathResource);
    }


    @GetMapping(value = "/stream/{id}")
    public void getOutputStream(@PathVariable("id") String id, HttpServletResponse response) {
        GridFsResource gridFsResource = fileService.downloadTestFile(id);
        response.setContentType(gridFsResource.getContentType());
        response.setHeader("filename", gridFsResource.getFilename());
        writeStream(response, gridFsResource);
    }

    @DeleteMapping("/{id}")
    @ResponseBody
    @PreAuthorize("hasRoleOrAdmin(@role.FILE_CRE_UPD_DEL)")
    public Boolean delete(@PathVariable("id") String id) {
        return fileService.deleteTestFileById(id);
    }

    @SneakyThrows(IOException.class)
    private void writeStream(HttpServletResponse response, Resource resource) {
        ServletOutputStream os = null;
        InputStream is = null;
        try {
            os = response.getOutputStream();
            is = resource.getInputStream();
            IOUtils.copy(is, os);
            os.flush();
        } finally {
            close(is);
            close(os);
        }
    }

    @SneakyThrows(IOException.class)
    private void close(Closeable stream) {
        if (stream != null) {
            stream.close();
        }
    }
}
