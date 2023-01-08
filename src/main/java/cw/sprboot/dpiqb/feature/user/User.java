package cw.sprboot.dpiqb.feature.user;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Table(name = "\"user\"")
@Data
@Entity
public class User {
  @Id
  private String email;
  private String fullName;
  private LocalDate birthday;
  @Enumerated(EnumType.STRING)
  private Gender gender;
}
