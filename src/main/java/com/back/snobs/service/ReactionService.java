package com.back.snobs.service;

import com.back.snobs.dto.log.Log;
import com.back.snobs.dto.log.LogRepository;
import com.back.snobs.dto.reaction.Reaction;
import com.back.snobs.dto.reaction.ReactionDto;
import com.back.snobs.dto.reaction.ReactionRepository;
import com.back.snobs.dto.snob.Snob;
import com.back.snobs.dto.snob.SnobRepository;
import com.back.snobs.error.CustomResponse;
import com.back.snobs.error.ResponseCode;
import com.back.snobs.error.exception.NoDataException;
import com.back.snobs.error.exception.ReactionDuplicateException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ReactionService {
    private final ReactionRepository reactionRepository;
    private final SnobRepository snobRepository;
    private final LogRepository logRepository;

    public ResponseEntity<CustomResponse> readReaction(String userEmail) {
        List<Reaction> reactionList = reactionRepository.findAllByReceiverSnob_userEmail(userEmail);

        return new ResponseEntity<>(new CustomResponse(ResponseCode.SUCCESS, reactionList),
                HttpStatus.valueOf(200));
    }

    @Transactional
    public ResponseEntity<CustomResponse> createReaction(String userEmail, ReactionDto reactionDto) {
        Optional<Reaction> reaction = reactionRepository.findBysenderEmailAndlogIdx(userEmail, reactionDto.getLogIdx());
        // 이미 존재하는 리액션
        if (reaction.isPresent()) {
            throw new ReactionDuplicateException("Reaction Already Exists", ResponseCode.REACTION_DUPLICATION);
        } else {
            Snob senderSnob = snobRepository.findById(userEmail).orElseThrow(() -> new NoDataException("No Such Data", ResponseCode.DATA_NOT_FOUND));
            Log log = logRepository.findById(reactionDto.getLogIdx()).orElseThrow(() -> new NoDataException("No Such Data", ResponseCode.DATA_NOT_FOUND));
            Snob receiverSnob = log.getSnob();

            reactionDto.setSenderSnob(senderSnob);
            reactionDto.setReceiverSnob(receiverSnob);
            reactionDto.setLog(log);

            return new ResponseEntity<>(new CustomResponse(ResponseCode.SUCCESS, reactionRepository.save(reactionDto.toEntity())),
                    HttpStatus.valueOf(200));
        }
    }
}
