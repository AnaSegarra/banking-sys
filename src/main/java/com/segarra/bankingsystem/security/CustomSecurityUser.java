package com.segarra.bankingsystem.security;

import com.segarra.bankingsystem.models.Role;
import com.segarra.bankingsystem.models.User;
import com.segarra.bankingsystem.services.AccountHolderService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public class CustomSecurityUser extends User implements UserDetails {
    private static final long serialVersionUID = -4381938875186527688L;
    private static final Logger LOGGER = LogManager.getLogger(CustomSecurityUser.class);

    public CustomSecurityUser(User user) {
        this.setRoles(user.getRoles());
        this.setId(user.getId());
        this.setPassword(user.getPassword());
        this.setUsername(user.getUsername());
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Collection<GrantedAuthority> authorities = new HashSet<>();
        Set<Role> roles = this.getRoles();
        LOGGER.info("Logged user - " + this);
        for( Role role : roles ) {
            authorities.add( new SimpleGrantedAuthority(role.getRole()) );
        }
        return authorities;
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