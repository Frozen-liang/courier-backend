package com.sms.satp.repository;

import com.sms.satp.dto.PageDto;
import com.sms.satp.entity.job.SceneCaseJob;
import java.util.List;
import org.springframework.data.domain.Page;

public interface CustomizedSceneCaseJobRepository {

    Page<SceneCaseJob> page(String sceneCaseId, List<String> userIds, PageDto pageDto);
}
