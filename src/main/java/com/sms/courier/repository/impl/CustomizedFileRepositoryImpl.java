package com.sms.courier.repository.impl;

import static com.sms.courier.common.exception.ErrorCode.EDIT_NOT_EXIST_ERROR;
import static com.sms.courier.common.field.CommonField.ID;
import com.mongodb.client.gridfs.GridFSFindIterable;
import com.mongodb.client.gridfs.model.GridFSFile;
import com.sms.courier.entity.file.FileInfoEntity;
import com.sms.courier.repository.CustomizedFileRepository;
import com.sms.courier.utils.ExceptionUtils;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
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
    @SuppressFBWarnings("BC_UNCONFIRMED_CAST_OF_RETURN_VALUE")
    public List<GridFSFile> list(ObjectId projectId) {
        Query query = Query.query(Criteria.where("metadata.projectId").is(projectId));
        GridFSFindIterable gridFsFiles = gridFsTemplate.find(query);
        List<GridFSFile> list = new ArrayList<>();
        return gridFsFiles.into(list);
    }

    @Override
    public String insertTestFile(MultipartFile multipartFile) {
        try {
            ObjectId id = gridFsTemplate.store(
                    multipartFile.getInputStream(),
                    multipartFile.getOriginalFilename(),
                    multipartFile.getContentType());
            return id.toString();
        } catch (IOException e) {
            throw ExceptionUtils.mpe("Get file InputStream error!");
        }
    }

    @Override
    @SuppressFBWarnings("NP_NULL_ON_SOME_PATH_FROM_RETURN_VALUE")
    public Boolean updateTestFile(FileInfoEntity fileInfo, MultipartFile multipartFile) {
        try {
            String id = fileInfo.getSourceId();
            Query query = new Query();
            ID.is(id).ifPresent(query::addCriteria);
            GridFSFile gridFsFile = gridFsTemplate.findOne(query);
            if (Objects.isNull(gridFsFile)) {
                throw ExceptionUtils.mpe(EDIT_NOT_EXIST_ERROR, "TestFile", id.toString());
            }
            gridFsTemplate.delete(query);
            InputStream inputStream = multipartFile.getInputStream();
            GridFsUpload<String> gridFsUpload = GridFsUpload.fromStream(inputStream)
                    .id(id).contentType(Objects.requireNonNull(multipartFile.getContentType()))
                    .filename(Objects.requireNonNull(multipartFile.getOriginalFilename())).build();
            gridFsTemplate.store(gridFsUpload);
            return true;
        } catch (IOException e) {
            throw ExceptionUtils.mpe("The file is error!");
        }

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

    @Override
    public GridFSFile findById(String id) {
        Query query = new Query();
        ID.is(id).ifPresent(query::addCriteria);
        return gridFsTemplate.findOne(query);
    }
}
