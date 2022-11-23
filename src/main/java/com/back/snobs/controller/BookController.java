package com.back.snobs.controller;

import com.back.snobs.domain.book.BookDto;
import com.back.snobs.error.CustomResponse;
import com.back.snobs.service.BookService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/book")
public class BookController {
    private final BookService bookService;

    // book search
    @GetMapping(value = "/search")
    public ResponseEntity<?> bookSearch(String query) {
        return bookService.bookSearch(query);
    }

    // book select
    @GetMapping(value = "")
    public ResponseEntity<CustomResponse> bookRead(@RequestParam(value = "bookIdx") Long bookIdx) {
        return bookService.read(bookIdx);
    }

    // book save
    @PostMapping(value = "")
    public ResponseEntity<CustomResponse> bookSave(@RequestBody BookDto bookDto) {
        return bookService.save(bookDto);
    }
}
