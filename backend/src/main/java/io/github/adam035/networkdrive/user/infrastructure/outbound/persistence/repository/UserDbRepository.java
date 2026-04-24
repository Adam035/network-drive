package io.github.adam035.networkdrive.user.infrastructure.outbound.persistence.repository;

import io.github.adam035.networkdrive.auth.application.port.out.UserRepositoryPort;
import io.github.adam035.networkdrive.common.domain.model.User;
import io.github.adam035.networkdrive.user.domain.repository.UserRepository;
import io.github.adam035.networkdrive.user.infrastructure.outbound.persistence.entity.UserEntity;
import io.github.adam035.networkdrive.user.infrastructure.outbound.persistence.mapper.UserMapper;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
@AllArgsConstructor
class UserDbRepository implements UserRepository, UserRepositoryPort {

    private UserJpaRepository userJpaRepository;

    private UserMapper userMapper;

    @Override
    public User save(User user) {
        UserEntity userEntity = userMapper.mapToEntity(user);
        return userMapper.mapToModel(userJpaRepository.save(userEntity));
    }

    @Override
    public Optional<User> findById(String id) {
        return userJpaRepository.findById(id)
                .map(userMapper::mapToModel);
    }

    @Override
    public Optional<User> findByUsername(String username) {
        return userJpaRepository.findByUsername(username)
                .map(userMapper::mapToModel);
    }

    @Override
    public List<User> findByUsernameOrEmail(String username, String email) {
        return userJpaRepository.findByUsernameOrEmail(username, email).stream()
                .map(userMapper::mapToModel)
                .toList();
    }

}
