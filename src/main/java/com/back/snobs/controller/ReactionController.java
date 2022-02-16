package com.back.snobs.controller;

import com.back.snobs.dto.reaction.ReactionDto;
import com.back.snobs.error.CustomResponse;
import com.back.snobs.security.UserPrincipal;
import com.back.snobs.security.oauth2.CurrentUser;
import com.back.snobs.service.ReactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/reaction")
public class ReactionController {
    private final ReactionService reactionService;

    @PostMapping(value = "/")
    @PreAuthorize("hasRole('GRANTED_USER')")
    public ResponseEntity<CustomResponse> saveReaction(@CurrentUser UserPrincipal userPrincipal,
                                                               @RequestBody ReactionDto reactionDto) {
        return reactionService.createReaction(userPrincipal.getEmail(), reactionDto);
    }

    @GetMapping(value = "/")
    @PreAuthorize("hasRole('GRANTED_USER')")
    public ResponseEntity<CustomResponse> getReaction(@CurrentUser UserPrincipal userPrincipal) {
        return reactionService.readReaction(userPrincipal.getEmail());
    }
}
