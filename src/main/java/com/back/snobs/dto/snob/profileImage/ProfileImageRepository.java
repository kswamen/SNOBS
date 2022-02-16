package com.back.snobs.dto.snob.profileImage;

import com.back.snobs.dto.snob.Snob;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ProfileImageRepository extends JpaRepository<ProfileImage, Long> {
    Optional<ProfileImage> findBySnob_UserEmailAndProfileImageType(String userEmail, ProfileImageType profileImageType);

    List<ProfileImage> findBySnob(Snob snob);
}
