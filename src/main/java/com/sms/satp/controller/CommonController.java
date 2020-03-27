package com.sms.satp.controller;

import com.sms.satp.common.constant.Constants;
import com.sms.satp.common.response.Response;
import com.sms.satp.entity.dto.SelectDto;
import com.sms.satp.parser.common.MediaType;
import com.sms.satp.parser.common.SchemaType;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(Constants.COMMON_PATH)
public class CommonController {

    private static final List<SelectDto> SELECT_DTO_LIST =
        Arrays.stream(SchemaType.values()).map(schemaType -> SelectDto.builder()
            .id(schemaType.name())
            .name(schemaType.getType()).build()).collect(Collectors.toList());

    @GetMapping("schema-type")
    public Response<List<SelectDto>> getSchemaTypeSelect() {
        return Response.ok(SELECT_DTO_LIST);
    }

    @GetMapping("media-type")
    public Response<List<SelectDto>> getMediaTypeSelect() {
        return Response.ok(MediaType.getSelectDtoList());
    }
}
