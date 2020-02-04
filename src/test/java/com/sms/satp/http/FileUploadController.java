package com.sms.satp.http;

import static java.util.Arrays.asList;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.http.MediaType.APPLICATION_OCTET_STREAM_VALUE;
import static org.springframework.http.MediaType.MULTIPART_FORM_DATA_VALUE;

import java.io.IOException;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
public class FileUploadController {

    @PostMapping(value = "/fileUpload", consumes = MULTIPART_FORM_DATA_VALUE, produces = APPLICATION_JSON_VALUE)
    public String fileUpload(@RequestParam MultipartFile file) {
        return "{ \"size\" : " + file.getSize() + ", \"name\" : \"" + file.getName() + "\" }";
    }

    @PostMapping(value = "/fileUpload2", consumes = MULTIPART_FORM_DATA_VALUE, produces = APPLICATION_JSON_VALUE)
    public @ResponseBody
    String fileUpload2(@RequestParam(value = "controlName") MultipartFile file) {
        return "{ \"size\" : " + file.getSize() + ", \"name\" : \"" + file.getName() + "\", \"originalName\" : \""
            + file.getOriginalFilename() + "\", \"mimeType\" : \"" + file.getContentType() + "\" }";
    }

    @PostMapping(value = "/multiFileUpload", consumes = MULTIPART_FORM_DATA_VALUE, produces = APPLICATION_JSON_VALUE)
    public List<FileDescription> multiFileUpload(@RequestParam(value = "controlName1") MultipartFile file1,
        @RequestParam(value = "controlName2") MultipartFile file2) throws IOException {
        FileDescription fd1 = new FileDescription();
        fd1.setContent(new String(file1.getBytes()));
        fd1.setName(file1.getName());
        fd1.setMimeType(file1.getContentType());
        fd1.setOriginalName(file1.getOriginalFilename());
        fd1.setSize(file1.getSize());

        FileDescription fd2 = new FileDescription();
        fd2.setContent(new String(file2.getBytes()));
        fd2.setName(file2.getName());
        fd2.setMimeType(file2.getContentType());
        fd2.setOriginalName(file2.getOriginalFilename());
        fd2.setSize(file2.getSize());
        return asList(fd1, fd2);
    }

    @PostMapping(value = "/fileUploadWithParam", consumes = MULTIPART_FORM_DATA_VALUE, produces = APPLICATION_JSON_VALUE)
    public FileWithParam fileUploadWithParam(@RequestParam(value = "controlName") MultipartFile file,
        @RequestParam(value = "param", required = false) String param) throws IOException {
        FileDescription fd1 = new FileDescription();
        fd1.setContent(new String(file.getBytes()));
        fd1.setName(file.getName());
        fd1.setMimeType(file.getContentType());
        fd1.setOriginalName(file.getOriginalFilename());
        fd1.setSize(file.getSize());

        FileWithParam fileWithParam = new FileWithParam();
        fileWithParam.setFile(fd1);
        fileWithParam.setParam(param);

        return fileWithParam;
    }

    @PostMapping(value = "/nonMultipartFileUpload", consumes = APPLICATION_OCTET_STREAM_VALUE, produces =
        APPLICATION_JSON_VALUE)
    public String nonMultipartFileUpload(@RequestBody String is) throws IOException {
        return "{ \"size\" : " + is.length() + ", \"content\":\"" + is + "\" }";
    }

    @PostMapping(value = "/fileUploadWithControlNameEqualToSomething", consumes = MULTIPART_FORM_DATA_VALUE,
        produces = APPLICATION_JSON_VALUE)
    public String fileUploadWithControlNameEqualToSomething(@RequestParam(value = "something") MultipartFile file) {
        return "{ \"size\" : " + file.getSize() + ", \"name\" : \"" + file.getName() + "\", \"originalName\" : \""
            + file.getOriginalFilename() + "\", \"mimeType\" : \"" + file.getContentType() + "\" }";
    }

    @PostMapping(value = "/textAndReturnHeader", consumes = "multipart/mixed", produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<String> fileUploadWithControlNameEqualToSomething(
        @RequestHeader("Content-Type") String requestContentType,
        @RequestParam(value = "something") MultipartFile file) {
        return ResponseEntity.ok().header(APPLICATION_JSON_VALUE).
            header("X-Request-Header", requestContentType).body(
            "{ \"size\" : " + file.getSize() + ", \"name\" : \"" + file.getName() + "\", \"originalName\" : \"" + file
                .getOriginalFilename() + "\", \"mimeType\" : \"" + file.getContentType() + "\" }");
    }

}