package com.sms.courier.service.impl;

import static com.sms.courier.common.exception.ErrorCode.ADD_CLIENT_PORT_STATISTIC_ERROR;
import static com.sms.courier.common.exception.ErrorCode.GET_CLIENT_PORT_STATISTIC_COUNT_ERROR;

import com.sms.courier.dto.response.CountStatisticsResponse;
import com.sms.courier.entity.statistics.ClientPortStatisticEntity;
import com.sms.courier.repository.ClientPortStatisticRepository;
import com.sms.courier.repository.CommonStatisticsRepository;
import com.sms.courier.repository.CustomizedApiRepository;
import com.sms.courier.repository.CustomizedApiTestCaseRepository;
import com.sms.courier.repository.CustomizedSceneCaseRepository;
import com.sms.courier.service.ClientPortStatisticService;
import com.sms.courier.utils.ExceptionUtils;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class ClientPortStatisticServiceImpl extends AbstractStatisticsService implements ClientPortStatisticService {

    private final ClientPortStatisticRepository clientPortStatisticRepository;

    public ClientPortStatisticServiceImpl(
        ClientPortStatisticRepository clientPortStatisticRepository,
        CommonStatisticsRepository commonStatisticsRepository,
        CustomizedSceneCaseRepository customizedSceneCaseRepository,
        CustomizedApiTestCaseRepository customizedApiTestCaseRepository,
        CustomizedApiRepository customizedApiRepository) {
        super(commonStatisticsRepository, customizedSceneCaseRepository, customizedApiTestCaseRepository,
            customizedApiRepository);
        this.clientPortStatisticRepository = clientPortStatisticRepository;
    }

    @Override
    public Boolean add(String ip, String host) {
        try {
            ClientPortStatisticEntity statistic = ClientPortStatisticEntity.builder().ip(ip).host(host).build();
            clientPortStatisticRepository.save(statistic);
            return Boolean.TRUE;
        } catch (Exception e) {
            log.error("Failed to add the ClientPortStatistic!", e);
            throw ExceptionUtils.mpe(ADD_CLIENT_PORT_STATISTIC_ERROR, e);
        }
    }

    @Override
    public List<CountStatisticsResponse> groupDayCount(Integer day) {
        try {
            return groupDay(day, ClientPortStatisticEntity.class);
        } catch (Exception e) {
            log.error("Failed to get the ClientPortStatistic count!", e);
            throw ExceptionUtils.mpe(GET_CLIENT_PORT_STATISTIC_COUNT_ERROR, e);
        }
    }

}
