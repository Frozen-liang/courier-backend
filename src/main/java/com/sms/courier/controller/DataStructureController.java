package com.sms.courier.controller;


import static com.sms.courier.common.constant.Constants.DATA_STRUCTURE_PATE;

import com.sms.courier.common.validate.InsertGroup;
import com.sms.courier.common.validate.UpdateGroup;
import com.sms.courier.dto.request.DataStructureListRequest;
import com.sms.courier.dto.request.DataStructureRequest;
import com.sms.courier.dto.response.DataStructureListResponse;
import com.sms.courier.dto.response.DataStructureReferenceResponse;
import com.sms.courier.dto.response.DataStructureResponse;
import com.sms.courier.service.DataStructureService;
import java.util.List;
import java.util.Map;
import org.springframework.data.domain.Page;
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
@RequestMapping(DATA_STRUCTURE_PATE)
public class DataStructureController {

    private final DataStructureService dataStructureService;

    public DataStructureController(DataStructureService dataStructureService) {
        this.dataStructureService = dataStructureService;
    }

    @GetMapping("/{id}")
    public DataStructureResponse getById(@PathVariable("id") String id) {
        return dataStructureService.findById(id);
    }

    @PostMapping
    @PreAuthorize("hasRoleOrAdmin(@role.DATA_STRUCTURE_CREATE)")
    public Boolean add(@Validated(InsertGroup.class) @RequestBody DataStructureRequest dataStructureRequest) {
        return dataStructureService.add(dataStructureRequest);
    }

    @PutMapping
    @PreAuthorize("hasRoleOrAdmin(@role.DATA_STRUCTURE_UPDATE)")
    public Boolean edit(@Validated(UpdateGroup.class) @RequestBody DataStructureRequest dataStructureRequest) {
        return dataStructureService.edit(dataStructureRequest);
    }

    @PostMapping("/list")
    @PreAuthorize("hasRoleOrAdmin(@role.DATA_STRUCTURE_QUERY_ALL)")
    public Page<DataStructureListResponse> list(@RequestBody DataStructureListRequest request) {
        return dataStructureService.getDataStructureList(request);
    }

    @PostMapping("/data/list")
    public List<DataStructureResponse> dataList(@RequestBody DataStructureListRequest request) {
        return dataStructureService.getDataStructureDataList(request);
    }

    @PostMapping("/ref/list")
    public List<DataStructureResponse> refStructList(@RequestBody DataStructureListRequest request) {
        return dataStructureService.getRefStructList(request);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRoleOrAdmin(@role.DATA_STRUCTURE_DELETE)")
    public Boolean delete(@PathVariable String id) {
        return dataStructureService.delete(id);
    }

    @GetMapping("/getReference/{id}")
    public Map<String, List<DataStructureReferenceResponse>> getReference(@PathVariable String id) {
        return dataStructureService.getReference(id);
    }
}
