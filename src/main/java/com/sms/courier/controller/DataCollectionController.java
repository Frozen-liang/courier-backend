package com.sms.courier.controller;

import static com.sms.courier.common.constant.Constants.DATA_COLLECTION_PATH;

import com.sms.courier.common.validate.InsertGroup;
import com.sms.courier.common.validate.UpdateGroup;
import com.sms.courier.dto.request.DataCollectionImportRequest;
import com.sms.courier.dto.request.DataCollectionRequest;
import com.sms.courier.dto.response.DataCollectionResponse;
import com.sms.courier.service.DataCollectionService;
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
    @PreAuthorize("hasRoleOrAdmin(@role.DATA_COLLECTION_CRE_UPD_DEL)")
    public Boolean add(@Validated(InsertGroup.class) @RequestBody DataCollectionRequest dataCollectionRequest) {
        return dataCollectionService.add(dataCollectionRequest);
    }

    @PutMapping
    @PreAuthorize("hasRoleOrAdmin(@role.DATA_COLLECTION_CRE_UPD_DEL)")
    public Boolean edit(@Validated(UpdateGroup.class) @RequestBody DataCollectionRequest dataCollectionRequest) {
        return dataCollectionService.edit(dataCollectionRequest);
    }

    @GetMapping("/list/{projectId}")
    @PreAuthorize("hasRoleOrAdmin(@role.DATA_COLLECTION_QUERY_ALL)")
    public List<DataCollectionResponse> list(@PathVariable String projectId, String collectionName, String groupId) {
        return dataCollectionService.list(projectId, collectionName, groupId);
    }

    @GetMapping("/param-list/{id}")
    public List<String> getParamListById(@PathVariable String id) {
        return dataCollectionService.getParamListById(id);
    }

    @DeleteMapping("/{ids}")
    @PreAuthorize("hasRoleOrAdmin(@role.DATA_COLLECTION_CRE_UPD_DEL)")
    public Boolean delete(@PathVariable List<String> ids) {
        return dataCollectionService.delete(ids);
    }

    @PostMapping("/import")
    public Boolean importDataCollection(@Validated DataCollectionImportRequest request) {
        return dataCollectionService.importDataCollection(request);
    }

    @GetMapping("/list/env/{envId}/{projectId}")
    @PreAuthorize("hasRoleOrAdmin(@role.DATA_COLLECTION_QUERY_ALL)")
    public List<DataCollectionResponse> listByEnvId(@PathVariable String envId, @PathVariable String projectId) {
        return dataCollectionService.listByEnvIdAndEnvIdIsNull(envId, projectId);
    }

}