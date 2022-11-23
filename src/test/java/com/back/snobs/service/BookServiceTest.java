package com.back.snobs.service;

import com.back.snobs.domain.book.BookDto;
import com.back.snobs.domain.book.BookRepository;
import com.back.snobs.error.CustomResponse;
import com.back.snobs.other.CreateDummyData;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.*;

//@SpringBootTest
@ExtendWith(MockitoExtension.class)
class BookServiceTest {
    @Mock
    BookRepository bookRepository;
    @InjectMocks
    BookService bookService;

    @Test
    @DisplayName("BookService - 조회")
    void read() {
        // given
        final Long bookIdx = 1L;
        given(bookRepository.findById(any())).willReturn(Optional.of(CreateDummyData.getOneBook()));

        // when
        ResponseEntity<CustomResponse> entity = bookService.read(bookIdx);

        // then
        assertEquals(entity.getStatusCode(), HttpStatus.OK);
    }

    @Test
    @DisplayName("BookService - 저장")
    void save() {
        // given
        final BookDto bookDto = new BookDto();
        bookDto.setBookId(UUID.randomUUID().toString());
        given(bookRepository.save(any())).willReturn(CreateDummyData.getOneBook());

        // when
        ResponseEntity<CustomResponse> entity = bookService.save(bookDto);

        // then
        assertEquals(entity.getStatusCode(), HttpStatus.OK);
    }
}