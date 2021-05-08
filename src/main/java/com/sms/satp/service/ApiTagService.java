package com.sms.satp.service;

import com.sms.satp.common.enums.ApiTagType;
import com.sms.satp.dto.ApiTagDto;
import java.util.List;

public interface ApiTagService {

    ApiTagDto findById(String id);

    List<ApiTagDto> list(String projectId, String tagName, ApiTagType tagType);

    void add(ApiTagDto apiTagDto);

    void edit(ApiTagDto apiTagDto);

    void delete(String id);

}
