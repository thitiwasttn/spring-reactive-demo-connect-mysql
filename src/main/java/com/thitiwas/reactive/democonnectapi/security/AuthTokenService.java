/*
 * Copyright 2019-Current jittagornp.me
 */
package com.thitiwas.reactive.democonnectapi.security;

import com.auth0.jwt.interfaces.DecodedJWT;
import reactor.core.publisher.Mono;

import java.util.Map;

/**
 * @author jitta
 */
public interface AuthTokenService {

    Mono<String> sign(final Map<String, Object> claims);

    Mono<Map<String, Object>> verify(final String token);
    Mono<DecodedJWT> verifyV2(final String token);

    Mono<Long> getClaimId();

}
