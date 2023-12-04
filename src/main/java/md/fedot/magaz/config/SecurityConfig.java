package md.fedot.magaz.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .authorizeHttpRequests(
                        (authorizationManagerRequestMatchers -> authorizationManagerRequestMatchers
                                .requestMatchers(HttpMethod.GET, "/products").permitAll()
                                .requestMatchers("/**").authenticated()))
                .build();
    }

}


