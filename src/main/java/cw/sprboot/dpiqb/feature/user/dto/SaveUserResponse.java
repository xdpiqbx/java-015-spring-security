package cw.sprboot.dpiqb.feature.user.dto;

import lombok.Builder;
import lombok.Data;
@Builder
@Data
public class SaveUserResponse {
  private boolean success;
  private Error error;
  public enum Error{
    ok, invalidEmail
  }
  public static SaveUserResponse success(){
    return SaveUserResponse.builder().success(true).error(Error.ok).build();
  }
  public static SaveUserResponse failed(Error error){
    return SaveUserResponse.builder().success(false).error(error).build();
  }
}
