package com.migomed.Security;

import com.migomed.Entity.Users;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import java.util.Collection;
import java.util.Collections;

public class CustomUserDetails implements UserDetails {

    private final Users user;
    private final Long userId;
    private final Long workerId;

    public CustomUserDetails(Users user) {
        this.user = user;
        this.userId = user.getId();
        this.workerId = (user.getWorkerDetails() != null) ? user.getWorkerDetails().getId() : null;
    }

    public Long getUserId() {
        return userId;
    }

    public Long getWorkerId() {
        return workerId;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.emptyList();
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        return user.getSurname();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
