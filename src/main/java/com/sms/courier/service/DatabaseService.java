package com.sms.courier.service;

import com.sms.courier.dto.request.DataBaseRequest;
import com.sms.courier.dto.response.DataBaseResponse;
import com.sms.courier.entity.job.JobDatabase;
import java.util.List;

public interface DatabaseService {

    DataBaseResponse get(String id);

    List<DataBaseResponse> list(String projectId);

    Boolean add(DataBaseRequest dataBaseRequest);

    Boolean edit(DataBaseRequest dataBaseRequest);

    Boolean delete(List<String> ids);

    JobDatabase findJobById(String id);
}
