package com.mindconnect.mindconnect.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.CookieClearingLogoutHandler;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;
import java.util.List;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfiguration {

    private final JwtAuthenticationFilter jwtAuthFilter;
    private final AuthenticationProvider authenticationProvider;
    private final LogoutConfig logoutConfig;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception{
        CookieClearingLogoutHandler cookies = new CookieClearingLogoutHandler("our-custom-cookie");
        httpSecurity
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(requests -> requests
                        .requestMatchers("/auth/**",
                                "auth/login",
                                "/swagger-ui.html",
                                "/swagger-ui.html",
                                "/webjars/**",
                                "/swagger-ui/**",
                                "configuration/security",
                                "configuration/ui",
                                "/swagger-resources/**",
                                "/swagger-resources",
                                "/v2/api-docs",
                                "/v3/api-docs",
                                "/health",
                                "/v3/api-docs/**",
                                "/mind_connect-ws/**")
                        .permitAll())
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .authorizeHttpRequests(request -> request.anyRequest().authenticated())
                .sessionManagement(request -> request.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authenticationProvider(authenticationProvider)
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)
                .logout(logoutConfigurer -> logoutConfigurer
                        .logoutUrl("/auth/logout")
                        .addLogoutHandler(logoutConfig)
                        .addLogoutHandler(cookies)
                        .logoutSuccessHandler((request, response, authentication) -> SecurityContextHolder.clearContext()
                        ));

        return httpSecurity.build();
    }
    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(List.of(
                "http://localhost:3000",
                "https://mind-connect.decagon.dev"
        ));
        configuration.setAllowedMethods(Arrays.asList(
                HttpMethod.GET.name(),
                HttpMethod.POST.name(),
                HttpMethod.PATCH.name(),
                HttpMethod.DELETE.name()
        ));
        configuration.setAllowCredentials(true);
        configuration.setAllowedHeaders(List.of(
                "X-CSRF-TOKEN",
                HttpHeaders.AUTHORIZATION,
                HttpHeaders.ACCEPT,
                HttpHeaders.CONTENT_TYPE
        ));
        configuration.setAllowedOriginPatterns(List.of(
                "/**"
        ));
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}
