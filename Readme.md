# java-015-spring-security

<img src="./imgs/004 sec 001.png" width="600"/>

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

<img src="./imgs/007 sec 004.png" width="600"/>

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

<img src="./imgs/005 sec 002.png" width="600"/>

---

<img src="./imgs/006 sec 003.png" width="600"/>

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
---
## UserDetailsService

>Де беремо юзерів

Коли `Spring` бачить імплементацію `UserDetailsService` то він починає юзати саме її і перестає створювати дефолтного юзера з довгим паролем

`public class CustomUserDetailsService implements UserDetailsService {...}`
```java
@Service
public class CustomUserDetailsService implements UserDetailsService {
  // для простоти тут Map але реально тут буде робота з базод даних
  private static final Map<String, String> USERS = Map.of(
      "user", "1111",
      "admin", "aaaa"
  );
  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    // відразу перевіряю чи є в мене такий юзер
    if(!USERS.containsKey(username)){
      throw new UsernameNotFoundException(username);
    }
    // Якщо все Ок - повертаю імплементацію UserDetails
    return new UserDetails() {
      @Override
      public Collection<? extends GrantedAuthority> getAuthorities() {...}
      @Override
      public String getPassword() {...}
      @Override
      public String getUsername() {...}
      @Override
      public boolean isAccountNonExpired() {...}
      @Override
      public boolean isAccountNonLocked() {...}
      @Override
      public boolean isCredentialsNonExpired() {...}
      @Override
      public boolean isEnabled() {...}
    };
  }
}
```

## CustomAuthenticationProvider

>Як ми автентифікуємо юзерів

```java
@Service
public class CustomAuthProvider implements AuthenticationProvider {
  private final CustomUserDetailsService userDetailsService;
  @Override
  public Authentication authenticate(Authentication authentication) throws AuthenticationException {
    // authentication - те що сконструює spring у відповідь на наш логін та пароль
    String username = authentication.getName();
    String password = authentication.getCredentials().toString();
    System.out.println("password = " + password);
    UserDetails user = userDetailsService.loadUserByUsername(username);
    return checkPassword(user, password);
  }
  @Override
  public boolean supports(Class<?> authentication) {
    return  UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication);
  }
  private Authentication checkPassword(UserDetails user, String rawPassword){
    if(Objects.equals(rawPassword, user.getPassword())){
      User innerUser = new User(
          user.getUsername(),
          user.getPassword(),
          user.getAuthorities()
      );
      return new UsernamePasswordAuthenticationToken(innerUser, user.getPassword(), user.getAuthorities());
    }else{
      throw new BadCredentialsException("Bad credentials");
    }
  }
}
```

## DefaultSecurityConfig

>Зклейка, додаємо до ланцюга провайдерів нашого CustomAuthProvider

```java
@EnableWebSecurity
public class DefaultSecurityConfig {
  private final CustomAuthProvider authProvider;
  @Bean
  PasswordEncoder passwordEncoder(){
    return PasswordEncoderFactories.createDelegatingPasswordEncoder();
  }
  @Bean
  SecurityFilterChain defaultSecurityFilterChain(HttpSecurity http) throws Exception{
    http
        .authorizeHttpRequests()
        .anyRequest()
        .authenticated()
        .and()
        .httpBasic()
        .and()
        .formLogin(Customizer.withDefaults());
    return http.build();
  }
  @Autowired
  public void injectCustomAuthProvider(AuthenticationManagerBuilder auth) throws Exception{
    auth.authenticationProvider(authProvider);
  }
}
```