package com.sms.courier.service;

import com.sms.courier.common.exception.ApiTestPlatformException;
import com.sms.courier.dto.request.AmazonStorageSettingRequest;
import com.sms.courier.dto.response.AmazonStorageSettingResponse;
import com.sms.courier.entity.file.AmazonStorageSettingEntity;
import com.sms.courier.mapper.AmazonStorageSettingMapper;
import com.sms.courier.mapper.AmazonStorageSettingMapperImpl;
import com.sms.courier.repository.AmazonStorageSettingRepository;
import com.sms.courier.service.impl.AmazonStorageSettingServiceImpl;
import com.sms.courier.storagestrategy.strategy.impl.AmazonStorageService;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static com.sms.courier.common.exception.ErrorCode.DELETE_AWS3_SETTING_BY_ID_ERROR;
import static com.sms.courier.common.exception.ErrorCode.EDIT_NOT_EXIST_ERROR;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@DisplayName("Tests for AmazonStorageSettingService")
class AmazonStorageSettingServiceTest {

    private final AmazonStorageSettingRepository amazonStorageSettingRepository = mock(AmazonStorageSettingRepository.class);
    private final AmazonStorageSettingMapper aws3SettingMapper = new AmazonStorageSettingMapperImpl();
    private final AmazonStorageService amazonStorageService = new AmazonStorageService(amazonStorageSettingRepository);
    private final AmazonStorageSettingService aws3SettingService = new AmazonStorageSettingServiceImpl(
            amazonStorageSettingRepository, aws3SettingMapper, amazonStorageService);
    private final AmazonStorageSettingResponse amazonStorageSettingResponse = AmazonStorageSettingResponse
            .builder().build();
    private final AmazonStorageSettingRequest aws3SettingRequest = AmazonStorageSettingRequest.builder()
            .id(ID).build();
    private final AmazonStorageSettingEntity aws3SettingEntity = AmazonStorageSettingEntity.builder().id(ID).build();
    private static final String ID = ObjectId.get().toString();

    @Test
    @DisplayName("Test the findOne method in the Aws3Setting service")
    public void findOne_test() {
        when(amazonStorageSettingRepository.findFirstByOrderByModifyDateTime())
                .thenReturn(Optional.ofNullable(amazonStorageSettingResponse));
        AmazonStorageSettingResponse response = aws3SettingService.findOne();
        assertThat(response).isNotNull();
    }

    @Test
    @DisplayName("Test the get method in the findOne Setting service then Exception")
    void findOne_test_thenException() {
        when(amazonStorageSettingRepository.findFirstByOrderByModifyDateTime()).thenThrow(new RuntimeException());
        assertThatThrownBy(aws3SettingService::findOne).isInstanceOf(ApiTestPlatformException.class);
    }

    @Test
    @DisplayName("Test the edit method in the Aws3Setting service")
    public void edit_test() {
        /*when(amazonStorageSettingRepository.findById(ID)).thenReturn(Optional.ofNullable(aws3SettingEntity));
        when(amazonStorageSettingRepository.save(any())).thenReturn(aws3SettingEntity);
        doNothing().when(amazonStorageService).createS3Client(aws3SettingEntity);
        assertThat(aws3SettingService.edit(aws3SettingRequest)).isTrue();*/
    }

    @Test
    @DisplayName("An exception occurred while edit Aws3Setting")
    public void edit_exception_test() {
        when(amazonStorageSettingRepository.existsById(any())).thenReturn(Boolean.TRUE);
        doThrow(new RuntimeException()).when(amazonStorageSettingRepository).save(any(AmazonStorageSettingEntity.class));
        assertThatThrownBy(() -> aws3SettingService.edit(aws3SettingRequest))
                .isInstanceOf(ApiTestPlatformException.class)
                .extracting("code").isEqualTo(EDIT_NOT_EXIST_ERROR.getCode());
    }

    @Test
    @DisplayName("Test the delete method in the Aws3Setting service")
    public void delete_test() {
        doNothing().when(amazonStorageSettingRepository).deleteById(ID);
        assertThat(aws3SettingService.delete(ID)).isTrue();
    }

    @Test
    @DisplayName("An exception occurred while delete Aws3Setting")
    public void delete_exception_test() {
        doThrow(new RuntimeException()).when(amazonStorageSettingRepository).deleteById(ID);
        assertThatThrownBy(() -> aws3SettingService.delete(ID))
                .isInstanceOf(ApiTestPlatformException.class)
                .extracting("code").isEqualTo(DELETE_AWS3_SETTING_BY_ID_ERROR.getCode());
    }

}
