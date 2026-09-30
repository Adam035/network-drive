package io.github.adam035.networkdrive.application.usecase;

import io.github.adam035.networkdrive.application.port.AuthUserExtractorPort;
import io.github.adam035.networkdrive.domain.exception.UserDoesNotExist;
import io.github.adam035.networkdrive.domain.model.Share;
import io.github.adam035.networkdrive.domain.model.User;
import io.github.adam035.networkdrive.domain.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Set;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class GetAvailableVolumeLabelsUseCase {

    private final AuthUserExtractorPort authUserExtractorPort;

    private final UserRepository userRepository;

    public Set<String> getAvailableVolumeLabels() {
        User user = authUserExtractorPort.extractUser()
                .orElseThrow(UserDoesNotExist::new);

        Set<String> ids = user.getGrantedShares().stream()
                .map(Share::getOwnerId)
                .collect(Collectors.toSet());

        Set<String> availableVolumeLabels = userRepository.findAllById(ids).stream()
                .map(User::getUsername)
                .collect(Collectors.toSet());

        availableVolumeLabels.add(user.getUsername());

        return availableVolumeLabels;
    }

}
