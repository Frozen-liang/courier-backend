package com.sms.satp.repository.impl;

import static com.sms.satp.common.exception.ErrorCode.EDIT_NOT_EXIST_ERROR;
import static com.sms.satp.common.field.CommonFiled.ID;
import static com.sms.satp.common.field.CommonFiled.PROJECT_ID;

import com.mongodb.client.gridfs.GridFSFindIterable;
import com.mongodb.client.gridfs.model.GridFSFile;
import com.sms.satp.dto.request.TestFileRequest;
import com.sms.satp.repository.CustomizedFileRepository;
import com.sms.satp.utils.ExceptionUtils;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import org.bson.Document;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.gridfs.GridFsResource;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;
import org.springframework.data.mongodb.gridfs.GridFsUpload;
import org.springframework.stereotype.Repository;
import org.springframework.web.multipart.MultipartFile;

@Repository
public class CustomizedFileRepositoryImpl implements CustomizedFileRepository {

    private final GridFsTemplate gridFsTemplate;

    public CustomizedFileRepositoryImpl(GridFsTemplate gridFsTemplate) {
        this.gridFsTemplate = gridFsTemplate;
    }


    @Override
    public List<GridFSFile> list(ObjectId projectId) {
        Query query = Query.query(Criteria.where("metadata.projectId").is(projectId));
        GridFSFindIterable gridFsFiles = gridFsTemplate.find(query);
        List<GridFSFile> list = new ArrayList<>();
        return gridFsFiles.into(list);
    }

    @Override
    public Boolean insertTestFile(TestFileRequest testFileRequest) throws IOException {
        MultipartFile testFile = testFileRequest.getTestFile();
        Document document = new Document();
        document.put(PROJECT_ID.getFiled(), testFileRequest.getProjectId());
        gridFsTemplate
            .store(testFile.getInputStream(), testFile.getOriginalFilename(), testFile.getContentType(), document);
        return true;
    }

    @Override
    public Boolean updateTestFile(TestFileRequest testFileRequest) throws IOException {
        ObjectId id = testFileRequest.getId();
        Query query = new Query();
        ID.is(id).ifPresent(query::addCriteria);
        GridFSFile gridFsFile = gridFsTemplate.findOne(query);
        if (Objects.isNull(gridFsFile)) {
            throw ExceptionUtils.mpe(EDIT_NOT_EXIST_ERROR, "TestFile", id.toString());
        }
        gridFsTemplate.delete(query);
        MultipartFile testFile = testFileRequest.getTestFile();
        GridFsUpload<ObjectId> gridFsUpload = GridFsUpload.fromStream(testFile.getInputStream())
            .id(id).contentType(Objects.requireNonNull(testFile.getContentType()))
            .filename(Objects.requireNonNull(testFile.getOriginalFilename()))
            .metadata(Objects.requireNonNull(gridFsFile.getMetadata())).build();
        gridFsTemplate.store(gridFsUpload);
        return true;
    }

    @Override
    public Boolean deleteTestFileById(String id) {
        Query query = new Query();
        ID.is(id).ifPresent(query::addCriteria);
        gridFsTemplate.delete(query);
        return Boolean.TRUE;
    }

    @Override
    public GridFsResource downloadTestFile(String id) {
        Query query = new Query();
        ID.is(id).ifPresent(query::addCriteria);
        GridFSFile gridFsFile = gridFsTemplate.findOne(query);
        if (Objects.isNull(gridFsFile)) {
            return null;
        }
        return gridFsTemplate.getResource(gridFsFile);
    }
}
