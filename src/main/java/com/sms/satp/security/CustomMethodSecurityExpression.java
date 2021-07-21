package com.sms.satp.security;

import com.sms.satp.common.constant.Role;
import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;
import org.springframework.security.access.expression.SecurityExpressionRoot;
import org.springframework.security.access.expression.method.MethodSecurityExpressionOperations;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;

public class CustomMethodSecurityExpression extends SecurityExpressionRoot implements
    MethodSecurityExpressionOperations {

    public CustomMethodSecurityExpression(Authentication authentication) {
        super(authentication);
    }

    public boolean hasRoleOrAdmin(String role) {
        return authentication.getAuthorities().stream()
            .anyMatch((grantedAuthority) -> Role.ADMIN
                .equals(grantedAuthority.getAuthority()) || grantedAuthority.getAuthority().equals(role));
    }

    public boolean hasAllRoleOrAdmin(String... roles) {
        Set<String> roleList = authentication.getAuthorities().stream().map(GrantedAuthority::getAuthority)
            .collect(Collectors.toUnmodifiableSet());
        if (roleList.contains(Role.ADMIN)) {
            return true;
        }
        return roleList.containsAll(Arrays.asList(roles));
    }

    private Object filterObject;

    private Object returnObject;

    private Object target;

    @Override
    public void setFilterObject(Object filterObject) {
        this.filterObject = filterObject;
    }

    @Override
    public Object getFilterObject() {
        return this.filterObject;
    }

    @Override
    public void setReturnObject(Object returnObject) {
        this.returnObject = returnObject;
    }

    @Override
    public Object getReturnObject() {
        return this.returnObject;
    }

    /**
     * Sets the "this" property for use in expressions. Typically this will be the "this" property of the {@code
     * JoinPoint} representing the method invocation which is being protected.
     *
     * @param target the target object on which the method in is being invoked.
     */
    void setThis(Object target) {
        this.target = target;
    }

    @Override
    public Object getThis() {
        return this.target;
    }
}
