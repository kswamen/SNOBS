package com.back.snobs.controller;

import com.back.snobs.domain.log.LogDto;
import com.back.snobs.domain.snob.Role;
import com.back.snobs.error.CustomResponse;
import com.back.snobs.security.UserPrincipal;
import com.back.snobs.security.interceptor.CustomPreAuthorize;
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
    @CustomPreAuthorize(role = Role.GRANTED_USER)
//    @PreAuthorize("hasRole('GRANTED_USER')")
    public ResponseEntity<CustomResponse> logRead(@CurrentUser UserPrincipal userPrincipal){
        return logService.read(userPrincipal.getEmail());
    }

    // 로그 조회(로그 번호로)
    @GetMapping(value = "/byLogIdx")
//    @PreAuthorize("hasRole('GRANTED_USER')")
    @CustomPreAuthorize(role = Role.GRANTED_USER)
    public ResponseEntity<CustomResponse> logRead(Long logIdx) {
        return logService.read(logIdx);
    }

    // 로그 등록
    @PostMapping(value = "")
//    @PreAuthorize("hasRole('GRANTED_USER')")
    @CustomPreAuthorize(role = Role.GRANTED_USER)
    public ResponseEntity<CustomResponse> logSaveOrUpdate(@CurrentUser UserPrincipal userPrincipal, @RequestBody LogDto logDto) {
        return logService.save(logDto, userPrincipal.getEmail());
    }

    @DeleteMapping(value = "")
    @CustomPreAuthorize(role = Role.GRANTED_USER)
//    @PreAuthorize("hasRole('GRANTED_USER')")
    public ResponseEntity<CustomResponse> logDelete(@CurrentUser UserPrincipal userPrincipal, Long logIdx) {
        return logService.delete(logIdx, userPrincipal.getEmail());
    }
}
