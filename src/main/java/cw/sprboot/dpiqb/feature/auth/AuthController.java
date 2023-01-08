package cw.sprboot.dpiqb.feature.auth;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import org.springframework.security.core.userdetails.User;

@RequestMapping("/auth")
@Controller
public class AuthController {
  @GetMapping("/profile")
  public ModelAndView get(){
    ModelAndView result = new ModelAndView("auth-page");

    SecurityContext context = SecurityContextHolder.getContext();
    Authentication authentication = context.getAuthentication();
    User principal = (User) authentication.getPrincipal();

    String username = authentication.getName();
    result.addObject("username", username);

    System.out.println("Thread.currentThread().getId() = " + Thread.currentThread().getId());

    return result;
  }
}
