package com.back.snobs.controller;

import com.back.snobs.domain.snob.SnobDto;
import com.back.snobs.error.CustomResponse;
import com.back.snobs.security.UserPrincipal;
import com.back.snobs.security.oauth2.CurrentUser;
import com.back.snobs.service.SnobService;
import lombok.RequiredArgsConstructor;
import org.json.simple.parser.ParseException;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api/snob")
public class SnobController {
    private final SnobService snobService;

    // Snob Read
    @GetMapping(value = "")
//    @PreAuthorize("hasAnyRole('USER', 'GRANTED_USER')")
    @PreAuthorize("hasRole('GRANTED_USER')")
    public ResponseEntity<CustomResponse> snobRead(@CurrentUser UserPrincipal userPrincipal) {
        return snobService.read(userPrincipal.getEmail());
    }

    @PatchMapping(value = "")
    @PreAuthorize("hasAnyRole('USER', 'GRANTED_USER')")
    public ResponseEntity<CustomResponse> snobUpdate(@CurrentUser UserPrincipal userPrincipal, @RequestBody SnobDto snobDto) {
        return snobService.update(snobDto, userPrincipal.getEmail());
    }

    @GetMapping(value = "/genre")
    public ResponseEntity<CustomResponse> snobGetGenre(String userEmail) {
        return snobService.getGenre(userEmail);
    }

    @PostMapping(value = "/genre")
    public ResponseEntity<CustomResponse> snobSetGenre(String userEmail, @RequestBody String genreData) throws ParseException {
        return snobService.setGenre(userEmail, genreData);
    }

    @GetMapping(value = "/keyword")
    public ResponseEntity<CustomResponse> snobGetKeyword(String userEmail) {
        return snobService.getKeyword(userEmail);
    }

    @PostMapping(value = "/keyword")
    public ResponseEntity<CustomResponse> snobSetKeyword(String userEmail, @RequestBody String keywordData) throws ParseException {
        return snobService.setKeyword(userEmail, keywordData);
    }

    @GetMapping(value = "/book")
    public ResponseEntity<CustomResponse> snobGetBook(String userEmail, Boolean readed) {
        return snobService.getSnobBook(userEmail, readed);
    }

    // fetch join diff test controller
    @GetMapping(value = "/bookTest")
    public ResponseEntity<CustomResponse> snobGetBookTest(String userEmail) {
        return snobService.getSnobBookTest(userEmail);
    }

    @GetMapping(value = "/bookTest2")
    public ResponseEntity<CustomResponse> snobGetBookTest2(String userEmail) {
        return snobService.getSnobBookTest2(userEmail);
    }

    @PostMapping(value = "/book")
    public ResponseEntity<CustomResponse> snobSetBook(String userEmail, String bookId, Boolean readed) {
        return snobService.setSnobBook(userEmail, bookId, readed);
    }

    @GetMapping(value = "/dailyLog")
    @PreAuthorize("hasRole('GRANTED_USER')")
    public ResponseEntity<CustomResponse> snobGetDailyLog(@CurrentUser UserPrincipal userPrincipal) {
        return snobService.getSnobDailyLog(userPrincipal.getEmail());
    }
}
