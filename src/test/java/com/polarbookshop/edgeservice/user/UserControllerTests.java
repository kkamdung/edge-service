package com.polarbookshop.edgeservice.user;

import com.polarbookshop.edgeservice.config.SecurityConfig;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webflux.test.autoconfigure.WebFluxTest;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Import;
import org.springframework.security.oauth2.client.registration.ReactiveClientRegistrationRepository;
import org.springframework.security.oauth2.core.oidc.StandardClaimNames;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.security.test.web.reactive.server.SecurityMockServerConfigurers.*;

@WebFluxTest(UserController.class)
@Import(SecurityConfig.class)
class UserControllerTests {

    @Autowired
    private WebTestClient webClient;

    @MockitoBean
    private ReactiveClientRegistrationRepository clientRegistrationRepository;

    @Autowired
    private ApplicationContext context;

    @BeforeEach
    public void setUp() {
        webClient = WebTestClient
                .bindToApplicationContext(context)
                .apply(springSecurity())
                .build();
    }

    @Test
    void whenNotAuthenticatedThen401() {
        webClient.get()
                .uri("/user")
                .exchange()
                .expectStatus().isUnauthorized();
    }

    @Test
    void whenAuthenticatedThenReturnUser() {
        var expectedUser = new User("jon.snow", "jon", "snow", List.of("employee", "customer"));

        webClient.mutateWith(configureMockOidcLogin(expectedUser))
                .get()
                .uri("/user")
                .exchange()
                .expectStatus().is2xxSuccessful()
                .expectBody(User.class)
                .value(user -> assertThat(user).isEqualTo(expectedUser));

    }

    private OidcLoginMutator configureMockOidcLogin(User expectedUser) {
        return mockOidcLogin().idToken(builder ->
            builder.claim(StandardClaimNames.PREFERRED_USERNAME, expectedUser.username())
                    .claim(StandardClaimNames.GIVEN_NAME, expectedUser.firstName())
                    .claim(StandardClaimNames.FAMILY_NAME, expectedUser.lastName())
                    .claim("roles", expectedUser.roles()));
    }

}
