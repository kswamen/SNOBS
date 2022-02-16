package com.back.snobs;

import com.back.snobs.dto.chatroom.ChatRoomRepository;
import com.back.snobs.dto.log.Log;
import com.back.snobs.dto.log.LogRepository;
import com.back.snobs.dto.reaction.Reaction;
import com.back.snobs.dto.reaction.ReactionRepository;
import com.back.snobs.dto.snob.Gender;
import com.back.snobs.dto.snob.Snob;
import com.back.snobs.dto.snob.SnobRepository;
import com.back.snobs.dto.snob.dailyLog.DailyLog;
import com.back.snobs.dto.snob.dailyLog.DailyLogRepository;
import com.back.snobs.dto.snob.profileImage.ProfileImage;
import com.back.snobs.dto.snob.profileImage.ProfileImageRepository;
import com.back.snobs.dto.snob.profileImage.ProfileImageType;
import com.back.snobs.dto.snob.refreshToken.RefreshTokenRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;

@SpringBootTest
class SnobModuleTest {
    @Autowired
    private SnobRepository snobRepository;

    @Autowired
    private ProfileImageRepository profileImageRepository;

    @Autowired
    private ChatRoomRepository chatRoomRepository;

    @Autowired
    private RefreshTokenRepository refreshTokenRepository;

    @Autowired
    private DailyLogRepository dailyLogRepository;

    @Autowired
    private LogRepository logRepository;

    @Autowired
    private ReactionRepository reactionRepository;

    // Snob CRUD
    @Test
    void snobInsert() {
        Snob tempUser = Snob.builder()
                .userEmail("SomethingEmail@naver.com")
                .userName("홍길동")
                .address("이 세상 어딘가")
                .birthDate(LocalDate.now())
                .gender(Gender.MALE)
                .selfIntroduction("반갑습니다.")
                .build();

        System.out.println(snobRepository.save(tempUser));
    }

    @Test
    void deleteReaction() {
        Reaction r = reactionRepository.findById(2L).orElseThrow();
        LocalDateTime dt = r.getCreateDate();
        LocalDateTime now = LocalDateTime.now();
        System.out.println(dt);
        System.out.println(now);
        System.out.println(ChronoUnit.DAYS.between(dt, now));
    }

    @Test
    void snobRead() {
        Optional<Snob> tempUser = snobRepository.findById("SomethingEmail@naver.com");
        if (tempUser.isPresent()) {
            System.out.println(tempUser.get().getUserEmail());
        } else {
            System.out.println("There is no such user!");
        }
    }

    @Test
    void snobDelete() {
        Optional<Snob> tempUser = snobRepository.findById("SomethingEmail@naver.com");
        tempUser.ifPresent(snob -> snobRepository.deleteById(snob.getUserEmail()));
    }

    @Test
    void t() {
        System.out.println(snobRepository.existsById("a@a.com"));
        System.out.println(snobRepository.existsByCellPhoneCode("f1fe9503-e84b-4442-bbcf-081b80e1333a"));
    }

    @Test
    void t2() {
        ProfileImage p = ProfileImage.builder()
                .snob(snobRepository.findById("a@a.com").get())
                .filePath("C:\\")
                .fileExtension(".jpeg")
                .profileImageType(ProfileImageType.FIRST)
                .build();
        profileImageRepository.save(p);
    }

    @Test
    void profileImageSearchTest() {
        Snob snob = snobRepository.findById("helloThere@naver.com").orElseThrow();

        List<ProfileImage> p = profileImageRepository.findBySnob(snob);
        System.out.println("\n");
        for (ProfileImage profileImage : p) {
            System.out.println(profileImage.getFilePath());
        }
    }

    @Test
    void createDailyLog() {
        Snob snob = snobRepository.findById("a@a.com").orElseThrow();
        Log log = logRepository.findById(2L).orElseThrow();

        DailyLog dailyLog = DailyLog.builder()
                .log(log)
                .snob(snob)
                .build();

        dailyLogRepository.save(dailyLog);
    }

    @Test
    void insertReaction() {
        Snob receiverSnob = snobRepository.findById("helloThere@naver.com").orElseThrow();
        Snob senderSnob = snobRepository.findById("a@a.com").orElseThrow();
        Log log = logRepository.findById(3L).orElseThrow();

        reactionRepository.save(Reaction.builder()
                .log(log)
                .receiverSnob(receiverSnob)
                .senderSnob(senderSnob)
                .message("asdf")
                .build());
    }

    @Test
    void findReaction() {
        Optional<Reaction> reaction =
                reactionRepository.findBysenderEmailAndlogIdx("helloThere@naver.com",
                        3L);
        reaction.ifPresent(value -> System.out.println(value.getMessage()));
    }

    @Test
    void deleteAllinBatchDailyLog() {
        dailyLogRepository.deleteAllInBatch();
    }

    @Test
    void countByDailyLog() {
        System.out.println(dailyLogRepository.countBySnob_userEmail("a@a.com"));
    }
}
