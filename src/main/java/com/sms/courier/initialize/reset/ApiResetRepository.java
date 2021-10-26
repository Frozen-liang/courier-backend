package com.sms.courier.initialize.reset;

import com.sms.courier.common.field.Field;
import com.sms.courier.initialize.ApiCaseCount;
import java.util.List;

public interface ApiResetRepository {

    List<ApiCaseCount> findProjectIdAndGroupByApiId(String projectId, boolean removed);

    long updateCountFieldByIds(List<ApiCaseCount> caseCountList, Field filedName, boolean isAdd);

    long resetSceneCaseCount(List<String> projectIds);

    List<ApiCaseCount> findApiCountByProjectIdAndGroupByApiId(String projectId, boolean removed, boolean isNowObject);

    List<String> findCaseTemplateIdByProjectIdAndRemoved(String projectId, boolean removed);

    List<ApiCaseCount> findApiCountByProjectIdAndCaseTemplateIdAndGroupByApiId(String caseTemplateId,
        String projectId, boolean isNowProject);
}
