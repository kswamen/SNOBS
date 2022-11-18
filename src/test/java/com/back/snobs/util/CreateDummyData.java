package com.back.snobs.util;

import com.back.snobs.domain.book.Book;
import com.back.snobs.domain.book.snob_book.SnobBook;
import com.back.snobs.domain.log.Log;
import com.back.snobs.domain.snob.Gender;
import com.back.snobs.domain.snob.LoginType;
import com.back.snobs.domain.snob.Role;
import com.back.snobs.domain.snob.Snob;

import java.time.LocalDate;
import java.util.UUID;

public class CreateDummyData {
    public static Book getOneBook() {
        return Book.builder()
                .bookId(UUID.randomUUID().toString())
                .author("김석원")
                .title("나 보기가 역겨워 가실 때에는")
                .translator("박수아")
                .thumbnailImageUrl("http://myimage.com")
                .build();
    }

    public static Log getOneLog(Snob snob, Book book) {
        return Log.builder()
                .logTitle("my test log")
                .previewText("my preview text")
                .isPublic(true)
                .logQuestion("is it good, right?")
                .book(book)
                .snob(snob)
                .build();
    }

    public static Snob getOneSnob(LoginType loginType, Role role) {
        return Snob.builder()
                .userEmail("mytestemail@naver.com")
                .cellPhoneCode(UUID.randomUUID().toString())
                .password("")
                .loginType(loginType)
                .role(role)
                .userName("meier")
                .birthDate(LocalDate.of(1995, 10, 23))
                .gender(Gender.MALE)
                .address("Somewhere I can rest")
                .selfIntroduction("반갑습니다 :)")
                .mainProfileImage("http://myprofileimage.com")
                .build();
    }

    public static SnobBook getOneSnobBook() {
        return SnobBook.builder()
                .snob(getOneSnob(LoginType.local, Role.GRANTED_USER))
                .book(getOneBook())
                .readed(true)
                .build();
    }
}
