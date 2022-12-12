package com.back.snobs.controller;

import com.back.snobs.other.CreateDummyData;
import com.back.snobs.security.interceptor.CustomInterceptor;
import com.back.snobs.service.BookService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@Transactional
@AutoConfigureMockMvc
//@DataJpaTest
@ExtendWith(SpringExtension.class)
//@TestPropertySource("classpath:application-test.properties")
//@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class BookControllerTest {
    @Autowired
    MockMvc mvc;
    @Autowired
    ObjectMapper objectMapper;
    @MockBean
    CustomInterceptor customInterceptor;
    @MockBean
    BookService bookService;
    private final String API_BASE = "/api/book";

    @BeforeEach
    void init() {
        when(customInterceptor.preHandle(any(), any(), any())).thenReturn(true);
    }

    @Test
    @DisplayName("Kakao API - 책 검색, 쿼리스트링 미포함")
    void searchBookWithNoQueryStringKakaoAPI() throws Exception {
        // given

        // when

        // then
        mvc.perform(get(API_BASE + "/search"))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Kakao API - 책 검색")
    void searchBookKakaoAPI() throws Exception {
        // given

        // when

        // then
        mvc.perform(get(API_BASE + "/search").param("query", "something"))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("책 검색, 쿼리스트링 미포함")
    void searchBookWithNoQueryString() throws Exception {
        // given

        // when

        // then
        mvc.perform(get(API_BASE))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("책 검색")
    void searchBook() throws Exception {
        // given

        // when

        // then
        mvc.perform(get(API_BASE).param("bookIdx", "1"))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("책 등록")
    void createBook() throws Exception {

        // given

        // when

        // then
        String content = objectMapper.writeValueAsString(CreateDummyData.getOneBook());

        mvc.perform(post(API_BASE)
                        .content(content)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }
}