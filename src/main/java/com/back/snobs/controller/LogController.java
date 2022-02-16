package com.back.snobs.controller;

import com.back.snobs.dto.log.LogDto;
import com.back.snobs.error.CustomResponse;
import com.back.snobs.security.UserPrincipal;
import com.back.snobs.security.oauth2.CurrentUser;
import com.back.snobs.service.LogService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/log")
public class LogController {
    private final LogService logService;

    // 로그 조회(유저 이메일로)
    @GetMapping(value = "/my")
    @PreAuthorize("hasRole('GRANTED_USER')")
    public ResponseEntity<CustomResponse> logRead(@CurrentUser UserPrincipal userPrincipal){
        return logService.readLog(userPrincipal.getEmail());
    }

    // 로그 조회(로그 번호로)
    @GetMapping(value = "/byLogIdx")
    @PreAuthorize("hasRole('GRANTED_USER')")
    public ResponseEntity<CustomResponse> logRead(Long logIdx) {
        return logService.readLog(logIdx);
    }

    // 로그 등록
    @PostMapping(value = "/")
    @PreAuthorize("hasRole('GRANTED_USER')")
    public ResponseEntity<CustomResponse> logSaveOrUpdate(@CurrentUser UserPrincipal userPrincipal, @RequestBody LogDto logDto) {
        return logService.saveOrUpdate(logDto, userPrincipal.getEmail());
    }

    @DeleteMapping(value = "/")
    @PreAuthorize("hasRole('GRANTED_USER')")
    public ResponseEntity<CustomResponse> logDelete(@CurrentUser UserPrincipal userPrincipal, Long logIdx) {
        return logService.deleteLog(logIdx, userPrincipal.getEmail());
    }
}
