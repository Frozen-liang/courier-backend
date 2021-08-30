package com.sms.courier.service;

import com.sms.courier.dto.request.MockApiPageRequest;
import com.sms.courier.dto.request.MockApiRequest;
import com.sms.courier.dto.response.MockApiResponseList;
import com.sms.courier.dto.response.MockApiResponsePage;
import java.util.List;
import org.bson.types.ObjectId;

public interface MockApiService {

    Boolean add(MockApiRequest request);

    MockApiResponsePage page(ObjectId apiId, MockApiPageRequest pageRequest);

    MockApiResponseList list(ObjectId apiId, Boolean isEnable);

    Boolean edit(MockApiRequest request);

    Boolean deleteByIds(List<String> ids);
}
