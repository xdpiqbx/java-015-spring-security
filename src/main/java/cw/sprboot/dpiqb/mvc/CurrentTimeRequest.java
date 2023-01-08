package cw.sprboot.dpiqb.mvc;

import lombok.Data;

@Data
public class CurrentTimeRequest {
  private String timezone;
  private String format;
}
