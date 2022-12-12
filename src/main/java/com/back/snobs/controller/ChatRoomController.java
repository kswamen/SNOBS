package com.back.snobs.controller;

import com.back.snobs.domain.chatroom.chatmessage.ChatMessageDto;
import com.back.snobs.domain.snob.Role;
import com.back.snobs.error.CustomResponse;
import com.back.snobs.security.UserPrincipal;
import com.back.snobs.security.interceptor.CustomPreAuthorize;
import com.back.snobs.security.oauth2.CurrentUser;
import com.back.snobs.service.ChatRoomService;
import com.back.snobs.service.chatmessage.ChatMessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/chat")
public class ChatRoomController {
    private final ChatRoomService chatRoomService;
    // MySQL
//    private final ChatMessageServiceRdb chatMessageService;
//     Redis
     private final ChatMessageService chatMessageService;

    @GetMapping(value = "/test")
    public ChatMessageDto chatMessageTest() {
        ChatMessageDto chatMessageDto = ChatMessageDto.builder()
                .chatRoomIdx(1L)
                .message("Sample Message")
                .createDate(999999999L)
                .userIdx(1L).build();

        return chatMessageService.saveMessage(chatMessageDto);
    }

    @PostMapping(value = "/room")
    @CustomPreAuthorize(role = Role.GRANTED_USER)
//    @PreAuthorize("hasRole('GRANTED_USER')")
    public ResponseEntity<CustomResponse> chatRoomCreate(Long reactionIdx) {
        return chatRoomService.createChatRoom(reactionIdx);
    }

    @GetMapping(value = "/room")
    @CustomPreAuthorize(role = Role.GRANTED_USER)
//    @PreAuthorize("hasRole('GRANTED_USER')")
    public ResponseEntity<CustomResponse> chatRoomGet(@CurrentUser UserPrincipal userPrincipal) {
        return chatRoomService.getChatRoom(userPrincipal.getEmail());
    }

    @GetMapping(value = "/prevMessage")
    public ResponseEntity<CustomResponse> prevMessage(Long chatRoomIdx) {
        return chatMessageService.getMessage(chatRoomIdx);
    }
}
