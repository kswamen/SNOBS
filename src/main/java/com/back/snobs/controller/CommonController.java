package com.back.snobs.controller;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.*;
import com.back.snobs.domain.log.Log;
import com.back.snobs.domain.log.LogRepository;
import com.back.snobs.domain.snob.Snob;
import com.back.snobs.domain.snob.SnobRepository;
import com.back.snobs.domain.snob.profileImage.ProfileImageType;
import com.back.snobs.error.CustomResponse;
import com.back.snobs.error.ResponseCode;
import com.back.snobs.error.exception.NoDataException;
import com.back.snobs.security.UserPrincipal;
import com.back.snobs.security.oauth2.CurrentUser;
import com.back.snobs.service.CommonService;
import lombok.RequiredArgsConstructor;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/common")
public class CommonController {
    @Value("${common.imageUploadDirectories}")
    private String uploadPath;
    private final LogRepository logRepository;
    private final SnobRepository snobRepository;
    private final CommonService commonService;
    private final AmazonS3Client amazonS3Client;
    @Value("${cloud.aws.s3.bucket}")
    public String bucket;

    @GetMapping("/myProfileImage")
    @PreAuthorize("hasAnyRole('USER', 'GRANTED_USER')")
    public ResponseEntity<Resource> download(@CurrentUser UserPrincipal userPrincipal, @RequestParam(required = false) ProfileImageType profileImageType) throws IOException {
        if(profileImageType == null) profileImageType = ProfileImageType.FIRST;
        String profileImagePath = commonService.getProfileImagePath(userPrincipal.getEmail(), profileImageType);
        final ByteArrayResource inputStream = new ByteArrayResource(Files.readAllBytes(Paths.get(profileImagePath)));

        return ResponseEntity
                .status(HttpStatus.OK)
                .contentLength(inputStream.contentLength())
                .body(inputStream);
    }

    @GetMapping("/profileImage/byLogIdx")
    @PreAuthorize("hasRole('GRANTED_USER')")
    public ResponseEntity<Resource> byLogIdx(@CurrentUser UserPrincipal userPrincipal, @RequestParam Long logIdx, @RequestParam(required = false) ProfileImageType profileImageType) throws IOException {
        if(profileImageType == null) profileImageType = ProfileImageType.FIRST;
        Log log = logRepository.findById(logIdx).orElseThrow(() -> new NoDataException("No Such Data", ResponseCode.DATA_NOT_FOUND));

        String profileImagePath = commonService.getProfileImagePath(log.getSnob().getUserEmail(), profileImageType);
        final ByteArrayResource inputStream = new ByteArrayResource(Files.readAllBytes(Paths.get(profileImagePath)));

        return ResponseEntity
                .status(HttpStatus.OK)
                .contentLength(inputStream.contentLength())
                .body(inputStream);
    }

    @GetMapping("/profileImage/byUID")
    @PreAuthorize("hasRole('GRANTED_USER')")
    public ResponseEntity<Resource> byReactionIdx(@CurrentUser UserPrincipal userPrincipal, @RequestParam(required = false) ProfileImageType profileImageType) throws IOException {
        if(profileImageType == null) profileImageType = ProfileImageType.FIRST;
        Snob snob = snobRepository.findByUserEmail(userPrincipal.getEmail()).orElseThrow(() -> new NoDataException("No Such Data", ResponseCode.DATA_NOT_FOUND));
        String profileImagePath = commonService.getProfileImagePath(snob.getUserEmail(), profileImageType);

        String[] params = profileImagePath.split("\\\\");
        S3Object o = amazonS3Client.getObject(new GetObjectRequest(bucket + "/" + params[0], params[1]));
        S3ObjectInputStream objectInputStream = o.getObjectContent();
        byte[] bytes = IOUtils.toByteArray(objectInputStream);
        final ByteArrayResource inputStream = new ByteArrayResource(bytes);

        return ResponseEntity
                .status(HttpStatus.OK)
                .contentLength(inputStream.contentLength())
                .body(inputStream);
    }

//    @GetMapping("/profileImage/byUID")
//    @PreAuthorize("hasRole('GRANTED_USER')")
//    public ResponseEntity<Resource> byReactionIdx(@CurrentUser UserPrincipal userPrincipal, @RequestParam String UID, @RequestParam(required = false) ProfileImageType profileImageType) throws IOException {
//        if(profileImageType == null) profileImageType = ProfileImageType.FIRST;
//        Snob snob = snobRepository.findBySnobIdx(UID).orElseThrow(() -> new NoDataException("No Such Data", ResponseCode.DATA_NOT_FOUND));
//        String profileImagePath = commonService.getProfileImagePath(snob.getUserEmail(), profileImageType);
//        final ByteArrayResource inputStream = new ByteArrayResource(Files.readAllBytes(Paths.get(profileImagePath)));
//
//        return ResponseEntity
//                .status(HttpStatus.OK)
//                .contentLength(inputStream.contentLength())
//                .body(inputStream);
//    }

