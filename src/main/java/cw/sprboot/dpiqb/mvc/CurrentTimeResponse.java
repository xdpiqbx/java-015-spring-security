package cw.sprboot.dpiqb.mvc;

import lombok.Builder;
import lombok.Data;
@Builder
@Data
public class CurrentTimeResponse {
  private boolean success;
  private Error error;
  public String time;

  public enum Error{
    ok, invalidTimezone
  }
  public static final CurrentTimeResponse success (String time){
    return CurrentTimeResponse.builder().success(true).error(Error.ok).time(time).build();
  };
  public static final CurrentTimeResponse failed (Error error){
    return CurrentTimeResponse.builder().success(false).error(error).build();
  };
}
