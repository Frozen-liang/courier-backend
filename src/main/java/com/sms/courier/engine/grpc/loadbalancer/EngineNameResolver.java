/*
 * Copyright 2015 The gRPC Authors
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

import static com.google.common.base.Preconditions.checkNotNull;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.annotations.VisibleForTesting;
import com.google.common.base.Preconditions;
import com.google.common.base.Stopwatch;
import com.google.common.util.concurrent.ThreadFactoryBuilder;
import com.sms.courier.common.exception.ApiTestPlatformException;
import com.sms.courier.common.response.Response;
import com.sms.courier.engine.grpc.client.EngineStubFactory;
import com.sms.courier.engine.model.EngineAddress;
import com.sms.courier.utils.EnvUtils;
import com.sms.courier.utils.ExceptionUtils;
import io.grpc.EquivalentAddressGroup;
import io.grpc.NameResolver;
import io.grpc.ProxyDetector;
import io.grpc.Status;
import io.grpc.SynchronizationContext;
import io.grpc.internal.SharedResourceHolder;
import io.grpc.internal.SharedResourceHolder.Resource;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

public class EngineNameResolver extends NameResolver {

    private static final Logger logger = Logger.getLogger(EngineNameResolver.class.getName());


    @VisibleForTesting
    static final String NETWORKADDRESS_CACHE_TTL_PROPERTY = "networkaddress.cache.ttl";
    /**
     * Default DNS cache duration if network cache ttl value is not specified ({@code null}).
     */
    @VisibleForTesting
    static final long DEFAULT_NETWORK_CACHE_TTL_SECONDS = 30;

    @VisibleForTesting
    final ProxyDetector proxyDetector;

    /**
     * Access through {@link #getLocalHostname}.
     */
    private static String localHostname;

    private final String authority;

    /**
     * Executor that will be used if an Executor is not provide via {@link Args}.
     */
    private final Resource<Executor> executorResource;
    private final long cacheTtlNanos;
    private final SynchronizationContext syncContext;

    // Following fields must be accessed from syncContext
    private final Stopwatch stopwatch;
    protected boolean resolved;
    private boolean shutdown;
    private Executor executor;
    private final ScheduledExecutorService scheduledExecutor;

    /**
     * True if using an executor resource that should be released after use.
     */
    private final boolean usingExecutorResource;

    private boolean resolving;

    // The field must be accessed from syncContext, although the methods on an Listener2 can be called
    // from any thread.
    private Listener2 listener;

    protected EngineNameResolver(
        String authority,
        Args args,
        Resource<Executor> executorResource,
        Stopwatch stopwatch,
        boolean isAndroid) {
        checkNotNull(args, "args");
        // TODO: if a DNS server is provided as nsAuthority, use it.
        this.authority = authority;
        this.executorResource = executorResource;
        this.proxyDetector = checkNotNull(args.getProxyDetector(), "proxyDetector");
        this.cacheTtlNanos = getNetworkAddressCacheTtlNanos(isAndroid);
        this.stopwatch = checkNotNull(stopwatch, "stopwatch");
        this.syncContext = checkNotNull(args.getSynchronizationContext(), "syncContext");
        this.executor = args.getOffloadExecutor();
        this.usingExecutorResource = executor == null;
        ThreadFactory threadFactory = new ThreadFactoryBuilder().setNameFormat("refresh-engine-thread-%d").build();
        scheduledExecutor = Executors.newScheduledThreadPool(1, threadFactory);
    }

    @Override
    public String getServiceAuthority() {
        return authority;
    }

    @Override
    public void start(Listener2 listener) {
        Preconditions.checkState(this.listener == null, "already started");
        if (usingExecutorResource) {
            executor = SharedResourceHolder.get(executorResource);
        }
        this.listener = checkNotNull(listener, "listener");
        resolve();
        scheduler();
    }

    private void scheduler() {
        logger.log(Level.INFO, "Open scheduler executor!");
        scheduledExecutor.scheduleWithFixedDelay(this::refresh, 60, 15, TimeUnit.SECONDS);
    }

    @Override
    public void refresh() {
        Preconditions.checkState(listener != null, "not started");
        resolve();
    }

    private final class Resolve implements Runnable {

        private final Listener2 savedListener;
        private static final String URL = "http://localhost:%s/v1/engine/getAvailableEngine";
        private final ObjectMapper objectMapper = new ObjectMapper();

        Resolve(Listener2 savedListener) {
            this.savedListener = checkNotNull(savedListener, "savedListener");
        }

        @Override
        public void run() {
            boolean success = true;
            try {
                ResolutionResult.Builder resolutionResultBuilder = ResolutionResult.newBuilder();
                List<EquivalentAddressGroup> address = getAddress();
                resolutionResultBuilder.setAddresses(address);
                savedListener.onResult(resolutionResultBuilder.build());
            } catch (ApiTestPlatformException e) {
                success = false;
                savedListener.onError(
                    Status.INTERNAL.withDescription(e.getMessage()).withCause(e));
            } finally {
                final boolean succeed = success;
                syncContext.execute(new Runnable() {
                    @Override
                    public void run() {
                        if (succeed) {
                            resolved = true;
                            if (cacheTtlNanos > 0) {
                                stopwatch.reset().start();
                            }
                        }
                        resolving = false;
                    }
                });
            }
        }

        private List<EquivalentAddressGroup> getAddress() {
            try {
                RestTemplate restTemplate = new RestTemplate(new HttpComponentsClientHttpRequestFactory());
                ResponseEntity<String> responseEntity = restTemplate
                    .getForEntity(String.format(URL, EnvUtils.getPort()), String.class);
                Response<List<EngineAddress>> response = objectMapper
                    .readValue(responseEntity.getBody(), new TypeReference<>() {
                    });
                List<EquivalentAddressGroup> addressGroups = new ArrayList<>();
                response.getData().forEach(engineAddress -> {
                    InetSocketAddress inetSocketAddress = new InetSocketAddress(engineAddress.getHost(),
                        engineAddress.getPort());
                    addressGroups.add(new EquivalentAddressGroup(inetSocketAddress));
                });
                if (addressGroups.isEmpty()) {
                    InetSocketAddress inetSocketAddress = new InetSocketAddress("", 0);
                    addressGroups.add(new EquivalentAddressGroup(inetSocketAddress));
                }
                return addressGroups;
            } catch (Exception e) {
                e.printStackTrace();
                throw ExceptionUtils.mpe("Failed to obtain the engine address.");
            }
        }
    }

    private void resolve() {
        logger.log(Level.INFO, "Refresh engine resolving=" + resolving + ",shutdown=" + shutdown);
        if (resolving || shutdown || !cacheRefreshRequired()) {
            if (shutdown) {
                EngineStubFactory.refresh();
                closeScheduler();
            }
            return;
        }
        resolving = true;
        executor.execute(new Resolve(listener));
    }

    private void closeScheduler() {
        if (!scheduledExecutor.isShutdown()) {
            logger.log(Level.INFO, "Close scheduler executor!");
            scheduledExecutor.shutdown();
        }
    }

    private boolean cacheRefreshRequired() {
        return !resolved
            || cacheTtlNanos == 0
            || (cacheTtlNanos > 0 && stopwatch.elapsed(TimeUnit.NANOSECONDS) > cacheTtlNanos);
    }

    @Override
    public void shutdown() {
        logger.log(Level.WARNING, "----------------Shutdown---------------!!!");
        if (shutdown) {
            return;
        }
        shutdown = true;
        if (executor != null && usingExecutorResource) {
            executor = SharedResourceHolder.release(executorResource, executor);
        }
    }

    /**
     * Returns value of network address cache ttl property if not Android environment. For android, DnsNameResolver does
     * not cache the dns lookup result.
     */
    private static long getNetworkAddressCacheTtlNanos(boolean isAndroid) {
        if (isAndroid) {
            // on Android, ignore dns cache.
            return 0;
        }

        String cacheTtlPropertyValue = System.getProperty(NETWORKADDRESS_CACHE_TTL_PROPERTY);
        long cacheTtl = DEFAULT_NETWORK_CACHE_TTL_SECONDS;
        if (cacheTtlPropertyValue != null) {
            try {
                cacheTtl = Long.parseLong(cacheTtlPropertyValue);
            } catch (NumberFormatException e) {
                logger.log(
                    Level.WARNING,
                    "Property({0}) valid is not valid number format({1}), fall back to default({2})",
                    new Object[]{NETWORKADDRESS_CACHE_TTL_PROPERTY, cacheTtlPropertyValue, cacheTtl});
            }
        }
        return cacheTtl > 0 ? TimeUnit.SECONDS.toNanos(cacheTtl) : cacheTtl;
    }


    private static String getLocalHostname() {
        if (localHostname == null) {
            try {
                localHostname = InetAddress.getLocalHost().getHostName();
            } catch (UnknownHostException e) {
                throw new RuntimeException(e);
            }
        }
        return localHostname;
    }

}
