package com.sms.satp.controller;

import com.sms.satp.common.constant.Constants;
import com.sms.satp.common.response.Response;
import com.sms.satp.entity.dto.SelectDto;
import com.sms.satp.parser.common.MediaType;
import com.sms.satp.parser.common.SchemaType;
import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(Constants.COMMON_PATH)
public class CommonController {

    @GetMapping("schema-type")
    public Response<List<SelectDto>> getSchemaTypeSelect() {
        return Response.ok(SchemaType.getSelectDtoList());
    }

    @GetMapping("media-type")
    public Response<List<SelectDto>> getMediaTypeSelect() {
        return Response.ok(MediaType.getSelectDtoList());
    }
}