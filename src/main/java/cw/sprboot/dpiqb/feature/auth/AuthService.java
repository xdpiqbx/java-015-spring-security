package cw.sprboot.dpiqb.feature.auth;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {
  public boolean hasAuthority(String authority){
    return getUser().getAuthorities().stream()
        .map(GrantedAuthority::getAuthority)
        .anyMatch(it -> it.equals(authority));
  }
  public String getUserName(){
    return getUser().getUsername();
  }
  private User getUser(){
    return (User) getAuthentication().getPrincipal();
  }
  private Authentication getAuthentication(){
    SecurityContext context = SecurityContextHolder.getContext();
    Authentication authentication = context.getAuthentication();
    System.out.println("AuthService authentication = " + authentication);
    return authentication;
  }
}
