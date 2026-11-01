package io.github.adam035.networkdrive.domain.model;

import lombok.Data;

import java.util.HashSet;
import java.util.Set;

@Data
public class User {

    private String id;

    private String username;

    private String email;

    private AccountTier accountTier;

    private Set<Share> receivedShares = new HashSet<>();

    private Set<Share> grantedShares = new HashSet<>();

}
