package com.thitiwas.reactive.democonnectapi.controller;

import com.auth0.jwt.interfaces.DecodedJWT;
import com.thitiwas.reactive.democonnectapi.exception.CustomErrorException;
import com.thitiwas.reactive.democonnectapi.model.CommonConstant;
import com.thitiwas.reactive.democonnectapi.model.ErrorObj;
import com.thitiwas.reactive.democonnectapi.model.ResponseWrapper;
import com.thitiwas.reactive.democonnectapi.model.UserModel;
import com.thitiwas.reactive.democonnectapi.security.AuthTokenService;
import com.thitiwas.reactive.democonnectapi.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

@RestController
@RequestMapping("/api/v1")
@Slf4j
public class UserController {
    private final UserService userService;

    private final AuthTokenService authTokenService;

    @Autowired
    public UserController(UserService userService, AuthTokenService authTokenService) {
        this.userService = userService;
        this.authTokenService = authTokenService;
    }

    @GetMapping("/user/test")
    public Mono<String> test() {
        return Mono.just("test");
    }

    @GetMapping("/user/all")
    public Flux<UserModel> testGetAllUser() {
        return userService.getAllUser();
    }

    @GetMapping("/user/{id}")
    public Mono<ResponseWrapper<UserModel>> testGetUserById(@PathVariable String id) {
        Mono<UserModel> byId = userService.getById(id);
        return byId
                .flatMap(userEntity -> Mono.just(ResponseWrapper.<UserModel>builder()
                .objectValue(userEntity)
                .build()));
    }

    @GetMapping("/user/telno/{telno}")
    public Mono<ResponseWrapper<UserModel>> testGetUserByTelno(@PathVariable String telno) {
        Mono<UserModel> byId = userService.findByTelno(telno);
        return byId
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
                .flatMap(userEntity -> Mono.just(ResponseWrapper.<UserModel>builder()
                        .objectValue(userEntity)
                        .build()));
    }

    @GetMapping("/s/user/all")
    public Mono<UserDetails> testGetAllUsers() {
        return ReactiveSecurityContextHolder.getContext()
                .map(SecurityContext::getAuthentication)
                .doOnNext(authentication -> {
                    DecodedJWT principal = (DecodedJWT) authentication.getPrincipal();
                    log.info("authentication.getDetails() {}", principal.getClaim("id").asString());
                })
                .map(authentication -> (UserDetails) authentication.getDetails());
    }
}
