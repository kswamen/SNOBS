package com.back.snobs.domain;

import com.back.snobs.domain.book.Book;
import com.back.snobs.domain.book.BookRepository;
import com.back.snobs.domain.log.Log;
import com.back.snobs.domain.log.LogRepository;
import com.back.snobs.domain.snob.LoginType;
import com.back.snobs.domain.snob.Role;
import com.back.snobs.domain.snob.Snob;
import com.back.snobs.domain.snob.SnobRepository;
import com.back.snobs.util.CreateDummyData;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ExtendWith(SpringExtension.class)
@TestPropertySource("classpath:application-test.properties")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class RepositoriesTest {
    @Autowired
    private BookRepository bookRepository;
    @Autowired
    private SnobRepository snobRepository;
    @Autowired
    private LogRepository logRepository;

    Snob snob;
    Book book;
    Log log;

    @BeforeEach
    void setup() {
        snob = CreateDummyData.getOneSnob(LoginType.kakao, Role.GRANTED_USER);
        book = CreateDummyData.getOneBook();
        log = CreateDummyData.getOneLog(snob, book);

        bookRepository.save(book);
        snobRepository.save(snob);
        logRepository.save(log);
    }

    @Test
    @DisplayName("Book - 저장")
    void bookSave() {
        Book bookSaved = bookRepository.save(book);
        assertEquals(bookSaved.getBookId(), bookSaved.getBookId());
    }

    @Test
    @DisplayName("Book - 조회")
    void bookRead() {
        Book bookRead = bookRepository.findById(book.getBookId()).orElse(null);
        assertNotNull(bookRead);
        assertEquals(bookRead.getBookId(), book.getBookId());
    }

    @Test
    @Disabled
    @DisplayName("Book - 수정")
    void bookUpdate() {

    }

    @Test
    @DisplayName("Book - 삭제")
    void bookDelete() {
        bookRepository.save(book);
        bookRepository.deleteById(book.getBookId());
        assertTrue(bookRepository.findById(book.getBookId()).isEmpty());
    }

    @Test
    @DisplayName("Snob - 저장")
    void snobSave() {
        Snob snobSaved = snobRepository.save(snob);
        assertEquals(snobSaved.getSnobIdx(), snobSaved.getSnobIdx());
    }

    @Test
    @DisplayName("Snob - 조회")
    void snobRead() {
        Snob snobRead = snobRepository.findBySnobIdx(snob.getSnobIdx()).orElse(null);
        assertNotNull(snobRead);
        assertEquals(snobRead.getSnobIdx(), snob.getSnobIdx());
    }

    @Test
    @Disabled
    @DisplayName("Snob - 수정")
    void snobUpdate() {

    }

    @Test
    @DisplayName("Snob - 삭제")
    void snobDelete() {
        // 유저 삭제는 기본적으로 지원하지 않는다.
    }

    @Test
    @DisplayName("Log - 저장")
    void logSave() {
        Log logSaved = logRepository.save(log);
        assertEquals(logSaved.getLogIdx(), log.getLogIdx());
    }

    @Test
    @DisplayName("Log - 조회")
    void logRead() {
        Log logRead = logRepository.findById(log.getLogIdx()).orElse(null);
        assertNotNull(logRead);
        assertEquals(logRead.getLogIdx(), log.getLogIdx());
    }

    @Test
    @Disabled
    @DisplayName("Log - 수정")
    void logUpdate() {
        String changedTitle = "my test changed title";
        log.LogUpdate(
                 changedTitle,
                "previewText changed",
                true,
                "logQuestion changed"
        );
        logRepository.save(log);
        assertEquals(
                logRepository.findById(log.getLogIdx()).get().getLogTitle(),
                changedTitle
        );
    }

    @Test
    @DisplayName("Log - 삭제")
    void logDelete() {
        logRepository.deleteById(log.getLogIdx());
        assertTrue(logRepository.findById(log.getLogIdx()).isEmpty());
    }
}
