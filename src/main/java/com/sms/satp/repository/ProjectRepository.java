package com.sms.satp.repository;

import com.sms.satp.entity.Project;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

public interface ProjectRepository extends MongoRepository<Project, String> {

}
