package io.github.adam035.networkdrive.domain.model;

import lombok.Data;

@Data
public class User {

    private String id;

    private String username;

    private String email;

    private AccountTier accountTier;

    private Role role;

    public enum Role {
        USER,
        ADMIN
    }

}
