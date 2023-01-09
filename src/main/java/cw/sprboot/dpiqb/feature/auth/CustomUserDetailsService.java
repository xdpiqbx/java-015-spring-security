package cw.sprboot.dpiqb.feature.auth;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;

@Service
public class CustomUserDetailsService implements UserDetailsService {
  private static final Map<String, String> USERS = Map.of(
      "user", "user",
      "admin", "admin"
  );
  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

    if(!USERS.containsKey(username)){
      throw new UsernameNotFoundException(username);
    }

    UserDetails result = new UserDetails() {
      @Override
      public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singleton((GrantedAuthority) () -> username);
      }

      @Override
      public String getPassword() {
        return USERS.get(username);
      }

      @Override
      public String getUsername() {
        return username;
      }

      @Override
      public boolean isAccountNonExpired() {
        return true;
      }

      @Override
      public boolean isAccountNonLocked() {
        return true;
      }

      @Override
      public boolean isCredentialsNonExpired() {
        return true;
      }

      @Override
      public boolean isEnabled() {
        return true;
      }
    };
    System.out.println("Creating user: CustomUserDetailsService");
    System.out.println("result.getPassword() = " + result.getPassword());
    System.out.println("result.getUsername() = " + result.getUsername());
    return result;
  }
}
