package io.github.adam035.networkdrive.user.domain.model;

import lombok.Data;

@Data
public class User {

    private String id;

    private String username;

    private String email;

    private UserRole role;

}
