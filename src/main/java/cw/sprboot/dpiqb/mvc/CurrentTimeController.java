package cw.sprboot.dpiqb.mvc;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collections;

@RequestMapping("/current-time")
@RequiredArgsConstructor
@Controller
public class CurrentTimeController {
  private final CurrentTimeService currentTimeService;
  private final LocalizeService localizeService;
  // @RequestMapping - загальна аннотація, можна налаштувать на пачку запитів
//  @RequestMapping(
//    method = {RequestMethod.GET, RequestMethod.POST, RequestMethod.DELETE},
//    value = {"/current-time", "/another"}
//  )

//  @RequestMapping(method = RequestMethod.GET, value = "current-time")
  // А можно просто @GetMapping
//  @GetMapping({"", "/"})
  @GetMapping({"/get"})
  public ModelAndView getCurrentTime(
    @RequestParam(required = false, name="timezone", defaultValue = "UTC") String timezone
  ){
    ModelAndView result = new ModelAndView("current-time");
//    result.addObject("time", LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
    result.addObject("time", currentTimeService.getCurrentTime(timezone));
    return result;
  }

  @ResponseBody
  @GetMapping("/getAsString")
  public String getCurrentTimeAsString(
      @RequestParam(required = false, name="timezone", defaultValue = "UTC") String timezone
  ){
    return currentTimeService.getCurrentTime(timezone);
  }
  @ResponseBody
  @GetMapping("/getAsObject")
  public CurrentTimeResponse getCurrentTimeAsObject(
      @RequestParam(required = false, name="timezone", defaultValue = "UTC") String timezone
  ){
    try {
      return CurrentTimeResponse.success(currentTimeService.getCurrentTime(timezone));
    }catch (InvaliTimeZoneException ex){
      ex.printStackTrace();
      return CurrentTimeResponse.failed(CurrentTimeResponse.Error.invalidTimezone);
    }
  }
  @GetMapping({"/update"})
  public ModelAndView setCurrentTime(){
    return null;
  }
  @PostMapping("/post-x-form-url-encoded")
  public ModelAndView postCurrentTimeXFormUrlEncoded(String tz){
    ModelAndView result = new ModelAndView("current-time");

    result.addObject("time", currentTimeService.getCurrentTime(tz));
    return result;
  }
  @PostMapping("/post-json")
  public ModelAndView getCurrentTimeJson(
      @RequestBody CurrentTimeRequest request,
      @RequestHeader(value = "Accept-Language", required = false) String acceptLanguage){

    ModelAndView result = new ModelAndView("current-time");

    result.addObject("time", currentTimeService.getCurrentTime(
      request.getTimezone(), request.getFormat()
    ));
    result.addObject("currentTimeLabel", localizeService.getCurrentTimeLabel(acceptLanguage));
    return result;
  }

  @GetMapping("/{timezone}/{format}")
  public ModelAndView getPathVariableTime(
    @PathVariable("timezone") String timezone,
    @PathVariable("format") String format
  ){
    ModelAndView result = new ModelAndView("current-time");
    result.addObject("time", currentTimeService.getCurrentTime(timezone, format));
    result.addObject("currentTimeLabel", localizeService.getCurrentTimeLabel("uk"));
    return result;
  }
}
