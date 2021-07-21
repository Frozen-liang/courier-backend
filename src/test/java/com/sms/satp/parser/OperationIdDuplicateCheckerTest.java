package com.sms.satp.parser;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.when;

import com.sms.satp.common.exception.ApiTestPlatformException;
import com.sms.satp.entity.api.ApiEntity;
import com.sms.satp.entity.project.ProjectImportFlowEntity;
import com.sms.satp.parser.impl.OperationIdDuplicateChecker;
import com.sms.satp.repository.ProjectImportFlowRepository;
import com.sms.satp.security.pojo.CustomUser;
import com.sms.satp.service.MessageService;
import com.sms.satp.utils.SecurityUtil;
import com.sms.satp.websocket.Payload;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.springframework.context.ApplicationContext;

@DisplayName("Test for OperationIdDuplicateChecker")
public class OperationIdDuplicateCheckerTest {

    private final OperationIdDuplicateChecker operationIdDuplicateChecker = new OperationIdDuplicateChecker();
    private ApplicationContext applicationContext = mock(ApplicationContext.class);
    private ProjectImportFlowRepository projectImportFlowRepository = mock(ProjectImportFlowRepository.class);
    private final MessageService messageService = mock(MessageService.class);


    static {
        MockedStatic<SecurityUtil> securityUtilMockedStatic = mockStatic(SecurityUtil.class);
        securityUtilMockedStatic.when(SecurityUtil::getCurrUserId).thenReturn(ObjectId.get().toString());
        securityUtilMockedStatic.when(SecurityUtil::getCurrentUser).thenReturn(new CustomUser("username", "password",
            Collections.emptyList(), "", "username@qq.com"));
    }


    @Test
    public void check_test() {
        ProjectImportFlowEntity projectImportFlowEntity = ProjectImportFlowEntity.builder().build();
        when(applicationContext.getBean(ProjectImportFlowRepository.class)).thenReturn(projectImportFlowRepository);
        when(applicationContext.getBean(MessageService.class)).thenReturn(messageService);
        doNothing().when(messageService).projectMessage(any(), any(Payload.class));
        when(projectImportFlowRepository.save(any(ProjectImportFlowEntity.class))).thenReturn(projectImportFlowEntity);
        assertThatThrownBy(() -> operationIdDuplicateChecker.check(getApi()))
            .isInstanceOf(ApiTestPlatformException.class);
    }

    private List<ApiEntity> getApi() {
        ArrayList<ApiEntity> list = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            list.add(ApiEntity.builder().id(ObjectId.get().toString()).swaggerId("swagger")
                .apiName("test" + Math.random()).build());
        }
        return list;
    }
}
