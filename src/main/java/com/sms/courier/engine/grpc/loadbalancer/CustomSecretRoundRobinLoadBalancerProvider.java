/*
 * Copyright 2018 The gRPC Authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.sms.courier.engine.grpc.loadbalancer;

import com.sms.courier.utils.ApplicationContextUtils;
import io.grpc.LoadBalancer;
import io.grpc.LoadBalancerProvider;
import io.grpc.NameResolver.ConfigOrError;
import java.util.Map;
import org.springframework.context.ApplicationContext;

/**
 * Provider for the "round_robin" balancing policy.
 */
// Make it package-private so that it cannot be directly referenced by users.  Java service loader
// requires the provider to be public, but we can hide it under a package-private class.
final class CustomSecretRoundRobinLoadBalancerProvider {

    private CustomSecretRoundRobinLoadBalancerProvider() {
    }

    public static final class Provider extends LoadBalancerProvider {

        private static final String NO_CONFIG = "no service config";


        @Override
        public boolean isAvailable() {
            return true;
        }

        @Override
        public int getPriority() {
            return 5;
        }

        @Override
        public String getPolicyName() {
            return Constants.ROUND_ROBIN;
        }

        @Override
        public LoadBalancer newLoadBalancer(LoadBalancer.Helper helper) {
            ApplicationContext applicationContext = ApplicationContextUtils.INSTANCE.getApplicationContext();
            return new CustomRoundRobinLoadBalancer(helper, applicationContext);
        }

        @Override
        public ConfigOrError parseLoadBalancingPolicyConfig(
            Map<String, ?> rawLoadBalancingPolicyConfig) {
            return ConfigOrError.fromConfig(NO_CONFIG);
        }
    }
}
