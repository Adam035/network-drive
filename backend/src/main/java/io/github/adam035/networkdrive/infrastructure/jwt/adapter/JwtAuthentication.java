package io.github.adam035.networkdrive.infrastructure.jwt.adapter;

import io.github.adam035.networkdrive.domain.model.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;
import java.util.Set;

@AllArgsConstructor
public class JwtAuthentication implements Authentication {

    @Getter
    private final User user;

    private final String jwtToken;

    private boolean isAuthenticated;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Set.of();
    }

    @Override
    public String getCredentials() {
        return jwtToken;
    }

    @Override
    public User getDetails() {
        return user;
    }

    @Override
    public String getPrincipal() {
        return user.getId();
    }

    @Override
    public boolean isAuthenticated() {
        return isAuthenticated;
    }

    @Override
    public void setAuthenticated(boolean isAuthenticated) throws IllegalArgumentException {
        this.isAuthenticated = isAuthenticated;
    }

    @Override
    public String getName() {
        return user.getUsername();
    }

}
