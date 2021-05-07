package com.sms.satp.controller;

import static com.sms.satp.common.constant.Constants.DATA_COLLECTION_PATH;

import com.sms.satp.common.response.Response;
import com.sms.satp.dto.DataCollectionDto;
import com.sms.satp.service.DataCollectionService;
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
@RequestMapping(DATA_COLLECTION_PATH)
public class DataCollectionController {

    private final DataCollectionService dataCollectionService;

    public DataCollectionController(DataCollectionService dataCollectionService) {
        this.dataCollectionService = dataCollectionService;
    }

    @GetMapping("/{id}")
    public Response<DataCollectionDto> getById(@PathVariable("id") String id) {
        return Response.ok(dataCollectionService.findById(id));
    }

    @PostMapping
    public Response<Boolean> add(@Validated @RequestBody DataCollectionDto dataCollectionDto) {
        dataCollectionService.add(dataCollectionDto);
        return Response.ok(Boolean.TRUE);
    }

    @PutMapping
    public Response<Boolean> edit(@Validated @RequestBody DataCollectionDto dataCollectionDto) {
        dataCollectionService.edit(dataCollectionDto);
        return Response.ok(Boolean.TRUE);
    }

    @GetMapping("/list/{projectId}")
    public Response<List<DataCollectionDto>> list(@PathVariable String projectId, String collectionName) {
        return Response.ok(dataCollectionService.list(projectId, collectionName));
    }

    @GetMapping("/param-list/{id}")
    public Response<List<String>> getParamListById(@PathVariable String id) {
        return Response.ok(dataCollectionService.getParamListById(id));
    }

    @DeleteMapping("/{ids}")
    public Response<Boolean> delete(@PathVariable String[] ids) {
        dataCollectionService.delete(ids);
        return Response.ok(Boolean.TRUE);
    }

}