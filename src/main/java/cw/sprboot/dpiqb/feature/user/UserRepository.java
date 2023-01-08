package cw.sprboot.dpiqb.feature.user;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, String>, JpaSpecificationExecutor<User> {
  @Query("from User u where lower(u.email) like lower(:query) or lower(u.fullName) like lower(:query)")
  List<User> search(@Param("query") String query);

  @Query(
    nativeQuery = true,
    value = "SELECT email, full_name, birthday, gender " +
      "FROM \"user\" " +
      "WHERE lower(email) LIKE lower(:query) " +
      "OR lower(full_name) LIKE lower(:query)")
  List<User> searchByNativeSql(@Param("query") String query);
  @Query(
    nativeQuery = true,
    value = "SELECT email " +
      "FROM \"user\" " +
      "WHERE lower(email) LIKE lower(:query)")
  List<String> searchEmails(@Param("query") String query);

  @Query(
    nativeQuery = true,
    value = "SELECT count(*) FROM \"user\" WHERE birthday < :maxAge")
  int countPeopleOlderThan(@Param("maxAge")LocalDate maxBirthday);

  @Modifying
  @Query(
      nativeQuery = true,
      value = "DELETE FROM \"user\" WHERE email IN (:emails)"
  )
  void deleteAllByEmails(@Param("emails") List<String> emails);
}
