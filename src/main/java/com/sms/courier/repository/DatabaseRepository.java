package com.sms.courier.repository;

import com.sms.courier.dto.response.DataBaseResponse;
import com.sms.courier.entity.database.DatabaseEntity;
import com.sms.courier.entity.job.JobDatabase;
import java.util.List;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface DatabaseRepository extends MongoRepository<DatabaseEntity, String> {

    List<DataBaseResponse> findAllByProjectIdAndRemoved(String projectId, boolean removed);

}
