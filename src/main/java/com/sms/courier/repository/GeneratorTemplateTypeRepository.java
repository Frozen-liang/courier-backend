package com.sms.courier.repository;

import com.sms.courier.entity.generator.GeneratorTemplateTypeEntity;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface GeneratorTemplateTypeRepository extends MongoRepository<GeneratorTemplateTypeEntity, String> {


}
