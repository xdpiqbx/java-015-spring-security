package cw.sprboot.dpiqb.feature.user.dto;

import lombok.Data;

import java.util.List;

@Data
public class UserInfoDTO {
  private UserDTO userDTO;
  private List<String> addresses;
}
