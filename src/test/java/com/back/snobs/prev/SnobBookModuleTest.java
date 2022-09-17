package com.back.snobs.prev;

import com.back.snobs.domain.book.snob_book.SnobBookRepository;
import com.back.snobs.domain.genre.snob_genre.SnobGenreRepository;
import com.back.snobs.domain.snob.Gender;
import com.back.snobs.domain.snob.LoginType;
import com.back.snobs.domain.snob.Snob;
import com.back.snobs.domain.snob.SnobRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;

@SpringBootTest
class SnobBookModuleTest {
    @Autowired
    private SnobRepository snobRepository;
    @Autowired
    private SnobBookRepository snobBookRepository;
    @Autowired
    private SnobGenreRepository snobGenreRepository;

    // SnobBook CRUD
//    @Test
//    void snobBookRead() {
//        List<SnobBook> ls = snobBookRepository.findBySnob_UserEmailWithFetchJoin(
//                "helloThere@naver.com",
//                true
//        );
//        for (SnobBook sb: ls) {
//            System.out.println(sb.getBook().getTitle());
//        }
//    }

    @Test
    void snobSave() {
        Snob s = Snob.builder()
                .userEmail("asdf@naver.com")
                .cellPhoneCode("ffff")
                .address("asdf")
                .gender(Gender.MALE)
                .loginType(LoginType.google)
                .birthDate(LocalDate.now())
                .userName("바보")
                .selfIntroduction("asdf")
                .build();
        snobRepository.save(s);
    }

    @Test
    void t() {
        System.out.println(snobRepository.existsByCellPhoneCode("f1fe9503-e84b-4442-bbcf-081b80e1333a"));
    }
}
