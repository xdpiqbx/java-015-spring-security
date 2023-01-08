package cw.sprboot.dpiqb.feature.auth;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Map;

@Service
public class CustomUserDetailsService implements UserDetailsService {
  private static final Map<String, String> USERS = Map.of(
      "user", "1111",
      "admin", "aaaa"
  );
  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    if(!USERS.containsKey(username)){
      throw new UsernameNotFoundException(username);
    }

    return new UserDetails() {
      @Override
      public Collection<? extends GrantedAuthority> getAuthorities() {
        return null;
      }

      @Override
      public String getPassword() {
        return null;
      }

      @Override
      public String getUsername() {
        return null;
      }

      @Override
      public boolean isAccountNonExpired() {
        return false;
      }

      @Override
      public boolean isAccountNonLocked() {
        return false;
      }

      @Override
      public boolean isCredentialsNonExpired() {
        return false;
      }

      @Override
      public boolean isEnabled() {
        return false;
      }
    };
  }
}
