package com.thitiwas.reactive.democonnectapi.service;

import com.thitiwas.reactive.democonnectapi.entity.UserEntity;
import com.thitiwas.reactive.democonnectapi.model.UserModel;
import com.thitiwas.reactive.democonnectapi.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.Optional;

@Service
@Slf4j
public class UserService {

    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public Flux<UserModel> getAllUser() {
        return userRepository.findAll().map(this::mapUserEntity);
    }

    public Mono<UserModel> getById(String id) {
        Long idLong = Long.valueOf(id);
        return userRepository.findById(idLong)
                .flatMap(userEntity -> Mono.just(mapUserEntity(userEntity)));
    }

    private UserModel mapUserEntity(UserEntity userEntity) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MMMM-dd HH:mm:ss", new Locale("th","TH"));
        return UserModel.builder()
                .id(String.valueOf(userEntity.getId()))
                .email(userEntity.getEmail())
                .isConfirm(Optional.ofNullable(userEntity.getIsConfirm()).orElse(false).toString().toUpperCase())
                .isDelete(Optional.ofNullable(userEntity.getIsDelete()).orElse(false).toString().toUpperCase())
                .telno(userEntity.getTelno())
                .build();
    }
}
