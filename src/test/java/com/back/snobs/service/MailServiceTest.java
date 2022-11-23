package com.back.snobs.service;

import com.back.snobs.domain.mailverification.MailVerification;
import com.back.snobs.domain.mailverification.MailVerificationRepository;
import com.back.snobs.domain.snob.LoginType;
import com.back.snobs.domain.snob.Role;
import com.back.snobs.domain.snob.Snob;
import com.back.snobs.error.CustomResponse;
import com.back.snobs.other.CreateDummyData;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.BDDMockito.any;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class MailServiceTest {
    @Mock
    MailVerificationRepository mailVerificationRepository;
    @InjectMocks
    MailService mailService;

    final String userEmail = "mytestemail@naver.com";
    final String verificationCode = "123456";
    final Snob snob = CreateDummyData.getOneSnob(LoginType.local, Role.GRANTED_USER);

    @Test
    @DisplayName("MailService - 인증 코드 검사")
    void checkVerificationCode() {
        // given
        MockMailVerification mmv = new MockMailVerification(LocalDateTime.now(), verificationCode);

        given(mailVerificationRepository.findBySnob_UserEmail(any())).willReturn(
                Optional.of(mmv)
        );

        // when
        ResponseEntity<CustomResponse> entity = mailService.checkVerificationCode(userEmail, verificationCode);

        // then
        assertEquals(entity.getStatusCode(), HttpStatus.OK);
    }

//    @Getter
    static class MockMailVerification extends MailVerification {
        private final LocalDateTime modifiedDate;
        private final String verificationCode;

        public MockMailVerification(LocalDateTime ldt, String verificationCode) {
            this.modifiedDate = ldt;
            this.verificationCode = verificationCode;
        }

        public LocalDateTime getModifiedDate() {
            return this.modifiedDate;
        }

        public String getVerificationCode() {
            return this.verificationCode;
        }
    }
}