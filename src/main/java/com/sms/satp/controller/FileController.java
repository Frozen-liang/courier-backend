package com.sms.satp.controller;

import com.sms.satp.common.constant.Constants;
import com.sms.satp.dto.request.TestFileRequest;
import com.sms.satp.service.FileService;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.gridfs.GridFsResource;
import org.springframework.stereotype.Controller;
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

    @PostMapping("/upload")
    @ResponseBody
    public Boolean insertTestFile(TestFileRequest testFileRequest) {
        return fileService.insertTestFile(testFileRequest);
    }

    @PutMapping
    @ResponseBody
    public Boolean updateTestFile(TestFileRequest testFileRequest) {
        return fileService.updateTestFile(testFileRequest);
    }

    @GetMapping("/download/{id}")
    @ResponseBody
    public void downloadTestFile(@PathVariable("id") ObjectId id) {
        GridFsResource gridFsResource = fileService.downloadTestFile(id);
    }
}
