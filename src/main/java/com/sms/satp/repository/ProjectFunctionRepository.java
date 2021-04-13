package com.sms.satp.repository;

import com.sms.satp.entity.function.ProjectFunction;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ProjectFunctionRepository extends MongoRepository<ProjectFunction, ObjectId> {

}