package com.sms.satp.service;

import com.sms.satp.common.enums.ApiLabelType;
import com.sms.satp.entity.dto.ApiLabelDto;
import java.util.List;

public interface ApiLabelService {

    ApiLabelDto findById(String id);

    List<ApiLabelDto> list(String projectId, String labelName, ApiLabelType labelType);

    void add(ApiLabelDto apiLabelDto);

    void edit(ApiLabelDto apiLabelDto);

    void delete(String id);

}
