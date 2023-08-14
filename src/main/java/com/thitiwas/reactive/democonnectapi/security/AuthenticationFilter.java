//package com.thitiwas.reactive.democonnectapi.security;
//
//import com.auth0.jwt.interfaces.DecodedJWT;
//import com.thitiwas.reactive.democonnectapi.service.ErrorService;
//import com.thitiwas.reactive.democonnectapi.service.TokenService;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
//import org.springframework.security.core.GrantedAuthority;
//import org.springframework.security.core.authority.SimpleGrantedAuthority;
//import org.springframework.security.core.context.SecurityContext;
//import org.springframework.security.core.context.SecurityContextHolder;
//import org.springframework.web.filter.OncePerRequestFilter;
//import java.io.IOException;
//import java.util.ArrayList;
//import java.util.List;
//
//@Slf4j
//public class AuthenticationFilter extends OncePerRequestFilter implements Filter  {
//
//    private final TokenService tokenService;
//    private final HandlerExceptionResolver handlerExceptionResolver;
//
//    private final ErrorService errorService;
//
//
//
//    private String claimType = "type";
//    private String claimId = "id";
//
//    public AuthenticationFilter(TokenService tokenService, ErrorService errorService , HandlerExceptionResolver handlerExceptionResolver) {
//        this.tokenService = tokenService;
//        this.handlerExceptionResolver = handlerExceptionResolver;
//        this.errorService = errorService;
//    }
//
//    @Override
//    protected void doFilterInternal(HttpServletRequest req, HttpServletResponse res, FilterChain chain) {
//        HttpServletResponse response = (HttpServletResponse) res;
//        HttpServletRequest request = (HttpServletRequest) req;
//        response.setHeader("Access-Control-Allow-Origin", "*");
//        response.setHeader("Access-Control-Allow-Methods", "POST, GET, PUT, OPTIONS, DELETE");
//        response.setHeader("Access-Control-Max-Age", "3600");
//        response.setHeader("Access-Control-Allow-Headers",
//                "X-PINGOTHER,Content-Type,X-Requested-With,accept,Origin,Access-Control-Request-Method,Access-Control-Request-Headers,Authorization");
//        response.setHeader("Access-Control-Expose-Headers", "xsrf-token");
//
//        String authorization = request.getHeader("Authorization");
//        if (authorization == null) {
//            handlerExceptionResolver.resolveException(request, response, null, errorService.unAuthorized());
//            return;
//        }
//        // check authorization type
//        if (!authorization.startsWith("Bearer ") && !authorization.startsWith("bearer ")) {
//            handlerExceptionResolver.resolveException(request, response, null, errorService.unAuthorized());
//            return;
//        }
//
//        String token = authorization.substring(7);
//        // if verify passed will return decoded else return null
//        DecodedJWT decoded = null;
//        try {
//            decoded = tokenService.verifyToken(token);
//        } catch (Exception e) {
//            e.printStackTrace();
//            handlerExceptionResolver.resolveException(request, response, null, e);
//            return;
//        }
//        // log.info("decoded :{}", decoded);
//        if (decoded == null) {
//            handlerExceptionResolver.resolveException(request, response, null, errorService.unAuthorized());
//            return;
//        }
//
//        String userType = decoded.getClaim(claimId).asString();
//        // log.debug("userType :{}", userType);
//        List<GrantedAuthority> grantedAuthorities = new ArrayList<>();
//        grantedAuthorities.add(new SimpleGrantedAuthority(userType));
//        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken =
//                new UsernamePasswordAuthenticationToken(
//                        decoded,
//                        token,
//                        grantedAuthorities);
//        SecurityContext context = SecurityContextHolder.getContext();
//        context.setAuthentication(usernamePasswordAuthenticationToken);
//
//
//        try {
//            chain.doFilter(request, response);
//        } catch (IOException | ServletException e) {
//            handlerExceptionResolver.resolveException(request, response, null, e);
//        }
//    }
//}
