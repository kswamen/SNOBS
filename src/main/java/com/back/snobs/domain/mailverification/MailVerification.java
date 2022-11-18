package com.back.snobs.domain.mailverification;

import com.back.snobs.domain.BaseTimeEntity;
import com.back.snobs.domain.snob.Snob;
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
    @JoinColumn(name = "snobIdx", nullable = false)
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
