package io.github.adam035.networkdrive.domain.exception;

public class AccountTierDoesNotExistException extends RuntimeException {
    public AccountTierDoesNotExistException() {
        super("Account Tier does not exist!");
    }
}
