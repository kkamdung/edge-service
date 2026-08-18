package com.polarbookshop.edgeservice.user;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
//import org.springframework.security.core.context.ReactiveSecurityContextHolder;
//import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import java.util.List;

@RestController
public class UserController {

    // 방법 1
//    @GetMapping("/user")
//    public Mono<User> getUser() {
//        return ReactiveSecurityContextHolder.getContext()
//                .mapNotNull(SecurityContext::getAuthentication)
//                .mapNotNull(authentication -> (OidcUser) authentication.getPrincipal())
//                .map(oidcUser -> new User(
//                        oidcUser.getPreferredUsername(),
//                        oidcUser.getGivenName(),
//                        oidcUser.getFamilyName(),
//                        List.of("employee", "customer")
//                ));
//    }

    // 방법 2
    @GetMapping("/user")
    public Mono<User> getUser(@AuthenticationPrincipal OidcUser oidcUser) {
        User user = new User(
                oidcUser.getPreferredUsername(),
                oidcUser.getGivenName(),
                oidcUser.getFamilyName(),
                List.of("employee", "customer"));

        return Mono.just(user);
    }

}
