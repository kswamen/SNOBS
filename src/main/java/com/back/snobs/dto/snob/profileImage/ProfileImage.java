package com.back.snobs.dto.snob.profileImage;


import com.back.snobs.dto.BaseTimeEntity;
import com.back.snobs.dto.snob.Snob;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicInsert;

import javax.persistence.*;

@Entity
@NoArgsConstructor
@DynamicInsert
@Getter
public class ProfileImage extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long profileImageIdx;

    @ManyToOne(cascade = CascadeType.REMOVE)
    @JsonIgnore
    @JoinColumn(name = "writerEmail")
    private Snob snob;

    @Column(nullable = false)
    @JsonIgnore
    private String filePath;

    @Column
    private String fileExtension;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ProfileImageType profileImageType;

    @Builder
    public ProfileImage(Snob snob, String filePath, String fileExtension, ProfileImageType profileImageType) {
        this.snob = snob;
        this.filePath = filePath;
        this.fileExtension = fileExtension;
        this.profileImageType = profileImageType;
    }

    public void profileImageUpdate(String filePath, String fileExtension) {
        this.filePath = filePath;
        this.fileExtension = fileExtension;
    }
}
