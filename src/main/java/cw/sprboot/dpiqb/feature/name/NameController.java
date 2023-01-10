package cw.sprboot.dpiqb.feature.name;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/api/v1")
@RestController
public class NameController {
  @Value("${appName}") // SpEL
  private String appName;
  @Value("${dbURL}") // SpEL
  private String dbURL;
  @Value("${userDB}") // SpEL
  private String userDB;
  @Value("${passDb}") // SpEL
  private String passDb;

  @GetMapping("/name")
  public String getName(){
    return appName+" - "+dbURL+" - "+userDB+" - "+passDb;
    // use -D param before every variable
    // java -DAPP_NAME=Application -DDB_URL=/some-db-url -DDB_USER=admin -DDB_PASS=@2ev%267 -jar spring-boot-app.jar
  }
}
