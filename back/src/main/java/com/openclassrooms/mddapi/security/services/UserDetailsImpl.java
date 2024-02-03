package com.openclassrooms.mddapi.security.services;

import com.openclassrooms.mddapi.model.User;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;

public class UserDetailsImpl implements UserDetails {

    @Getter
    private Long id;
    private String username;
    @Getter
    private String email;
    private String password;

    @Getter
    private User user;

    public static UserDetailsImpl build(User user) {
        UserDetailsImpl userDetails = new UserDetailsImpl();
        userDetails.id = user.getId();
        userDetails.username = user.getUsername();
        userDetails.email = user.getEmail();
        userDetails.password = user.getPassword();
        userDetails.user = user;
        return userDetails;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER"));
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public String getPassword() {
        return password;
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
