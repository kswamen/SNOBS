package com.back.snobs.service;

import com.back.snobs.dto.mailverification.MailVerification;
import com.back.snobs.dto.mailverification.MailVerificationRepository;
import com.back.snobs.dto.snob.Snob;
import com.back.snobs.dto.snob.SnobRepository;
import com.back.snobs.error.CustomResponse;
import com.back.snobs.error.ResponseCode;
import com.back.snobs.util.MailHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Optional;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class MailService {
    private final JavaMailSender mailSender;
    private final MailVerificationRepository mailVerificationRepository;
    private final SnobRepository snobRepository;
    private static final String FROM_ADDRESS = "kimseokwon95@gmail.com";

    public ResponseEntity<CustomResponse> checkVerificationCode(String userEmail, String verificationCode) {
        MailVerification mailVerification = mailVerificationRepository.findBySnob_UserEmail(userEmail).orElseThrow();
        LocalDateTime now = LocalDateTime.now();
        if(ChronoUnit.SECONDS.between(mailVerification.getModifiedDate(), now) <= 300) {
            if (mailVerification.getVerificationCode().equals(verificationCode)) {
                mailVerificationRepository.delete(mailVerification);
                return new ResponseEntity<>(new CustomResponse(ResponseCode.SUCCESS, "VERIFICATED!"),
                        HttpStatus.valueOf(200));
            }
        }
        return new ResponseEntity<>(new CustomResponse(ResponseCode.EMAIL_VERIFICATION_FAILED, "Email Verification Failed"),
                HttpStatus.valueOf(200));
    }

    public ResponseEntity<CustomResponse> mailSend(String address) {
        String verificationCode = String.format("%06d", new Random().nextInt(999999));
        String title = "스놉스에 오신 걸 환영합니다!";
        Snob snob = snobRepository.findById(address).orElseThrow();
        try {
            MailHandler mailHandler = new MailHandler(mailSender);
            mailHandler.setTo(address);
            mailHandler.setFrom(FROM_ADDRESS);
            mailHandler.setSubject(title);

            String htmlContent =
                    "<h1>Welcome to SNOBS!</h1><br><br>"
                    + "인증 번호: " + verificationCode;

            mailHandler.setText(htmlContent, true);
            mailHandler.send();

            Optional<MailVerification> mv = mailVerificationRepository.findBySnob_UserEmail(address);
            if(mv.isPresent()) {
                mv.get().updateCode(verificationCode);
                mailVerificationRepository.save(mv.get());
            } else {
                mailVerificationRepository.save(MailVerification.builder()
                        .snob(snob)
                        .verificationCode(verificationCode)
                        .build());
            }

        } catch(Exception e) {
            e.printStackTrace();
        }

        return new ResponseEntity<>(new CustomResponse(ResponseCode.SUCCESS, "Mail sent"),
                HttpStatus.valueOf(200));
    }
}
