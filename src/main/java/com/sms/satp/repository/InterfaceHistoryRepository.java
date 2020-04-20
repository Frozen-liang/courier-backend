package com.sms.satp.repository;

import com.sms.satp.entity.InterfaceHistory;
import com.sms.satp.entity.InterfaceShowInHistory;
import com.sms.satp.parser.common.HttpMethod;
import java.util.List;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface InterfaceHistoryRepository extends MongoRepository<InterfaceHistory, String> {

    List<InterfaceShowInHistory> findByProjectIdAndMethodAndPath(String projectId, HttpMethod method, String path);
}
