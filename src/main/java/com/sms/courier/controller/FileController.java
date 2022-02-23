package com.sms.courier.controller;

import com.sms.courier.common.constant.Constants;
import com.sms.courier.common.validate.InsertGroup;
import com.sms.courier.common.validate.UpdateGroup;
import com.sms.courier.dto.request.TestFileRequest;
import com.sms.courier.dto.response.FileInfoResponse;
import com.sms.courier.service.FileService;
import com.sms.courier.utils.ResponseUtil;
import java.util.List;
import javax.servlet.http.HttpServletResponse;
import org.bson.types.ObjectId;
import org.springframework.core.io.ClassPathResource;
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
    private static final String FILENAME = "data-coll-template.xls";

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
        ResponseUtil.export(response, ResponseUtil.getInputStream(gridFsResource), gridFsResource.getFilename(),
            gridFsResource.getContentType());
    }

    @GetMapping(value = "/download/data-collection-template")
    @PreAuthorize("hasRoleOrAdmin(@role.DATA_COLLECTION_CRE_UPD_DEL)")
    public void downloadDataCollectionTemplate(HttpServletResponse response) {
        final ClassPathResource classPathResource = new ClassPathResource("template/data-collection.xls");
        ResponseUtil.export(response, ResponseUtil.getInputStream(classPathResource), FILENAME);
    }


    @GetMapping(value = "/stream/{id}")
    public void getOutputStream(@PathVariable("id") String id, HttpServletResponse response) {
        GridFsResource gridFsResource = fileService.downloadTestFile(id);
        response.setContentType(gridFsResource.getContentType());
        response.setHeader("filename", gridFsResource.getFilename());
        ResponseUtil.write(response, ResponseUtil.getInputStream(gridFsResource));
    }

    @DeleteMapping("/{id}")
    @ResponseBody
    @PreAuthorize("hasRoleOrAdmin(@role.FILE_CRE_UPD_DEL)")
    public Boolean delete(@PathVariable("id") String id) {
        return fileService.deleteTestFileById(id);
    }

}
