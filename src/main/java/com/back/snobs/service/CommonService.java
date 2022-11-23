package com.back.snobs.service;

import com.back.snobs.domain.snob.Snob;
import com.back.snobs.domain.snob.SnobRepository;
import com.back.snobs.domain.snob.profileImage.ProfileImage;
import com.back.snobs.domain.snob.profileImage.ProfileImageRepository;
import com.back.snobs.domain.snob.profileImage.ProfileImageType;
import com.back.snobs.error.CustomResponse;
import com.back.snobs.error.ResponseCode;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CommonService {
    private final ProfileImageRepository profileImageRepository;
    private final SnobRepository snobRepository;

    @Transactional
    public ResponseEntity<CustomResponse> saveOrUpdateProfileImage(
            String userEmail, String filePath, String fileExtension, ProfileImageType profileImageType) {
        Snob s = snobRepository.findByUserEmail(userEmail).orElseThrow();
        Optional<ProfileImage> p = profileImageRepository.findBySnob_UserEmailAndProfileImageType(
                userEmail, profileImageType
        );

        ProfileImage profileImage;
        if (p.isEmpty()) {
            profileImage = ProfileImage.builder()
                    .snob(s)
                    .filePath(filePath)
                    .fileExtension(fileExtension)
                    .profileImageType(profileImageType)
                    .build();

        } else {
            profileImage = p.get();
            profileImage.profileImageUpdate(filePath, fileExtension);
            s.updateMainProfileImage(filePath);
        }
        return new ResponseEntity<>(new CustomResponse(ResponseCode.SUCCESS,
                profileImageRepository.save(profileImage)),
                HttpStatus.valueOf(200));
    }

    public String getProfileImagePath(String userEmail, ProfileImageType profileImageType) {
        ProfileImage p = profileImageRepository.findBySnob_UserEmailAndProfileImageType(userEmail, profileImageType).orElseThrow();
        return p.getFilePath();
    }
}
