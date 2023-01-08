package cw.sprboot.dpiqb.mvc;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@RequiredArgsConstructor
@RequestMapping("/api/v1/time")
@RestController
public class TimeApiController {
  private final CurrentTimeService timeService;
  @PostMapping
  public CurrentTimeResponse get(@RequestBody CurrentTimeRequest request){
    try{
      String time = timeService.getCurrentTime(
          request.getTimezone(),
          request.getFormat()
      );
      return CurrentTimeResponse.success(time);
    }catch (InvaliTimeZoneException ex){
      ex.printStackTrace();
      return CurrentTimeResponse.failed(CurrentTimeResponse.Error.invalidTimezone);
    }
  }

  @GetMapping("/download/v1")
  public ResponseEntity<ByteArrayResource> download1(@RequestParam(value = "timezone", required = false) String timezone){
    String currentTime = timeService.getCurrentTime(timezone);
    byte[] bytes = currentTime.getBytes(StandardCharsets.UTF_8);
    ByteArrayResource resource = new ByteArrayResource(bytes);
    String filename = "time-"+ LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME)+ ".json";
    return ResponseEntity.ok()
        .header(HttpHeaders.CONTENT_DISPOSITION, "attachment;filename=\""+filename+"\"")
        .contentType(MediaType.APPLICATION_JSON)
        .contentLength(bytes.length)
        .body(resource);
  }

//  @GetMapping(value = "/download/v2", consumes = {"application/json"})
  @GetMapping(value = "/download/v2")
  public String download2(@RequestParam(value = "timezone", required = false) String timezone,
                          HttpServletRequest request,
                          HttpServletResponse response
  ){
    String currentTime = timeService.getCurrentTime(timezone);
    byte[] bytes = currentTime.getBytes(StandardCharsets.UTF_8);
    String filename = "time-"+ LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME)+ ".json";

    response.addHeader(HttpHeaders.CONTENT_DISPOSITION, "attachment;filename=\""+filename+"\"");
    response.setContentType(MediaType.APPLICATION_JSON.toString());
    response.setContentLength(bytes.length);

    return currentTime;
  }
}
