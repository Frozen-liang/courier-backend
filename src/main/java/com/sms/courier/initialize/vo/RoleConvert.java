package com.sms.courier.initialize.vo;

import com.sms.courier.entity.system.SystemRoleEntity;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RoleConvert {

    private List<SystemRoleEntity> roles;
}
