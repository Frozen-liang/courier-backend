package com.sms.satp.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import com.sms.satp.entity.ApiInterface;
import com.sms.satp.entity.dto.ApiInterfaceDto;
import com.sms.satp.repository.ApiInterfaceRepository;
import com.sms.satp.repository.ProjectRepository;
import com.sms.satp.service.ApplicationTests;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;

@DisplayName("")
@SpringBootTest(classes = ApplicationTests.class,
    webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ApiInterfaceMapperTest {

    @Autowired
    ApiInterfaceMapper apiInterfaceMapper;

    @MockBean
    ApiInterfaceRepository apiInterfaceRepository;

    @MockBean
    ProjectRepository projectRepository;

    private static final Integer SIZE = 10;
    private static final String TITLE = "title";

    @Test
    void entity_to_dto() {
        ApiInterface apiInterface = ApiInterface.builder()
            .title("TITLE")
            .build();
        ApiInterfaceDto apiInterfaceDto = apiInterfaceMapper.toDto(apiInterface);
        assertThat(StringUtils.equals(apiInterfaceDto.getTitle(), TITLE));
    }

    @Test
    void apiInterfaceList_to_apiInterfaceDtoList() {
        List<ApiInterface> apiInterfaceList = new ArrayList<>();
        for (int i = 0; i < SIZE; i++) {
            apiInterfaceList.add(
                ApiInterface.builder()
                    .title(TITLE)
                    .build());
        }
        List<ApiInterfaceDto> apiInterfaceDtoList = apiInterfaceMapper.toDtoList(apiInterfaceList);
        assertThat(apiInterfaceDtoList.size()).isEqualTo(SIZE);
        assertThat(apiInterfaceDtoList).allMatch(result -> StringUtils.equals(result.getTitle(), TITLE));
    }

}