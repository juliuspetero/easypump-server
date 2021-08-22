package com.easypump.infrastructure.security;

import com.easypump.model.user.User;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

public class UserDetails extends org.springframework.security.core.userdetails.User {

    private final User user;

    public UserDetails(User user, Collection<? extends GrantedAuthority> authorities) {
        super(user.getEmail(), user.getPassword(), authorities);
        this.user = user;
    }

    public User getAppUser() {
        return user;
    }
}
