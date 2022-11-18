package com.back.snobs.service;

import com.back.snobs.domain.book.Book;
import com.back.snobs.domain.book.BookRepository;
import com.back.snobs.domain.log.Log;
import com.back.snobs.domain.log.LogDto;
import com.back.snobs.domain.log.LogRepository;
import com.back.snobs.domain.snob.Snob;
import com.back.snobs.domain.snob.SnobRepository;
import com.back.snobs.error.CustomResponse;
import com.back.snobs.error.ResponseCode;
import com.back.snobs.error.exception.DifferentSnobException;
import com.back.snobs.error.exception.NoDataException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
@RequiredArgsConstructor
public class LogService {
    private final LogRepository logRepository;
    private final BookRepository bookRepository;
    private final SnobRepository snobRepository;

    // 사용자 이메일로 작성된 로그 검색
    public ResponseEntity<CustomResponse> read(String userEmail) {
        List<Log> ls = logRepository.findBySnob_UserEmail(userEmail);
        return new ResponseEntity<>(new CustomResponse(ResponseCode.SUCCESS, ls),
                HttpStatus.valueOf(200));
    }

    public ResponseEntity<CustomResponse> read(Long logIdx) {
        return new ResponseEntity<>(new CustomResponse(ResponseCode.SUCCESS,
                logRepository.findById(logIdx).orElseThrow(() -> new NoDataException("No Such Data", ResponseCode.DATA_NOT_FOUND))),
                HttpStatus.valueOf(200));
    }

    @Transactional
    public ResponseEntity<CustomResponse> delete(Long logIdx, String userEmail) {
        Log log = logRepository.findById(logIdx).orElseThrow(() -> new NoDataException("No Such Data", ResponseCode.DATA_NOT_FOUND));
        if(!log.getSnob().getUserEmail().equals(userEmail)) {
            throw new DifferentSnobException("different snob", ResponseCode.DIFFERNT_SNOB);
        } else {
            logRepository.delete(log);
            return new ResponseEntity<>(new CustomResponse(ResponseCode.SUCCESS, "log deleted"),
                    HttpStatus.valueOf(200));
        }
    }

    @Transactional
    public ResponseEntity<CustomResponse> save(LogDto logDto, String userEmail) {
        // 새롭게 생성
        if (logDto.getLogIdx() == null) {
            Book book = bookRepository.findByBookId(logDto.getBookId())
                    .orElseThrow(() -> new NoDataException("No such Data", ResponseCode.DATA_NOT_FOUND));
            Snob snob = snobRepository.findByUserEmail(userEmail)
                    .orElseThrow(() -> new NoDataException("No such Data", ResponseCode.DATA_NOT_FOUND));
            logDto.setBook(book);
            logDto.setSnob(snob);

            return new ResponseEntity<>(new CustomResponse(ResponseCode.SUCCESS, logRepository.save(logDto.toEntity())),
                    HttpStatus.valueOf(200));
        }
        // 이미 존재하는 로그의 수정
        else {
            Log log = logRepository.findById(logDto.getLogIdx())
                    .orElseThrow(() -> new NoDataException("No such Data", ResponseCode.DATA_NOT_FOUND));
            log.LogUpdate(
                    logDto.getLogTitle(),
                    logDto.getPreviewText(),
                    logDto.getIsPublic(),
                    logDto.getLogQuestion()
            );

            return new ResponseEntity<>(new CustomResponse(ResponseCode.SUCCESS, logRepository.save(log)),
                    HttpStatus.valueOf(200));
        }
    }
}
