package com.innovation.backend.domain.Member.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.innovation.backend.domain.Badge.domain.Badge;
import com.innovation.backend.domain.Crew.domain.Crew;
import com.innovation.backend.domain.Meeting.domain.Meeting;
import com.innovation.backend.domain.Review.domain.Review;
import com.innovation.backend.global.enums.Authority;
import com.innovation.backend.global.util.Timestamped;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.*;

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
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;


  @OneToMany(mappedBy = "member", orphanRemoval = true)
  @JsonIgnore
  private List<Crew> crews;

  @OneToMany(mappedBy = "admin", orphanRemoval = true)
  @JsonIgnore
  private List<Meeting> meetings;

  //후기
  @OneToMany(mappedBy = "member", orphanRemoval = true)
  @JsonIgnore
  private List<Review> reviews = new ArrayList<>();

  @Column(nullable = false, unique = true)
  private String email;

  @Column(nullable = false)
  private String nickname;

  private String username;

  @Column(nullable = false)
  private String password;

  @Column
  private String profileImage;

  @Enumerated(EnumType.STRING)
  private Authority authority;

  private String provider;

  @JsonIgnore
  @OneToMany(mappedBy = "member", cascade = CascadeType.REMOVE,orphanRemoval = true)
  private List<Badge> badgeList = new ArrayList<>();

  public boolean validatePassword(PasswordEncoder passwordEncoder, String password) {
    return passwordEncoder.matches(password, this.password);
  }

  public Member(String email, String nickname, String password, String profileImage, Authority authority, String provider){
    this.email = email;
    this.nickname = nickname;
    this.password = password;
    this.profileImage = profileImage;
    this.authority = authority;
    this.provider = provider;
  }

}

