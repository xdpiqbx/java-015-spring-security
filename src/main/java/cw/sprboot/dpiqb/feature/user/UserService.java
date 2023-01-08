package cw.sprboot.dpiqb.feature.user;

import cw.sprboot.dpiqb.feature.user.dto.UserDTO;
import cw.sprboot.dpiqb.feature.user.dto.UserInfoDTO;
import jakarta.persistence.EntityManager;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import jakarta.transaction.Transactional;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Service;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class UserService {
  private final UserRepository userRepository;
  private final EntityManager entityManager; // схожий на session factory (Hibernate)
  private final NamedParameterJdbcTemplate jdbcTemplate; // Дозволяє працювати з SQL запитами, аналогія Statement
  private final SessionFactory sessionFactory;

  public List<User> findAll(){
      return userRepository.findAll();
  }

  public void save(User user){
    userRepository.save(user);
  }

  public boolean exists(String email){
    if(email == null){
      return false;
    }
    Integer userCount = jdbcTemplate.queryForObject(
        "SELECT count(*) FROM \"user\" WHERE email = :email",
        Map.of("email", email), // параметри для запиту
        Integer.class // що хочу отримати у відповідь
    );
    return userCount == 1;
  }

//  public boolean exists(String email){
//    if(email == null){
//      return false;
//    }
//    Session session = sessionFactory.openSession();
//    User user = session.find(User.class, email);
//    session.close();
//    return user != null;
//  }

//  public boolean exists(String email){
//    if(email == null){
//      return false;
//    }
//    User user = entityManager.find(User.class, email);
//    return user != null;
//  }

//  public boolean exists(String email){
//    if(email == null){
//      return false;
//    }
//    return userRepository.existsById(email);
//  }

  public void deleteById(String email){
    userRepository.deleteById(email);
  }

//  @Transactional - тут не треба
  public void deleteByIds(List<String> emails){
    Integer userCount = jdbcTemplate.update(
        "DELETE FROM \"user\" WHERE email IN (:emails)",
        Map.of("emails", emails) // параметри для запиту
    );
  }
//  @Transactional(
//      rollbackOn = {NullPointerException.class},
//      dontRollbackOn = {IOException.class}
//  )
//  public void deleteByIds(List<String> emails){
//    userRepository.deleteAllByEmails(emails);
//  }

  public List<User> search(String query){
    String sql = "SELECT email, full_name, birthday, gender " +
        "FROM \"user\" " +
        "WHERE lower(email) LIKE lower(:query) OR lower(full_name) LIKE lower(:query)";
    List<User> result = jdbcTemplate.query(
        sql,
        Map.of("query", "%" + query + "%"),
        new UserRowMapper()
    );
    return result;
  }

  private static class UserRowMapper implements RowMapper<User> {
    @Override
    public User mapRow(ResultSet rs, int rowNum) throws SQLException {
      User user = new User();
      user.setEmail(rs.getString("email"));
      user.setFullName(rs.getString("full_name"));
      user.setBirthday(LocalDate.parse(rs.getString("birthday")));
      user.setGender(Gender.valueOf(rs.getString("gender")));
      return user;
    }
  }

//  public List<User> search(String query){
////    userRepository.search("%"+query+"%");
////    userRepository.searchByNativeSql("%"+query+"%");
////    userRepository.searchEmails("%"+query+"%");
//    return userRepository.findAllById(
//      userRepository.searchEmails("%"+query+"%")
//    );
//  }

  public int countPeopleOlderThan(int age){
    return userRepository.countPeopleOlderThan(LocalDate.now().minusYears(age));
  }

  // ============================================================================ public UserInfoDTO getUserInfo - START
  public UserInfoDTO getUserInfo(String email){
    String sql =
        "SELECT u.email AS email, full_name, birthday, gender, address\n" +
        "FROM \"user\" AS u\n" +
        "LEFT JOIN user_address AS ua ON u.email = ua.email\n" +
        "WHERE u.email = :email";
    List<UserInfoItem> items = jdbcTemplate.query(
        sql,
        Map.of("email", email),
        new UserInfoMapper()
    );

    UserInfoItem userInfoItem = items.get(0);

    int age = (int)ChronoUnit.YEARS.between(userInfoItem.getBirthday(), LocalDate.now());
    UserDTO userDTO = UserDTO.builder()
        .email(userInfoItem.getEmail())
        .fullName(userInfoItem.getFullName())
        .birthday(userInfoItem.getBirthday())
        .gender(userInfoItem.getGender())
        .age(age)
        .build();

    List<String> addressList = items.stream()
        .map(it -> it.getAddress())
        .filter(it -> it != null)
        .collect(Collectors.toList());

    UserInfoDTO userInfoDTO = new UserInfoDTO();
    userInfoDTO.setUserDTO(userDTO);
    userInfoDTO.setAddresses(addressList);

    return userInfoDTO;
  }
  // =========================================== public UserInfoDTO getUserInfoV2 - START
  public UserInfoDTO getUserInfoV2(String email){
    User user = userRepository.findById(email).get();

    List<String> addressList = jdbcTemplate.queryForList(
        "SELECT address FROM user_address WHERE email = :email",
        Map.of("email", email),
        String.class
    );

    UserInfoDTO userInfoDTO = new UserInfoDTO();

    userInfoDTO.setUserDTO(UserDTO.toDTO(user));
    userInfoDTO.setAddresses(addressList);

    return userInfoDTO;
  }
  // =========================================== public UserInfoDTO getUserInfoV2 - END

  private static class UserInfoMapper implements RowMapper<UserInfoItem>{
    @Override
    public UserInfoItem mapRow(ResultSet rs, int rowNum) throws SQLException {
      return UserInfoItem.builder()
          .email(rs.getString("email"))
          .fullName(rs.getString("full_name"))
          .birthday(LocalDate.parse(rs.getString("birthday")))
          .gender(Gender.valueOf(rs.getString("gender")))
          .address(rs.getString("address"))
          .build();
    }
  }
  @Builder
  @Data
  private static class UserInfoItem{
    private String email;
    private String fullName;
    private LocalDate birthday;
    private Gender gender;
    private String address;
  }
  // ============================================================================ public UserInfoDTO getUserInfo - END
  public List<User> getUserBetween(LocalDate start, LocalDate end){
    return userRepository.findAll(
        (root, cq, cb) -> cb.between(root.get("birthday"), start, end)
    );
  }

//  public List<User> getUserBetween(LocalDate start, LocalDate end){
//    return userRepository.findAll((root, cq, cb) -> cb.and(
//        cb.greaterThanOrEqualTo(root.get("birthday"), start),
//        cb.lessThanOrEqualTo(root.get("birthday"), end)
//    ));
//  }

//  public List<User> getUserBetween(LocalDate start, LocalDate end){
//    Specification<User> betweenSpec = new Specification<User>() {
//      @Override
//      public Predicate toPredicate(Root<User> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
//        return criteriaBuilder.and(
//            criteriaBuilder.greaterThanOrEqualTo(root.get("birthday"), start),
//            criteriaBuilder.lessThanOrEqualTo(root.get("birthday"), end)
//        );
//      }
//    };
//
//    return userRepository.findAll(betweenSpec);
//  }
}
