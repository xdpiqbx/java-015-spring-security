package cw.sprboot.dpiqb.feature.auth;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.Objects;

@RequiredArgsConstructor
@Service
public class CustomAuthProvider implements AuthenticationProvider {
  private final CustomUserDetailsService userDetailsService;
  private final PasswordEncoder passwordEncoder; // Bean from DefaultSecurityConfig

//  private BCryptPasswordEncoder passwordEncoder;
//  @PostConstruct
//  public void init(){
//    passwordEncoder = new BCryptPasswordEncoder();
//  }

  @Override
  public Authentication authenticate(Authentication authentication) throws AuthenticationException {
    // authentication - те що сконструює spring у відповідь на наш логін та пароль

    String username = authentication.getName();
    String password = authentication.getCredentials().toString();
    System.out.println("password = " + password);

    UserDetails user = userDetailsService.loadUserByUsername(username);
    return checkPassword(user, password);
  }

  @Override
  public boolean supports(Class<?> authentication) {
    return  UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication);
  }

  private Authentication checkPassword(UserDetails user, String rawPassword){
//    if(Objects.equals(rawPassword, user.getPassword())){
    if(passwordEncoder.matches(rawPassword, user.getPassword())){
      User innerUser = new User(
          user.getUsername(),
          user.getPassword(),
          user.getAuthorities()
      );
      return new UsernamePasswordAuthenticationToken(innerUser, user.getPassword(), user.getAuthorities());
    }else{
      throw new BadCredentialsException("Bad credentials");
    }
  }
}
