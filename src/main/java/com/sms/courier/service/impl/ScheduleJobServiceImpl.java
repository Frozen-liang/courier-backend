package com.sms.courier.service.impl;

import static com.sms.courier.common.field.ScheduleSceneCaseJobField.SCENE_CASE_ID;
import static com.sms.courier.common.field.ScheduleSceneCaseJobField.SCHEDULE_RECORD_ID;

import com.sms.courier.dto.request.ScheduleJobRequest;
import com.sms.courier.dto.response.ScheduleCaseJobResponse;
import com.sms.courier.dto.response.ScheduleSceneCaseJobResponse;
import com.sms.courier.entity.job.ScheduleSceneCaseJobEntity;
import com.sms.courier.entity.mongo.QueryVo;
import com.sms.courier.repository.CommonRepository;
import com.sms.courier.repository.ScheduleCaseJobRepository;
import com.sms.courier.service.ScheduleJobService;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.stereotype.Service;

@Service
public class ScheduleJobServiceImpl implements ScheduleJobService {

    private final ScheduleCaseJobRepository scheduleCaseJobRepository;
    private final CommonRepository commonRepository;

    public ScheduleJobServiceImpl(ScheduleCaseJobRepository scheduleCaseJobRepository,
        CommonRepository commonRepository) {
        this.scheduleCaseJobRepository = scheduleCaseJobRepository;
        this.commonRepository = commonRepository;
    }

    @Override
    public List<ScheduleCaseJobResponse> getCaseJobInfoString(ScheduleJobRequest request) {
        return scheduleCaseJobRepository
            .findByScheduleRecordIdAndApiTestCase_JobApiTestCase_Id(request.getScheduleRecordId(), request.getCaseId());
    }

    @Override
    public Page<ScheduleSceneCaseJobResponse> getSceneCaseJobInfo(ScheduleJobRequest request) {
        QueryVo query = new QueryVo();
        query.setEntityClass(ScheduleSceneCaseJobEntity.class);
        List<Optional<Criteria>> criteriaList = List
            .of(SCHEDULE_RECORD_ID.is(request.getScheduleRecordId()), SCENE_CASE_ID.is(request.getCaseId()));
        query.setCriteriaList(criteriaList);
        return commonRepository.page(query, request, ScheduleSceneCaseJobResponse.class);
    }
}
