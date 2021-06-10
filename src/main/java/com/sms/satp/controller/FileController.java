package com.sms.satp.controller;

import static org.springframework.http.MediaType.APPLICATION_OCTET_STREAM_VALUE;

import com.sms.satp.common.constant.Constants;
import com.sms.satp.common.validate.InsertGroup;
import com.sms.satp.common.validate.UpdateGroup;
import com.sms.satp.dto.request.TestFileRequest;
import com.sms.satp.dto.response.FileInfoResponse;
import com.sms.satp.service.FileService;
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
import org.springframework.data.mongodb.gridfs.GridFsResource;
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

    public FileController(FileService fileService) {
        this.fileService = fileService;
    }

    @GetMapping("/list/{projectId}")
    @ResponseBody
    public List<FileInfoResponse> list(@PathVariable("projectId") ObjectId projectId) {
        return fileService.list(projectId);
    }

    @PostMapping("/upload")
    @ResponseBody
    public Boolean insertTestFile(@Validated(InsertGroup.class) TestFileRequest testFileRequest) {
        return fileService.insertTestFile(testFileRequest);
    }

    @PutMapping
    @ResponseBody
    public Boolean updateTestFile(@Validated(UpdateGroup.class) TestFileRequest testFileRequest) {
        return fileService.updateTestFile(testFileRequest);
    }

    @SneakyThrows({IOException.class, IllegalStateException.class})
    @GetMapping(value = "/download/{id}", produces = APPLICATION_OCTET_STREAM_VALUE)
    public void downloadTestFile(@PathVariable("id") ObjectId id, HttpServletResponse response) {
        GridFsResource gridFsResource = fileService.downloadTestFile(id);
        String filename = gridFsResource.getFilename();
        response.setContentType(gridFsResource.getContentType());
        response.setHeader("Content-Disposition",
            "attachment;filename=" + URLEncoder
                .encode(StringUtils.isEmpty(filename) ? ObjectId.get().toString() : filename, StandardCharsets.UTF_8));
        ServletOutputStream os = null;
        InputStream is = null;
        try {
            os = response.getOutputStream();
            is = gridFsResource.getInputStream();
            IOUtils.copy(is, os);
            os.flush();
        } finally {
            close(is);
            close(os);
        }
    }

    @DeleteMapping("/{id}")
    @ResponseBody
    public Boolean delete(@PathVariable("id") String id) {
        return fileService.deleteTestFileById(id);
    }

    @SneakyThrows(IOException.class)
    private void close(Closeable stream) {
        if (stream != null) {
            stream.close();
        }
    }
}
