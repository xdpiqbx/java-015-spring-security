package cw.sprboot.dpiqb.feature.auth;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@EnableWebSecurity
@Service
public class DefaultSecurityConfig {
  private final CustomAuthProvider authProvider;
//  @Bean
//  PasswordEncoder passwordEncoder(){
//    return PasswordEncoderFactories.createDelegatingPasswordEncoder();
//  }
  @Bean
  SecurityFilterChain defaultSecurityFilterChain(HttpSecurity http) throws Exception{

    http
      .authorizeHttpRequests(
        auth -> auth
        .requestMatchers("/api/v1/**").permitAll()
        .anyRequest().authenticated()
      )
      .httpBasic(Customizer.withDefaults())
      .formLogin(Customizer.withDefaults());
    return http.build();
  }

  @Autowired
  public void injectCustomAuthProvider(AuthenticationManagerBuilder auth) throws Exception{
    auth.authenticationProvider(authProvider);
  }
}
