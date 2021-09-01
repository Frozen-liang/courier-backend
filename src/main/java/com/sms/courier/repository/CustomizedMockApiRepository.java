package com.sms.courier.repository;

import com.sms.courier.dto.request.MockApiPageRequest;
import com.sms.courier.dto.response.MockApiResponse;
import java.util.List;
import org.bson.types.ObjectId;
import org.springframework.data.domain.Page;

public interface CustomizedMockApiRepository {

    Page<MockApiResponse> page(ObjectId apiId, MockApiPageRequest pageRequest);

    List<MockApiResponse> list(ObjectId apiId, Boolean isEnable);
}
