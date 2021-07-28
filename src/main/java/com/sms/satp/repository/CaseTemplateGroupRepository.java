package com.sms.satp.repository;

import com.sms.satp.entity.group.CaseTemplateGroupEntity;
import java.util.List;
import java.util.stream.Stream;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface CaseTemplateGroupRepository extends MongoRepository<CaseTemplateGroupEntity, String> {

    Stream<CaseTemplateGroupEntity> findAllByPathContains(Long realGroupId);

    void deleteAllByIdIn(List<String> ids);

    List<CaseTemplateGroupEntity> findCaseTemplateGroupEntitiesByProjectId(String projectId);
}
