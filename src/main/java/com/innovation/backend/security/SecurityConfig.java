package com.innovation.backend.security;


import com.innovation.backend.security.cors.CorsConfig;
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

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

  @Value("${jwt.secret}")
  String SECRET_KEY;
  private final TokenProvider tokenProvider;
  private final UserDetailsServiceImpl userDetailsService;
private final CorsConfig corsConfig;
  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }

  @Bean
  public WebSecurityCustomizer ignoringCustomizer() {
    return (web) -> web.ignoring().antMatchers("/h2-console/**", "/ws");
  }

  @Bean
  public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
    http.addFilter(corsConfig.corsFilter());

    http.csrf().disable()
        .sessionManagement()
        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
        .and()
        .authorizeRequests()
//                .requestMatchers(CorsUtils::isPreFlightRequest).permitAll()
        .antMatchers("/**").permitAll()
        .antMatchers("/h2-console/**", "/ws").permitAll()
        .antMatchers("/google", "/oauth2/**", "/css/**", "/images/**", "/js/**", "/favicon.ico/**")
        .permitAll()
        .antMatchers("/login/**").permitAll()
        .antMatchers("/main/**").permitAll()
        .antMatchers("/board", "/board/{boardTagName}", "/board/heart/{boardId}, /board/tag")
        .permitAll()
        .antMatchers("/zeroshop/offline").permitAll()
        .antMatchers("/subscribe/**").permitAll()
        .antMatchers("/test/**").permitAll()
        .antMatchers(HttpMethod.GET, "/meeting/**").permitAll()
        .antMatchers("/swagger-ui/**", "/v3/api-docs/**").permitAll()
        .antMatchers(HttpMethod.GET, "/review/**").permitAll()
        .antMatchers("/meeting/tag").permitAll()
        .anyRequest().authenticated()
        .and()
        .exceptionHandling()
        .authenticationEntryPoint(new CustomAuthenticationEntryPoint())
        .and()
        .apply(new JwtSecurityConfig(SECRET_KEY, tokenProvider, userDetailsService));
    return http.build();
  }
}
