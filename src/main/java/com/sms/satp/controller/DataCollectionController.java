package com.sms.satp.controller;

import static com.sms.satp.common.constant.Constants.DATA_COLLECTION_PATH;

import com.sms.satp.common.validate.InsertGroup;
import com.sms.satp.common.validate.UpdateGroup;
import com.sms.satp.dto.request.DataCollectionImportRequest;
import com.sms.satp.dto.request.DataCollectionRequest;
import com.sms.satp.dto.response.DataCollectionResponse;
import com.sms.satp.service.DataCollectionService;
import java.util.List;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping(DATA_COLLECTION_PATH)
public class DataCollectionController {

    private final DataCollectionService dataCollectionService;

    public DataCollectionController(DataCollectionService dataCollectionService) {
        this.dataCollectionService = dataCollectionService;
    }

    @GetMapping("/{id}")
    public DataCollectionResponse getById(@PathVariable("id") String id) {
        return dataCollectionService.findById(id);
    }

    @PostMapping
    @PreAuthorize("hasRole(@role.DATA_COLLECTION_CRE_UPD_DEL)")
    public Boolean add(@Validated(InsertGroup.class) @RequestBody DataCollectionRequest dataCollectionRequest) {
        return dataCollectionService.add(dataCollectionRequest);
    }

    @PutMapping
    @PreAuthorize("hasRole(@role.DATA_COLLECTION_CRE_UPD_DEL)")
    public Boolean edit(@Validated(UpdateGroup.class) @RequestBody DataCollectionRequest dataCollectionRequest) {
        return dataCollectionService.edit(dataCollectionRequest);
    }

    @GetMapping("/list/{projectId}")
    @PreAuthorize("hasRole(@role.DATA_COLLECTION_QUERY_ALL)")
    public List<DataCollectionResponse> list(@PathVariable String projectId, String collectionName) {
        return dataCollectionService.list(projectId, collectionName);
    }

    @GetMapping("/param-list/{id}")
    public List<String> getParamListById(@PathVariable String id) {
        return dataCollectionService.getParamListById(id);
    }

    @DeleteMapping("/{ids}")
    @PreAuthorize("hasRole(@role.DATA_COLLECTION_CRE_UPD_DEL)")
    public Boolean delete(@PathVariable List<String> ids) {
        return dataCollectionService.delete(ids);
    }

    @PostMapping("/import")
    public Boolean importDataCollection(@Validated DataCollectionImportRequest request) {
        return dataCollectionService.importDataCollection(request);
    }

}