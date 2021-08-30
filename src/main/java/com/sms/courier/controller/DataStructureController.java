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
    public Boolean add(@Validated(InsertGroup.class) @RequestBody DataStructureRequest dataStructureRequest) {
        return dataStructureService.add(dataStructureRequest);
    }

    @PutMapping
    public Boolean edit(@Validated(UpdateGroup.class) @RequestBody DataStructureRequest dataStructureRequest) {
        return dataStructureService.edit(dataStructureRequest);
    }

    @PostMapping("/list")
    public List<DataStructureListResponse> list(@RequestBody DataStructureListRequest request) {
        return dataStructureService.getDataStructureList(request);
    }

    @PostMapping("/data/list")
    public List<DataStructureResponse> dataList(@RequestBody DataStructureListRequest request) {
        return dataStructureService.getDataStructureDataList(request);
    }

    @DeleteMapping("/{ids}")
    public Boolean delete(@PathVariable List<String> ids) {
        return dataStructureService.delete(ids);
    }

    @GetMapping("/getReference/{id}")
    public List<DataStructureReferenceResponse> getReference(@PathVariable String id) {
        return dataStructureService.getReference(id);
    }
}