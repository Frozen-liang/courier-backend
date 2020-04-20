package com.sms.satp.controller;

import com.sms.satp.common.constant.Constants;
import com.sms.satp.common.response.Response;
import com.sms.satp.entity.dto.SelectDto;
import com.sms.satp.parser.common.MediaType;
import com.sms.satp.parser.common.SchemaType;
import com.sms.satp.service.ApiInterfaceService;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(Constants.COMMON_PATH)
public class CommonController {

    private final ApiInterfaceService apiInterfaceService;

    public CommonController(ApiInterfaceService apiInterfaceService) {
        this.apiInterfaceService = apiInterfaceService;
    }

    private static final List<SelectDto> SCHEMA_TYPE_SELECT_DTO_LIST =
        Arrays.stream(SchemaType.values()).map(schemaType -> SelectDto.builder()
            .id(schemaType.name())
            .name(schemaType.getType()).build()).collect(Collectors.toList());

    private static final List<SelectDto> MEDIA_TYPE_SELECT_DTO_LIST =
        Arrays.stream(MediaType.values()).map(mediaType -> SelectDto.builder()
            .id(mediaType.name())
            .name(mediaType.getType()).build()).collect(Collectors.toList());

    @GetMapping("schema-type")
    public Response<List<SelectDto>> getSchemaTypeSelect() {
        return Response.ok(SCHEMA_TYPE_SELECT_DTO_LIST);
    }

    @GetMapping("media-type")
    public Response<List<SelectDto>> getMediaTypeSelect() {
        return Response.ok(MEDIA_TYPE_SELECT_DTO_LIST);
    }

    @GetMapping("tags/{projectId}")
    public Response<List<SelectDto>> getAllTags(@PathVariable String projectId,
        @RequestParam(required = false) String tag) {
        return Response.ok(apiInterfaceService.getAllTags(projectId, tag));
    }
}
