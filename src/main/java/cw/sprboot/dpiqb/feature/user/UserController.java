package cw.sprboot.dpiqb.feature.user;

import cw.sprboot.dpiqb.feature.user.dto.DeleteUserResponse;
import cw.sprboot.dpiqb.feature.user.dto.SaveUserResponse;
import cw.sprboot.dpiqb.feature.user.dto.UserDTO;
import cw.sprboot.dpiqb.feature.user.dto.UserInfoDTO;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@RequestMapping("/api/v1/user")
@RestController
public class UserController {
  private final UserService userService;
  private final UserValidateService userValidateService;
  @GetMapping("/list")
  public List<UserDTO> list(){
    return userService.findAll().stream().map(user -> UserDTO.toDTO(user)).collect(Collectors.toList());
  }

  @PostMapping("/save")
  public SaveUserResponse save(@RequestBody UserDTO userDTO){
    if(!userValidateService.isEmailValid(userDTO.getEmail())){
      return SaveUserResponse.failed(SaveUserResponse.Error.invalidEmail);
    }
    User user = UserDTO.fromDTO(userDTO);
    userService.save(user);
    return SaveUserResponse.success();
  }

  @DeleteMapping("/delete/{email}")
  public DeleteUserResponse delete(
    @PathVariable(name = "email", required = false) String email,
    HttpServletResponse response
  ){
    if(!userService.exists(email)){
      response.setStatus(HttpServletResponse.SC_NOT_FOUND);
      return DeleteUserResponse.failed(DeleteUserResponse.Error.userNotFound);
    }
    userService.deleteById(email);
    return DeleteUserResponse.success();
  }
  @DeleteMapping("/deleteAll")
  public DeleteUserResponse deleteAll(@RequestBody List<String> emails){
    // Якщо є хочаб один неіснуючий email - Error.userNotFound
    boolean thereAreNonExistingEmails = emails == null || emails.stream().anyMatch(email -> !userService.exists(email));
    if (thereAreNonExistingEmails) {
      return DeleteUserResponse.failed(DeleteUserResponse.Error.userNotFound);
    }
    userService.deleteByIds(emails);
    return DeleteUserResponse.success();
  }
  @GetMapping("/search")
  public List<UserDTO> search(
    @RequestParam(name = "query", required = false) String query,
    HttpServletResponse response
  ){
    if(!userValidateService.isSearchQueryValid(query)){
      response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
      return Collections.emptyList();
    }
    return userService.search(query).stream().map(UserDTO::toDTO).toList();
  }
  @GetMapping("/countOlderThan/{age}")
  public int countPeopleOlderThan(@PathVariable(name = "age") int age){
    return userService.countPeopleOlderThan(age);
  }

  @GetMapping("/info/{email}")
  public UserInfoDTO getUserInfo(@PathVariable(name = "email") String email){
    return userService.getUserInfoV2(email);
  }

  @GetMapping("/between")
  public List<User> getUserBetween(
        @RequestParam("start")LocalDate start,
        @RequestParam("end")LocalDate end
      ){
    return userService.getUserBetween(start, end);
  }
}
