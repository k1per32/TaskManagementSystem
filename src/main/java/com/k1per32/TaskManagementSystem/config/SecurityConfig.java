package com.k1per32.TaskManagementSystem.config;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.util.Assert;
import jakarta.servlet.http.HttpServletResponse;


@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true)
public class SecurityConfig {

    @Value("keycloak.resource")
    private String clientId;

    @Value("keycloak.authority-prefix")
    private String authorityPrefix;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.cors(cors -> cors.disable()).csrf(csrf -> csrf.disable())
                .authorizeHttpRequests((authz) -> authz.requestMatchers("/authors/signup", "/performers/signup", "/authors/login", "/performers/login")
                        .permitAll().anyRequest().authenticated())
                .sessionManagement((sessionManagement) -> {
                    sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS);
                }).exceptionHandling(exceptionHandling -> exceptionHandling
                        .authenticationEntryPoint((request, response, ex) -> {
                            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, ex.getMessage());
                        }))
                .oauth2ResourceServer(oauth2 -> oauth2
                        .jwt(jwt -> jwt.jwtAuthenticationConverter(new KeycloakJwtAuthenticationConverter())));

        return http.build();
    }

    class KeycloakJwtAuthenticationConverter extends JwtAuthenticationConverter {

        public KeycloakJwtAuthenticationConverter() {
            KeycloakJwtAuthoritiesConverter grantedAuthoritiesConverter =
                    new KeycloakJwtAuthoritiesConverter();
            grantedAuthoritiesConverter.setAuthorityPrefix(authorityPrefix);

            setJwtGrantedAuthoritiesConverter(grantedAuthoritiesConverter);
            setPrincipalClaimName(clientId);
        }
    }

    class KeycloakJwtAuthoritiesConverter implements Converter<Jwt, Collection<GrantedAuthority>> {

        private String authorityPrefix = "";

        public KeycloakJwtAuthoritiesConverter() {
        }

        public KeycloakJwtAuthoritiesConverter setAuthorityPrefix(String authorityPrefix) {
            Assert.notNull(authorityPrefix, "authorityPrefix cannot be null");
            this.authorityPrefix = authorityPrefix;
            return this;
        }

        @Override
        public Collection<GrantedAuthority> convert(Jwt source) {
            Map<String, Object> realmAccess = source.getClaim("realm_access");
            if (Objects.isNull(realmAccess)) {
                return Collections.emptySet();
            }

            Object roles = realmAccess.get("roles");
            if (Objects.isNull(roles) || !Collection.class.isAssignableFrom(roles.getClass())) {
                return Collections.emptySet();
            }

            Collection<?> rolesCollection = (Collection<?>) roles;

            return rolesCollection.stream().filter(String.class::isInstance)
                    .map(x -> new SimpleGrantedAuthority(authorityPrefix + x)).collect(Collectors.toSet());
        }
    }

}