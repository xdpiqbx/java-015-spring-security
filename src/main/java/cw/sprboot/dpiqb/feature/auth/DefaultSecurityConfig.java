package cw.sprboot.dpiqb.feature.auth;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@RequiredArgsConstructor
@EnableWebSecurity
public class DefaultSecurityConfig {
  private final CustomAuthProvider authProvider;
  @Bean
  PasswordEncoder passwordEncoder(){
    return PasswordEncoderFactories.createDelegatingPasswordEncoder();
  }
  @Bean
  SecurityFilterChain defaultSecurityFilterChain(HttpSecurity http) throws Exception{
    http
        .authorizeHttpRequests().anyRequest().authenticated().and().httpBasic().and()
//        .requestMatchers(HttpMethod.GET, "/auth/profile").hasAnyRole()
//        .requestMatchers(HttpMethod.GET, "/auth/super-admin").permitAll()
//        .and()
        .formLogin(Customizer.withDefaults());
    return http.build();
  }

  @Autowired
  public void injectCustomAuthProvider(AuthenticationManagerBuilder auth) throws Exception{
    auth.authenticationProvider(authProvider);
  }
}
