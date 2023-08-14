package com.thitiwas.reactive.democonnectapi.service;

import com.thitiwas.reactive.democonnectapi.entity.UserEntity;
import com.thitiwas.reactive.democonnectapi.exception.CustomErrorException;
import com.thitiwas.reactive.democonnectapi.model.CommonConstant;
import com.thitiwas.reactive.democonnectapi.model.ErrorObj;
import com.thitiwas.reactive.democonnectapi.model.UserModel;
import com.thitiwas.reactive.democonnectapi.repository.UserRepository;
import com.thitiwas.reactive.democonnectapi.repository.UserRepositoryCustom;
import lombok.extern.slf4j.Slf4j;
import org.joda.time.format.DateTimeFormatter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.Locale;
import java.util.Optional;

@Service
@Slf4j
public class UserService {

    private final UserRepository userRepository;
    private final UserRepositoryCustom userRepositoryCustom;

    @Autowired
    public UserService(UserRepository userRepository, UserRepositoryCustom userRepositoryCustom) {
        this.userRepository = userRepository;
        this.userRepositoryCustom = userRepositoryCustom;
    }

    public Flux<UserModel> getAllUser() {
        return userRepository.findAll().map(this::mapUserEntity);
    }

    public Mono<UserModel> getById(String id) {
        Long idLong = Long.valueOf(id);
        return userRepository.findById(idLong)
                .switchIfEmpty(Mono.error(() -> {
                    ErrorObj errorObj = ErrorObj.builder()
                            .errorCode("400")
                            .topicMessage("เกิดข้อผิดพลาด")
                            .detailMessage("ไม่พบผู้ใช้งานในระบบ")
                            .msg("user not found")
                            .msgType("")
                            .build();
                    return new CustomErrorException(HttpStatus.BAD_REQUEST,
                            errorObj,
                            CommonConstant.STATUS_FAIL_CODE,
                            CommonConstant.STATUS_FAIL);
                }))
                .flatMap(userEntity -> Mono.just(mapUserEntity(userEntity)));
    }

    public Mono<UserModel> findByTelno(String telno) {
        return userRepositoryCustom.findByTelno(telno)
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
                .createDate(convertToDateViaInstant(userEntity.getCreateDate().toLocalDate()).getTime())
                .createDateStr(format.format(convertToDateViaInstant(userEntity.getCreateDate().toLocalDate())))
                .build();
    }

    public Date convertToDateViaInstant(LocalDate dateToConvert) {
        return java.util.Date.from(dateToConvert.atStartOfDay()
                .atZone(ZoneId.systemDefault())
                .toInstant());
    }
}
