package com.back.snobs.service;

import com.back.snobs.dto.book.Book;
import com.back.snobs.dto.book.BookDto;
import com.back.snobs.dto.book.BookRepository;
import com.back.snobs.dto.book.snob_book.SnobBookRepository;
import com.back.snobs.dto.snob.SnobRepository;
import com.back.snobs.error.CustomResponse;
import com.back.snobs.error.ResponseCode;
import com.back.snobs.error.exception.NoDataException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BookService {
    private final SnobRepository snobRepository;
    private final BookRepository bookRepository;
    private final SnobBookRepository snobBookRepository;
    @Value("${spring.security.oauth2.client.registration.kakao.client-id}")
    private String key;
    private final String url = "https://dapi.kakao.com/v3/search/book";

    // 책 정보 읽어오기
    public ResponseEntity<CustomResponse> read(String bookId) {
        return new ResponseEntity<>(new CustomResponse(ResponseCode.SUCCESS, bookRepository.findById(bookId).orElseThrow(() ->
                new NoDataException("No Such Book", ResponseCode.DATA_NOT_FOUND))),
                HttpStatus.valueOf(200));
    }

    public ResponseEntity<?> bookSearch(String query) {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.set("Authorization", "KakaoAK " + key);
        HttpEntity<String> httpEntity = new HttpEntity<>(httpHeaders);
        URI targetUrl = UriComponentsBuilder
                .fromUriString(url)
                .queryParam("query", query)
                .build()
                .encode(StandardCharsets.UTF_8)
                .toUri();

        //GetForObject는 헤더를 정의할 수 없음
        ResponseEntity<Map> result = restTemplate.exchange(targetUrl, HttpMethod.GET, httpEntity, Map.class);
        return new ResponseEntity<>(new CustomResponse(ResponseCode.SUCCESS, result.getBody()),
                HttpStatus.valueOf(200));
    }

    // 책 등록
    @Transactional
    public ResponseEntity<CustomResponse> save(BookDto bookDto) {
        Optional<Book> temp = bookRepository.findById(bookDto.getBookId());
        if (temp.isPresent()) {
            return new ResponseEntity<>(new CustomResponse(ResponseCode.SUCCESS, temp.get()),
                    HttpStatus.valueOf(200));
        }
        Book book = bookDto.toEntity();
        return new ResponseEntity<>(new CustomResponse(ResponseCode.SUCCESS, bookRepository.save(book)),
                HttpStatus.valueOf(200));
    }
}
