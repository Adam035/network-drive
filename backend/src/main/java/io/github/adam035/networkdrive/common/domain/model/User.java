package io.github.adam035.networkdrive.common.domain.model;

import lombok.Data;

@Data
public class User {

    private String id;

    private String username;

    private String email;

    private UserRole role;

}
