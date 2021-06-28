package com.sms.satp;

import com.mongodb.client.result.UpdateResult;
import com.sms.satp.entity.function.GlobalFunction;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;

@SpringBootTest(classes = ApplicationTests.class)
public class TestMongoTemplate {

    @Autowired
    private MongoTemplate mongoTemplate;

    @Test
    private void testUpdate() {
        Query query = new Query();
        Update update = new Update();
        update.set("workspaceId", "60d5439614c852066b1bf466");
        UpdateResult updateResult = mongoTemplate.updateMulti(query, update, GlobalFunction.class);
        System.out.println(updateResult.getModifiedCount());
        System.out.println(updateResult.getMatchedCount());
    }
}
