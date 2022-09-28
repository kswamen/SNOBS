package com.back.snobs.controller.test;

import com.back.snobs.domain.book.BookDto;
import com.back.snobs.domain.chatroom.chatmessage.ChatMessageDto;
import com.back.snobs.error.CustomResponse;
import com.back.snobs.service.BookService;
import com.back.snobs.service.chatmessage.ChatMessageService;
import com.back.snobs.service.chatmessage.ChatMessageServiceRdb;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Random;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/chatTest")
public class RedisAndMySqlTestController {
//    private final ChatMessageService chatMessageService;
    private final ChatMessageServiceRdb chatMessageService;
    private final List<String> dummyString = List.of(
            "Hello from the other side!",
            "Let it go, let it go, they'll never see me cry.",
            "Country road, take me home. To the place I belong, West Virginia."
    );
    private final List<String> dummyUserIdx = List.of(
            "b0c2eb23-ae1f-44eb-ac11-d57158cb6bfb",
            "32870249-9fb1-470a-8e0f-03c1d0701e29"
    );

    // book save
    @PostMapping(value = "")
    public void chatSave() {
        Random r = new Random();
        String txt = dummyString.get(r.nextInt(3));
        String userIdx = dummyUserIdx.get(r.nextInt(2));

        ChatMessageDto chatMessageDto = ChatMessageDto.builder()
                .chatRoomIdx(1L)
                .message(txt)
                .createDate(System.currentTimeMillis())
                .userIdx(userIdx)
                .build();
        chatMessageService.saveMessage(chatMessageDto);
    }
}
