package com.thitiwas.reactive.democonnectapi.service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.thitiwas.reactive.democonnectapi.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Calendar;
import java.util.Date;
import java.util.Optional;

@Service
@Slf4j
public class TokenService {

    @Value("${custom.constant.secretLogin}")
    private String secretLogin;

    @Value("${custom.constant.issuer}")
    private String issuer;

    @Value("${custom.claim.id}")
    private String claimId;

    @Value("${custom.claim.type}")
    private String claimType;

    private final ErrorService errorService;

    private final UserRepository userRepository;

    @Autowired
    public TokenService(ErrorService errorService, UserRepository userRepository) {
        this.errorService = errorService;
        this.userRepository = userRepository;
    }

    public Long getClaimId() {
        SecurityContext context = SecurityContextHolder.getContext();
        Authentication authentication = context.getAuthentication();
        DecodedJWT principal = (DecodedJWT) authentication.getPrincipal();
        String id = principal.getClaim(claimId).asString();
        return Long.valueOf(id);
    }

    public String createToken(Long userId) {
        //HMAC
        Algorithm algorithmHS = getAlgorithm(secretLogin);
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MONTH, 1);
        Date dateExpire = calendar.getTime();

        return JWT.create()
                .withClaim(claimId, String.valueOf(userId))
                .withClaim("create_time", String.valueOf(calendar.getTime().getTime()))
                // who create
                .withIssuer(issuer)
                // not expired for now จะไปเช็คใน db เอง
                // .withExpiresAt(dateExpire)
                .sign(algorithmHS);
    }

    /**
     * if verify passed will return decoded else return null
     */
    public DecodedJWT verifyToken(String token) {
        DecodedJWT ret;
        /*try {
            Algorithm algorithm = getAlgorithm(secretLogin);
            JWTVerifier verifier = JWT.require(algorithm)
                    .withIssuer(issuer)
                    .build();
            ret = verifier.verify(token);
            userVerify(ret, token);
        } catch (TokenExpiredException e) {
            log.debug("jwt expired");
            e.printStackTrace();
            ret = null;
        } catch (Exception e) {
            e.printStackTrace();
            ret = null;
        }*/
        Algorithm algorithm = getAlgorithm(secretLogin);
        JWTVerifier verifier = JWT.require(algorithm)
                .withIssuer(issuer)
                .build();
        try {
            ret = verifier.verify(token);
        } catch (Exception e) {
            e.printStackTrace();
            throw errorService.unAuthorized();
        }
        userVerify(ret, token);

        return ret;
    }

    private void userVerify(DecodedJWT ret, String token) {
        /*Long userId = Long.valueOf(ret.getClaim(claimId).asString());
        Optional<UserEntity> optionalMember = userRepository.findById(userId);
        UserEntity userEntity = null;
        if (optionalMember.isPresent()) {
            userEntity = optionalMember.get();
        }
        if (userEntity == null) {
            log.debug("user not found");
            throw errorService.unAuthorized();
        }

        if (userEntity.isDelete()) {
            log.info("user is deleted");
            throw errorService.unAuthorized();
        }
        if (userEntity.getLoginExpired() != null && Calendar.getInstance().getTime().compareTo(userEntity.getLoginExpired()) > 0) {
            log.debug("login expired");
            throw errorService.unAuthorized();
        }
        if (userEntity.getAccessToken() == null) {
            log.debug("token in db is null");
            throw errorService.unAuthorized();
        }
        if (!userEntity.getAccessToken().equals(token)) {
            log.debug("access token is change");
            throw errorService.unAuthorized();
        }*/
    }

    private Algorithm getAlgorithm(String secretLogin) {
        return Algorithm.HMAC256(secretLogin);
    }

}
