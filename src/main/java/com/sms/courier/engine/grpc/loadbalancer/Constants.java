package com.sms.courier.engine.grpc.loadbalancer;

import io.grpc.Attributes;
import io.grpc.CallOptions;
import io.grpc.Metadata;

public class Constants {

    private Constants() {
    }

    public static final String ROUND_ROBIN = "custom_round_robin";
    public static final CallOptions.Key<String> TASK_TYPE = CallOptions.Key.create("taskType");
    public static final Attributes.Key<String> ENGINE_NAME = Attributes.Key.create("name");
    public static final Metadata.Key<String> JOB_ID = Metadata.Key.of("jobId", Metadata.ASCII_STRING_MARSHALLER);
    public static final Metadata.Key<String> JOT_TYPE = Metadata.Key.of("jobType", Metadata.ASCII_STRING_MARSHALLER);
    public static final Metadata.Key<String> AUTHORIZATION = Metadata.Key
        .of("Authorization", Metadata.ASCII_STRING_MARSHALLER);
}
