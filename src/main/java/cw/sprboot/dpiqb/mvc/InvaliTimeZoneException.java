package cw.sprboot.dpiqb.mvc;

public class InvaliTimeZoneException extends RuntimeException{
  public InvaliTimeZoneException(String timezone) {
    super("Invalid timezone: " + timezone);
  }
}
