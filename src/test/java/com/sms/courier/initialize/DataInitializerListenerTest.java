package com.sms.courier.initialize;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.sms.courier.initialize.impl.EngineInitializer;
import java.util.Map;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.context.ConfigurableApplicationContext;

@DisplayName("Test for DataInitializerListener")
public class DataInitializerListenerTest {

    private final ApplicationStartedEvent applicationStartedEvent = mock(ApplicationStartedEvent.class);
    private final ConfigurableApplicationContext applicationContext = mock(ConfigurableApplicationContext.class);
    private final DataInitializerListener dataInitializerListener = new DataInitializerListener();
    private final EngineInitializer engineInitializer = mock(EngineInitializer.class);

    @Test
    @DisplayName("Test method onApplicationEvent in DataInitializerListener")
    public void onApplicationEvent_test() {
        when(applicationStartedEvent.getApplicationContext()).thenReturn(applicationContext);
        when(applicationContext.getBeansOfType(DataInitializer.class))
            .thenReturn(Map.of("cleanRunningEngine", engineInitializer));
        dataInitializerListener.onApplicationEvent(applicationStartedEvent);
        doNothing().when(engineInitializer).init(applicationContext);

    }


}
