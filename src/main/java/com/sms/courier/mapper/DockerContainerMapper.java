package com.sms.courier.mapper;

import com.sms.courier.docker.entity.ContainerSettingEntity;
import com.sms.courier.dto.request.ContainerSettingRequest;
import com.sms.courier.dto.response.ContainerSettingResponse;
import com.sms.courier.utils.AesUtil;
import org.apache.commons.lang3.StringUtils;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", injectionStrategy = InjectionStrategy.CONSTRUCTOR,
    unmappedTargetPolicy = ReportingPolicy.IGNORE, imports = {AesUtil.class, StringUtils.class})
public interface DockerContainerMapper {

    ContainerSettingResponse toResponse(ContainerSettingEntity containerSetting);

    @Mapping(target = "password",
        expression = "java(StringUtils.isNotBlank(request.getPassword()) ? AesUtil.decrypt(request.getPassword()) "
            + ": null)")
    ContainerSettingEntity toEntity(ContainerSettingRequest request);
}