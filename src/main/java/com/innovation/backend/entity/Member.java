package com.innovation.backend.entity;

import javax.persistence.*;

import com.innovation.backend.enums.Authority;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Member extends Timestamped {

  @Id
  @Column(name="member_id")
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable = false, unique = true)
  private String email;

  @Column(nullable = false)
  private String nickname;

  @Column(nullable = false)
  private String password;

  @Column
  private String profileImage;

  @Enumerated(EnumType.STRING)
  private Authority authority;

  private String provider;

  public boolean validatePassword(PasswordEncoder passwordEncoder, String password) {
    return passwordEncoder.matches(password, this.password);
  }

  public Member(String email, String nickname, String password, String profileImage, Authority authority, String provider){
    this.email = email;
    this.password = password;
    this.nickname = nickname;
    this.profileImage = profileImage;
    this.authority = authority;
    this.provider = provider;
  }

}