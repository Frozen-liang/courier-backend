package com.sms.courier.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.sms.courier.dto.response.SystemVersionResponse;
import com.sms.courier.repository.SystemVersionRepository;
import com.sms.courier.service.impl.SystemVersionServiceImpl;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("Tests for SystemVersionService")
class SystemVersionServiceTest {

    private final SystemVersionRepository systemVersionRepository = mock(SystemVersionRepository.class);
    private final SystemVersionService systemVersionService = new SystemVersionServiceImpl(systemVersionRepository);

    @Test
    @DisplayName("Test the list method in the SystemVersion service")
    public void list_test() {
        ArrayList<SystemVersionResponse> list = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            list.add(SystemVersionResponse.builder().build());
        }
        when(systemVersionRepository.findAllByOrderByVersionDesc()).thenReturn(list);
        List<SystemVersionResponse> result = systemVersionService.findAll();
        assertThat(result).hasSize(10);
    }

}
