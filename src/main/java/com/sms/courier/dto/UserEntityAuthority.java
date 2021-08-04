package com.sms.courier.dto;

import com.sms.courier.entity.system.UserEntity;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserEntityAuthority {

    private UserEntity userEntity;
    private List<SimpleGrantedAuthority> authorities;
}