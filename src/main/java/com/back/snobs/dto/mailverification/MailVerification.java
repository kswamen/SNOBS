package com.back.snobs.dto.mailverification;

import com.back.snobs.dto.BaseTimeEntity;
import com.back.snobs.dto.snob.Snob;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import javax.persistence.*;

@Getter
@RequiredArgsConstructor
@Entity
public class MailVerification extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long reactionIdx;

    @ManyToOne
    @JoinColumn(name = "userEmail", nullable = false)
    private Snob snob;

    @Column(nullable = false)
    private String verificationCode;

    @Builder
    public MailVerification(Snob snob, String verificationCode) {
        this.snob = snob;
        this.verificationCode = verificationCode;
    }

    public void updateCode(String verificationCode) {
        this.verificationCode = verificationCode;
    }
}
