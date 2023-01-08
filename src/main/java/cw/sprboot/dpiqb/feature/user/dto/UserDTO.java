package cw.sprboot.dpiqb.feature.user.dto;

import cw.sprboot.dpiqb.feature.user.Gender;
import cw.sprboot.dpiqb.feature.user.User;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

@Builder
@Data
public class UserDTO {
  private String email;
  private String fullName;
  private LocalDate birthday;
  private int age;
  private Gender gender;

  public static UserDTO toDTO(User user){
    int age = (int)ChronoUnit.YEARS.between(user.getBirthday(), LocalDate.now());
    return UserDTO.builder()
      .email(user.getEmail())
      .fullName(user.getFullName())
      .birthday(user.getBirthday())
      .gender(user.getGender())
      .age(age)
      .build();
  }

  public static User fromDTO(UserDTO userDTO){

    User user = new User();
    user.setEmail(userDTO.getEmail());
    user.setFullName(userDTO.getFullName());
    user.setGender(userDTO.getGender());
    user.setBirthday(userDTO.getBirthday());
    return user;

//    @Builder
//    public class User {...}
//    return User.builder()
//      .email(userDTO.getEmail())
//      .fullName(userDTO.getFullName())
//      .gender(userDTO.getGender())
//      .birthday(userDTO.getBirthday())
//      .build();
  }
}
