package com.sms.courier.repository;

import com.sms.courier.entity.generator.GeneratorTemplateEntity;
import java.util.List;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface GeneratorTemplateRepository extends MongoRepository<GeneratorTemplateEntity, String> {

    Long deleteAllByIdIsIn(List<String> ids);

}
