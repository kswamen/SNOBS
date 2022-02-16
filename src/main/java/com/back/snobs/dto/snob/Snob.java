package com.back.snobs.dto.snob;

import com.back.snobs.dto.BaseTimeEntity;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicInsert;

import javax.persistence.*;
import java.time.LocalDate;

@Getter
@NoArgsConstructor
@Entity
@DynamicInsert
@Table(uniqueConstraints = {
        @UniqueConstraint(name = "UK_cellPhoneCode", columnNames = {"cellPhoneCode"}),
        @UniqueConstraint(name = "UK_snobIdx", columnNames = {"snobIdx"}),
})
public class Snob extends BaseTimeEntity {
    @Id
    @JsonIgnore
    private String userEmail;

    @Column(nullable = false)
    @JsonIgnore
    private String cellPhoneCode;

    @Column(nullable = false)
    private String snobIdx;

    @Column
    @JsonIgnore
    private String password;

    @Column(nullable = false)
    @JsonIgnore
    private LoginType loginType;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;

    @Column(nullable = false)
    private String userName;

    @Column(nullable = false)
    private LocalDate birthDate;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Gender gender;

    @Column
    @JsonIgnore
    private String address;

    @Column(columnDefinition = "varchar(500)")
    private String selfIntroduction;

    @Column
    private String mainProfileImage;

    @Builder
    public Snob(String userEmail, String cellPhoneCode, String password, LoginType loginType, Role role, String userName, LocalDate birthDate, Gender gender, String address, String selfIntroduction, String mainProfileImage, String snobIdx) {
        this.userEmail = userEmail;
        this.cellPhoneCode = cellPhoneCode;
        this.password = password;
        this.loginType = loginType;
        this.role = role;
        this.userName = userName;
        this.birthDate = birthDate;
        this.gender = gender;
        this.address = address;
        this.selfIntroduction = selfIntroduction;
        this.mainProfileImage = mainProfileImage;
        this.snobIdx = snobIdx;
    }

    public void updateMainProfileImage(String mainProfileImage) {
        this.mainProfileImage = mainProfileImage;
    }

    public void SnobUpdate(String userName, Gender gender, LocalDate birthDate, String address, String selfIntroduction) {
        this.userName = userName;
        this.gender = gender;
        this.birthDate = birthDate;
        this.address = address;
        this.selfIntroduction = selfIntroduction;
        this.role = Role.GRANTED_USER;
    }
}
