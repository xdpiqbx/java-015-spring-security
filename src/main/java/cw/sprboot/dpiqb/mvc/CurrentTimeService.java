package cw.sprboot.dpiqb.mvc;

import org.springframework.stereotype.Service;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Date;
import java.util.TimeZone;

@Service
public class CurrentTimeService {
  public String getCurrentTime(String timezone) throws InvaliTimeZoneException{
    return getCurrentTime(timezone, "yyyy.MM.dd hh:mm:ss");
  }
  public String getCurrentTime(String timezone, String format) throws InvaliTimeZoneException{
    if(timezone == null){
      return LocalDateTime.now().format(
        DateTimeFormatter.ofPattern(format)
      );
    }
    if(!isTimezoneExists(timezone)){
      throw new InvaliTimeZoneException(timezone);
    }
    Date date = new Date();
    DateFormat df = new SimpleDateFormat(format);
    df.setTimeZone(TimeZone.getTimeZone(timezone));
    return df.format(date);
  }

  private boolean isTimezoneExists(String timezone){
    return Arrays.asList(TimeZone.getAvailableIDs()).contains(timezone);
  }
}
