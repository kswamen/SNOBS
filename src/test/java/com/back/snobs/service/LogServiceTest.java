package com.back.snobs.service;

import com.back.snobs.domain.book.Book;
import com.back.snobs.domain.book.BookRepository;
import com.back.snobs.domain.log.Log;
import com.back.snobs.domain.log.LogDto;
import com.back.snobs.domain.log.LogRepository;
import com.back.snobs.domain.snob.*;
import com.back.snobs.error.CustomResponse;
import com.back.snobs.error.exception.DifferentSnobException;
import com.back.snobs.error.exception.NoDataException;
import com.back.snobs.util.CreateDummyData;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.*;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class LogServiceTest {
    @Mock
    BookRepository bookRepository;
    @Mock
    SnobRepository snobRepository;
    @Mock
    LogRepository logRepository;
    @InjectMocks
    LogService logService;

    final String userEmail = "mytestemail@naver.com";
    final Long logIdx = 1L;

    Snob snob = CreateDummyData.getOneSnob(LoginType.local, Role.GRANTED_USER);
    Book book = CreateDummyData.getOneBook();

    @Test
    @DisplayName("LogService - 조회(with LogIdx)")
    void readWithLogIdx() {
        // given
        given(logRepository.findById(any())).willReturn(Optional.of(
                        CreateDummyData.getOneLog(snob, book)
                )
        );

        // when
        ResponseEntity<CustomResponse> entity = logService.read(logIdx);

        // then
        assertEquals(entity.getStatusCode(), HttpStatus.OK);
    }

    @Test
    @DisplayName("LogService - 조회(with UserEmail)")
    void readWithUserEmail() {
        // given
        given(logRepository.findBySnob_UserEmail(any())).willReturn(List.of(
                CreateDummyData.getOneLog(snob, book)
        ));

        // when
        ResponseEntity<CustomResponse> entity = logService.read(userEmail);

        // then
        assertEquals(entity.getStatusCode(), HttpStatus.OK);
    }

    @Test
    @DisplayName("LogService - 저장")
    void save() {
        // given
        given(logRepository.findById(any())).willReturn(Optional.of(
                CreateDummyData.getOneLog(snob, book)
        ));
        given(snobRepository.findByUserEmail(any())).willReturn(Optional.of(
                CreateDummyData.getOneSnob(LoginType.local, Role.GRANTED_USER)
        ));
        given(bookRepository.findById(any())).willReturn(Optional.of(
                CreateDummyData.getOneBook()
        ));
        LogDto logDto = new LogDto();

        // when
        ResponseEntity<CustomResponse> entity = logService.save(logDto, userEmail);

        // then
        assertEquals(entity.getStatusCode(), HttpStatus.OK);
    }

    @Test
    @DisplayName("LogService - 삭제")
    void delete() {
        // given
        given(logRepository.findById(any())).willReturn(
                Optional.of(CreateDummyData.getOneLog(snob, book))
        );

        // when
        ResponseEntity<CustomResponse> entity = logService.delete(logIdx, userEmail);

        // then
        assertEquals(entity.getStatusCode(), HttpStatus.OK);
    }

    @Test
    @DisplayName("LogService - 삭제(로그가 존재하지 않으면 예외 처리)")
    void deleteNoData() {
        // given
        given(logRepository.findById(any())).willReturn(Optional.empty());

        // when

        // then
        assertThrows(NoDataException.class, () -> {
            logService.delete(logIdx, userEmail);
        });
    }

    @Test
    @DisplayName("LogService - 삭제(삭제하려는 로그와 이메일이 일치하지 않으면 예외 처리)")
    void deleteDiffSnob() {
        // given
        Snob snob1 = Snob.builder()
                .userEmail("somediffemail@naver.com")
                .cellPhoneCode(UUID.randomUUID().toString())
                .password("")
                .loginType(LoginType.local)
                .role(Role.GRANTED_USER)
                .userName("meier")
                .birthDate(LocalDate.of(1995, 10, 23))
                .gender(Gender.MALE)
                .address("Somewhere I can rest")
                .selfIntroduction("반갑습니다 :)")
                .mainProfileImage("http://myprofileimage.com")
                .build();

        given(logRepository.findById(any())).willReturn(
                Optional.of(CreateDummyData.getOneLog(snob1, book)
        ));

        // when

        // then
        assertThrows(DifferentSnobException.class, () ->
                logService.delete(logIdx, userEmail)
        );
    }
}