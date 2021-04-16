package com.sms.satp.infrastructure.id;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class DefaultIdentifierGeneratorTest {

    @Test
    public void testGenerateId() {
        DefaultIdentifierGenerator sharedInstance = DefaultIdentifierGenerator.getSharedInstance();
        Long id = sharedInstance.nextId();
        Long id2 = sharedInstance.nextId();
        Assertions.assertNotEquals(id, id2);
    }

    @Test
    public void testGenerateId_notNull() {
        DefaultIdentifierGenerator sharedInstance = DefaultIdentifierGenerator.getSharedInstance();
        Long id = sharedInstance.nextId(null);
        Long id2 = sharedInstance.nextId(null);
        Assertions.assertNotEquals(id, id2);
    }

    @Test
    public void testGenerateSingleton() {
        DefaultIdentifierGenerator sharedInstance1 = DefaultIdentifierGenerator.getSharedInstance();
        DefaultIdentifierGenerator sharedInstance2 = DefaultIdentifierGenerator.getSharedInstance();

        Assertions.assertEquals(sharedInstance1, sharedInstance2);
    }

}
