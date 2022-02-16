package com.back.snobs.service;

import com.back.snobs.dto.book.Book;
import com.back.snobs.dto.book.BookRepository;
import com.back.snobs.dto.log.Log;
import com.back.snobs.dto.log.LogDto;
import com.back.snobs.dto.log.LogRepository;
import com.back.snobs.dto.snob.Snob;
import com.back.snobs.dto.snob.SnobRepository;
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
    public ResponseEntity<CustomResponse> readLog(String userEmail) {
        List<Log> ls = logRepository.findBySnob_UserEmail(userEmail);
        return new ResponseEntity<>(new CustomResponse(ResponseCode.SUCCESS, ls),
                HttpStatus.valueOf(200));
    }

    public ResponseEntity<CustomResponse> readLog(Long logIdx) {
        return new ResponseEntity<>(new CustomResponse(ResponseCode.SUCCESS,
                logRepository.findById(logIdx).orElseThrow(() -> new NoDataException("No Such Data", ResponseCode.DATA_NOT_FOUND))),
                HttpStatus.valueOf(200));
    }

    @Transactional
    public ResponseEntity<CustomResponse> deleteLog(Long logIdx, String userEmail) {
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
    public ResponseEntity<CustomResponse> saveOrUpdate(LogDto logDto, String userEmail) {
        // 새롭게 생성
        if (logDto.getLogIdx() == null) {
            Book book = bookRepository.findById(logDto.getBookId())
                    .orElseThrow(() -> new NoDataException("No such Data", ResponseCode.DATA_NOT_FOUND));
            Snob snob = snobRepository.findById(userEmail)
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
