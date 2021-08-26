package com.sms.courier.service;

import com.sms.courier.dto.request.MockApiPageRequest;
import com.sms.courier.dto.request.MockApiRequest;
import com.sms.courier.dto.response.PageMockApiResponse;
import org.bson.types.ObjectId;

public interface MockApiService {

    Boolean add(MockApiRequest request);

    PageMockApiResponse page(ObjectId apiId, MockApiPageRequest pageRequest);

}
