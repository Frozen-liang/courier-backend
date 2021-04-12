package com.sms.satp.service;

import com.sms.satp.entity.dto.ApiLabelDto;
import java.util.List;
import org.bson.types.ObjectId;

public interface ApiLabelService {

    ApiLabelDto findById(ObjectId id);

    List<ApiLabelDto> list(String projectId, String labelName, Short labelType);

    void add(ApiLabelDto apiLabelDto);

    void edit(ApiLabelDto apiLabelDto);

    void delete(ObjectId id);

}
