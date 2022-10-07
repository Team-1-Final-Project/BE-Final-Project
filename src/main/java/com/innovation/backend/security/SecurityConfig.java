package com.innovation.backend.security;


import com.innovation.backend.security.jwt.JwtSecurityConfig;
import com.innovation.backend.global.exception.CustomAuthenticationEntryPoint;
import com.innovation.backend.security.jwt.TokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {
    @Value("${jwt.secret}")
    String SECRET_KEY;
    private final TokenProvider tokenProvider;
    private final UserDetailsServiceImpl userDetailsService;
    private final CorsConfigurationSource corsConfigurationSource;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public WebSecurityCustomizer ignoringCustomizer() { return (web) -> web.ignoring().antMatchers("/h2-console/**"); }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.cors();
        http.cors().configurationSource(request -> {
            var cors = new CorsConfiguration();
            cors.setAllowedOrigins(List.of("http://earth-us.s3-website.ap-northeast-2.amazonaws.com","http://localhost:3000","https://accounts.google.com/o/oauth2/v2/**")); // 허용할 URL
            cors.setAllowedMethods(List.of("GET","POST", "PUT", "DELETE", "OPTIONS")); // 허용할 Http Method
            cors.setAllowedHeaders(List.of("*")); // 허용할 Header
            cors.addExposedHeader("Authorization");
            cors.addExposedHeader("Refresh-Token");
            cors.setAllowCredentials(true);
            return cors;
        });

        http.csrf().disable()
            .sessionManagement()
            .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            .and()
            .authorizeRequests()
//                .requestMatchers(CorsUtils::isPreFlightRequest).permitAll()
            .antMatchers("/").permitAll()
            .antMatchers("/h2-console/**").permitAll()
            .antMatchers("/google","/oauth2/**", "/css/**","/images/**","/js/**","/favicon.ico/**").permitAll()
            .antMatchers("/login/**").permitAll()
            .antMatchers("/main/**").permitAll()
            .antMatchers("/board" , "/board/{boardTagName}","/board/heart/{boardId}").permitAll()
            .antMatchers("/zeroshop/offline").permitAll()
            .antMatchers(HttpMethod.GET,"/meeting/**").permitAll()
            .antMatchers("/swagger-ui/**").permitAll()
            .antMatchers(HttpMethod.GET,"/review/**").permitAll()
            .anyRequest().authenticated()
            .and()
            .exceptionHandling()
            .authenticationEntryPoint(new CustomAuthenticationEntryPoint())
            .and()
            .apply(new JwtSecurityConfig(SECRET_KEY, tokenProvider, userDetailsService));
        return http.build();
    }

    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();

        configuration.addAllowedOrigin("http://localhost:3000");
        configuration.addAllowedOrigin("");
        configuration.addAllowedHeader("*");
        configuration.addAllowedMethod("*");
        configuration.setAllowCredentials(true);
        // 클라이언트가 응답에 접근 가능하도록 헤더 전송
        configuration.addExposedHeader("Authorization");
        configuration.addExposedHeader("Refresh-Token");

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;

    }

}
