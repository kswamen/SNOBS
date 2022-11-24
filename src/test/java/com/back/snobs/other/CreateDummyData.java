package com.back.snobs.other;

import com.back.snobs.domain.book.Book;
import com.back.snobs.domain.book.snob_book.SnobBook;
import com.back.snobs.domain.log.Log;
import com.back.snobs.domain.snob.Gender;
import com.back.snobs.domain.snob.LoginType;
import com.back.snobs.domain.snob.Role;
import com.back.snobs.domain.snob.Snob;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.time.LocalDate;
import java.util.Date;
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

    public static String createJwtToken(String userEmail) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + 500000);
        Key key = Keys.hmacShaKeyFor("A".repeat(128).getBytes(StandardCharsets.UTF_8));

        return Jwts.builder()
                .setSubject(userEmail)
                .setIssuedAt(new Date())
                .setExpiration(expiryDate)
//                .signWith(SignatureAlgorithm.HS512, authProperties.getAuth().getTokenSecret())
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }
}
