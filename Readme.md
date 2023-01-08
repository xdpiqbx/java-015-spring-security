# java-015-spring-security

```java
ThreadLocal<String> name = new ThreadLocal<>();
// allows to store data that will be accessible only by a specific thread
// ThreadLocal has his own different data in different threads
name.set("In default thread");
new Thread(() -> {
    name.set("In new thread");
    System.out.println("Finished: " + name.get());
}).start();
Thread.sleep(1000);
System.out.println("name = " + name.get());
```
```console
Finished: In new thread
name = In default thread
```

---

```gradle
implementation 'org.springframework.boot:spring-boot-starter-security'
testImplementation 'org.springframework.security:spring-security-test'
implementation 'org.thymeleaf.extras:thymeleaf-extras-springsecurity6'
```
---
### За замовчуванням:
- Кожен HTTP запит виконується в окремомму потоці (Tomcat за замовчуванням їх 200)
- Дані про автентифікацію отриуються із `SecurityContextHolder.getContext().getAuthentication()`
- `SecurityContextHolder` теж за замовчуванням завязаний на багатопоточку і для кожного потоку вертає окремий контекст
---
```java
@RequestMapping("/auth")
@Controller
public class AuthController {
  @GetMapping("/profile")
  public ModelAndView get(){
    ModelAndView result = new ModelAndView("auth-page");

    SecurityContext context = SecurityContextHolder.getContext();
    Authentication authentication = context.getAuthentication();

    System.out.println("authentication.getCredentials() = " + authentication.getCredentials());
    // authentication.getCredentials() = null

    User principal = (User) authentication.getPrincipal();

    String username = authentication.getName();
    result.addObject("username", username);

    return result;
  }
}
```
## `Authentication authentication = context.getAuthentication();`
```java
System.out.println("authentication = " + authentication);
//  authentication =
//    UsernamePasswordAuthenticationToken [
//      Principal=org.springframework.security.core.userdetails.User [
//        Username=user,
//        Password=[PROTECTED],
//        Enabled=true,
//        AccountNonExpired=true,
//        credentialsNonExpired=true,
//        AccountNonLocked=true,
//        Granted Authorities=[]
//      ],
//      Credentials=[PROTECTED],
//      Authenticated=true,
//      Details=WebAuthenticationDetails [
//        RemoteIpAddress=0:0:0:0:0:0:0:1,
//        SessionId=C32EB1022F90836D568C446E665620E8
//      ],
//      Granted Authorities=[]
//    ]
```
## `User principal = (User) authentication.getPrincipal();`
```java
User principal = (User) authentication.getPrincipal();
System.out.println("authentication principal = " + principal);
//  authentication principal = 
//    org.springframework.security.core.userdetails.User [
//      Username=user,
//      Password=[PROTECTED],
//      Enabled=true,
//      AccountNonExpired=true,
//      credentialsNonExpired=true,
//      AccountNonLocked=true,
//      Granted Authorities=[]
//    ]

System.out.println("principal.getUsername() = " + principal.getUsername());
System.out.println("principal.getAuthorities() = " + principal.getAuthorities());
System.out.println("principal.getPassword() = " + principal.getPassword());
System.out.println("principal.getClass() = " + principal.getClass());
//  principal.getUsername() = user
//  principal.getAuthorities() = []
//  principal.getPassword() = null
//  principal.getClass() = class org.springframework.security.core.userdetails.User
```
