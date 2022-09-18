package com.back.snobs.service;

import com.back.snobs.domain.book.BookRepository;
import com.back.snobs.domain.book.snob_book.SnobBookRepository;
import com.back.snobs.domain.snob.LoginType;
import com.back.snobs.domain.snob.Role;
import com.back.snobs.domain.snob.SnobDto;
import com.back.snobs.domain.snob.SnobRepository;
import com.back.snobs.error.CustomResponse;
import com.back.snobs.util.CreateDummyData;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.*;

@ExtendWith(MockitoExtension.class)
class SnobServiceTest {
    @Mock
    SnobRepository snobRepository;
    @Mock
    SnobBookRepository snobBookRepository;
    @Mock
    BookRepository bookRepository;
    @InjectMocks
    SnobService snobService;
    final String userEmail = "mytestemail@naver.com";

    @Test
    @DisplayName("SnobService - 조회")
    void read() {
        // given
        given(snobRepository.findById(any())).willReturn(
                Optional.of(CreateDummyData.getOneSnob(LoginType.local, Role.GRANTED_USER))
        );

        // when
        ResponseEntity<CustomResponse> entity = snobService.read(userEmail);

        // then
        assertEquals(entity.getStatusCode(), HttpStatus.OK);
    }

    @Test
    @DisplayName("SnobService - 저장")
    void save() {
        // given
        final SnobDto snobDto = new SnobDto();
        snobDto.setBirthDate("1995-10-23");
        given(snobRepository.findById(any())).willReturn(
                Optional.of(CreateDummyData.getOneSnob(LoginType.local, Role.GRANTED_USER))
        );

        // when
        ResponseEntity<CustomResponse> entity = snobService.save(snobDto, userEmail);

        // then
        assertEquals(entity.getStatusCode(), HttpStatus.OK);
    }

    @Test
    @DisplayName("SnobService - 읽고 싶은 책 목록 조회")
    void getSnobBook() {
        // given
        final boolean readed = true;
        given(snobBookRepository.findBySnob_UserEmailWithFetchJoin(any(), any())).willReturn(
                List.of(CreateDummyData.getOneSnobBook())
        );

        // when
        ResponseEntity<CustomResponse> entity = snobService.getSnobBook(userEmail, readed);

        // then
        assertEquals(entity.getStatusCode(), HttpStatus.OK);
    }

    @Test
    @DisplayName("SnobService - 읽고 싶은 책 등록")
    void setSnobBook() {
        // given
        final String bookId = UUID.randomUUID().toString();
        final boolean readed = true;
        given(snobBookRepository.save(any())).willReturn(
                CreateDummyData.getOneSnobBook()
        );
        given(snobRepository.findById(any())).willReturn(
                Optional.of(CreateDummyData.getOneSnob(LoginType.local, Role.GRANTED_USER))
        );
        given(bookRepository.findById(any())).willReturn(Optional.of(CreateDummyData.getOneBook()));


        // when
        ResponseEntity<CustomResponse> entity = snobService.setSnobBook(userEmail, bookId, readed);

        // then
        assertEquals(entity.getStatusCode(), HttpStatus.OK);
    }
}