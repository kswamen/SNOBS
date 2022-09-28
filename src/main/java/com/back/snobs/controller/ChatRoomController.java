package com.back.snobs.controller;

import com.back.snobs.error.CustomResponse;
import com.back.snobs.security.UserPrincipal;
import com.back.snobs.security.oauth2.CurrentUser;
import com.back.snobs.service.chatmessage.ChatMessageService;
import com.back.snobs.service.ChatRoomService;
import com.back.snobs.service.chatmessage.ChatMessageServiceRdb;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/chat")
public class ChatRoomController {
    private final ChatRoomService chatRoomService;
//    private final ChatMessageServiceRdb chatMessageService;
    private final ChatMessageService chatMessageService;

    @PostMapping(value = "/room")
    @PreAuthorize("hasRole('GRANTED_USER')")
    public ResponseEntity<CustomResponse> chatRoomCreate(Long reactionIdx) {
        return chatRoomService.createChatRoom(reactionIdx);
    }

    @GetMapping(value = "/room")
    @PreAuthorize("hasRole('GRANTED_USER')")
    public ResponseEntity<CustomResponse> chatRoomGet(@CurrentUser UserPrincipal userPrincipal) {
        return chatRoomService.getChatRoom(userPrincipal.getEmail());
    }

    @GetMapping(value = "/prevMessage")
    public ResponseEntity<CustomResponse> prevMessage(Long chatRoomIdx) {
        return chatMessageService.getMessage(chatRoomIdx);
    }
}