    // file upload
    @PostMapping(value = "/myProfileImage", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("hasAnyRole('USER', 'GRANTED_USER')")
    public ResponseEntity<CustomResponse> upload(
            @CurrentUser UserPrincipal userPrincipal,
            @RequestParam("file") MultipartFile file,
            @RequestParam(required = false) ProfileImageType profileImageType) throws IOException {
        if(profileImageType == null) profileImageType = ProfileImageType.FIRST;
        String userEmail = userPrincipal.getEmail();

        String ext = StringUtils.getFilenameExtension(file.getOriginalFilename());
        String uniqueFileName = UUID.randomUUID() + "." + ext;
        String filePath = uploadPath + "/" + LocalDate.now();

        ObjectMetadata omd = new ObjectMetadata();
        omd.setContentType(file.getContentType());
        omd.setContentLength(file.getSize());
        omd.setHeader("fileName", file.getOriginalFilename());
        amazonS3Client.putObject(new PutObjectRequest(bucket + "/" + filePath, uniqueFileName,
                file.getInputStream(), omd));

        return commonService.saveOrUpdateProfileImage(userEmail,
                filePath + '\\' + uniqueFileName, ext, profileImageType);
    }

//    // file upload
//    @PostMapping("/myProfileImage")
//    @PreAuthorize("hasAnyRole('USER', 'GRANTED_USER')")
//    public ResponseEntity<CustomResponse> upload(
//            @CurrentUser UserPrincipal userPrincipal, @RequestParam("file")MultipartFile file,
//            ProfileImageType profileImageType) {
////        System.out.println("파일 이름 : " + file.getOriginalFilename());
////        System.out.println("파일 크기 : " + file.getSize());
//
//        String userEmail = userPrincipal.getEmail();
//
////        Snob s = snobRepository.findById("a@a.com").orElseThrow();
////        System.out.println(s.getCreateDate().toLocalDate());
//
//        String ext = StringUtils.getFilenameExtension(file.getOriginalFilename());
//        String uniqueFileName = UUID.randomUUID() + "." + ext;
//        String filePath = uploadPath + File.separator + LocalDate.now();
//        try {
//            File f = new File(filePath);
//            if(!f.exists()) {
//                try {
//                    f.mkdirs();
//                } catch(Exception e) {
//                    e.printStackTrace();
//                }
//            }
//            FileOutputStream fos = new FileOutputStream(f.getAbsolutePath()
//                    + File.separator + uniqueFileName);
//            InputStream is = file.getInputStream();
//            int readCount = 0;
//            byte[] buffer = new byte[1024];
//
//            while ((readCount = is.read(buffer)) != -1) {
//                fos.write(buffer, 0, readCount);
//            }
//
//            fos.close();
//            is.close();
//        } catch (Exception ex) {
//            throw new RuntimeException("file Save Error");
//        }
//
//        return commonService.saveOrUpdateProfileImage(userEmail,
//                filePath + File.separator + uniqueFileName, ext, profileImageType);
//    }
}
