package com.example.book_exchange.security;

import com.example.book_exchange.model.User;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;

@Getter
public class CustomUserDetails implements UserDetails {

    private final User user;

    public CustomUserDetails(User user) {
        this.user = user;
    }

    // Expose your User ID here for Thymeleaf and other uses
    public Long getId() {
        return user.getId();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singleton(new SimpleGrantedAuthority("ROLE_" + user.getRole().name()));
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        return user.getUsername();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true; // modify if you have account expiry logic
    }

    @Override
    public boolean isAccountNonLocked() {
        return true; // modify if you have account lock logic
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true; // modify if you have credentials expiry logic
    }

    @Override
    public boolean isEnabled() {
        return true; // modify if you have enabled/disabled logic
    }
}
