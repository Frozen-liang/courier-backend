package com.sms.satp.service.impl;

import com.sms.satp.dto.ApiImportRequest;
import com.sms.satp.entity.api.ApiEntity;
import com.sms.satp.entity.project.ProjectEntity;
import com.sms.satp.parser.ApiDocumentTransformer;
import com.sms.satp.parser.DocumentReader;
import com.sms.satp.parser.common.DocumentParserResult;
import com.sms.satp.parser.common.DocumentType;
import com.sms.satp.repository.ApiRepository;
import com.sms.satp.repository.ProjectEntityRepository;
import com.sms.satp.service.ApiService;
import com.sms.satp.utils.ExceptionUtils;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Objects;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class ApiServiceImpl implements ApiService {

    private final ProjectEntityRepository projectEntityRepository;
    private final ApiRepository apiRepository;

    public ApiServiceImpl(ProjectEntityRepository projectEntityRepository,
        ApiRepository apiRepository) {
        this.projectEntityRepository = projectEntityRepository;
        this.apiRepository = apiRepository;
    }

    @Override
    public boolean importDocument(ApiImportRequest apiImportRequest) {
        DocumentType documentType = DocumentType.resolve(apiImportRequest.getDocumentType());
        DocumentReader reader = documentType.getReader();
        ApiDocumentTransformer transformer = documentType.getTransformer();
        String documentUrl = apiImportRequest.getDocumentUrl();
        MultipartFile file = apiImportRequest.getFile();
        DocumentParserResult content = getDocumentParserResult(reader, documentUrl, file);
        ProjectEntity projectEntity = projectEntityRepository.insert(transformer.toProjectEntity(content));
        List<ApiEntity> apiEntities = transformer.toApiEntities(content, projectEntity.getId());
        apiRepository.insert(apiEntities);
        return true;
    }

    private DocumentParserResult getDocumentParserResult(DocumentReader reader, String documentUrl,
        MultipartFile file) {
        DocumentParserResult content;
        if (StringUtils.isNotBlank(documentUrl)) {
            content = reader.readLocation(documentUrl);
        } else if (Objects.nonNull(file)) {
            try {
                content = reader.readContents(IOUtils.toString(file.getInputStream(), StandardCharsets.UTF_8));
            } catch (Exception e) {
                throw ExceptionUtils.mpe("Failed to read the file in DocumentType", e);
            }
        } else {
            throw ExceptionUtils
                .mpe("Not in the supported range(DocumentType=%s)", new Object[]{DocumentType.values()});
        }
        return content;
    }
}
