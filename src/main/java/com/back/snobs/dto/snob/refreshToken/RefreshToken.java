package com.back.snobs.dto.snob.refreshToken;

import com.back.snobs.dto.BaseTimeEntity;
import com.back.snobs.dto.snob.Snob;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor
public class RefreshToken extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long tokenIdx;

    @OneToOne(cascade = CascadeType.REMOVE)
    @JoinColumn(name = "userEmail")
    private Snob snob;

    @Column
    private String token;

    @Builder
    public RefreshToken(Snob snob, String token) {
        this.snob = snob;
        this.token = token;
    }

    public void updateToken(String token) {
        this.token = token;
    }
}
