package com.culture.BAEUNDAY.s3;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.*;
import com.culture.BAEUNDAY.domain.user.User;
import com.culture.BAEUNDAY.domain.user.UserService;
import com.culture.BAEUNDAY.jwt.Custom.CustomUserDetails;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class UserImageService {

    private final AmazonS3 amazonS3;
    private final UserService userService;

    private static final String DEFAULT_IMAGE_URL = "https://rootimpact11-user.s3.ap-northeast-2.amazonaws.com/defaultImage.png";

    @Value("${cloud.aws.s3.bucket-user}")
    private String bucket;

    public ImageResponseDTO uploadImg(CustomUserDetails customUserDetails, String imageAddress, MultipartFile image) {

        User user = userService.findUserByUsernameOrThrow(customUserDetails.getUsername());

        if(image == null || image.isEmpty() || image.getOriginalFilename() == null) {
            throw new IllegalArgumentException("파일이 비어 있거나 파일명이 없습니다.");
        }

        deleteImg(customUserDetails, imageAddress); //기존 이미지를 삭제하고
        String ProfileImgAddress =  uploadImage(user, image); //새로운 이미지로 수정한다.

        return ImageResponseDTO.builder()
                .name(user.getName())
                .profileImg(ProfileImgAddress)
                .build();
    }

    private String uploadImage(User user, MultipartFile image) {

        validateImageFileExtention(Objects.requireNonNull(image.getOriginalFilename()));

        try {

            String profileImageAddress = uploadImageToS3(image);
            user.profileImg(profileImageAddress);

            return profileImageAddress;


        } catch (IOException e) {
            throw new IllegalArgumentException("이미지를 저장하는데 실패하였습니다.");
        }

    }

    private void validateImageFileExtention(String filename) {

        int lastDotIndex = filename.lastIndexOf(".");
        if (lastDotIndex == -1) {
            throw new IllegalArgumentException("파일 확장자가 없습니다.");
        }

        String extention = filename.substring(lastDotIndex + 1).toLowerCase();
        List<String> allowedExtentionList = Arrays.asList("jpg", "jpeg", "png", "gif");

        if (!allowedExtentionList.contains(extention)) {
            throw new IllegalArgumentException("파일 확장자가 올바르지 않습니다.");
        }
    }

    private String uploadImageToS3(MultipartFile image) throws IOException {

        String originalFilename = image.getOriginalFilename(); //원본 파일 명

        String s3FileName = UUID.randomUUID().toString().substring(0, 10) + originalFilename; //S3에 저장될 파일명

        ObjectMetadata metadata = new ObjectMetadata(); //파일에 대한 정보를 저장하는 객체
        metadata.setContentType(image.getContentType());
        metadata.setContentLength(image.getSize()); //파일의 크기 지정

        try (InputStream inputStream = image.getInputStream()) {
            PutObjectRequest putObjectRequest =
                    new PutObjectRequest(bucket, s3FileName, inputStream, metadata)
                            .withCannedAcl(CannedAccessControlList.PublicRead); //업로드한 파일이 공개적으로 읽힐 수 있도록 권한을 설정

            amazonS3.putObject(putObjectRequest); //실제로 S3에 업로드
        } catch (Exception e) {
            throw new AmazonS3Exception("저장소에 이미지를 저장하는데 실패하였습니다.");
        }

        return amazonS3.getUrl(bucket, s3FileName).toString(); //S3에 저장된 파일 경로명 반환

    }

    public ImageResponseDTO deleteImg(CustomUserDetails customUserDetails, String imageAddress) {

        User user = userService.findUserByUsernameOrThrow(customUserDetails.getUsername());

        //imageAddress가 기본 이미지 경로명이면 key = null
        String key = getKeyFromImageAddressBeforeUpdate(imageAddress);

        if (key != null) {

            try {
                amazonS3.deleteObject(new DeleteObjectRequest(bucket, key));
            } catch (Exception e) {
                throw new AmazonS3Exception("저장소에 이미지를 삭제하는데 실패하였습니다.");
            }

        }

        user.profileImg(DEFAULT_IMAGE_URL);

        return ImageResponseDTO.builder()
                .name(user.getName())
                .profileImg(user.getProfileImg())
                .build();
    }

    private String getKeyFromImageAddressBeforeUpdate(String imageAddress){

        if (imageAddress.equals(DEFAULT_IMAGE_URL)) {
            return null;
        }

        return getKeyFromImageAddress(imageAddress);
    }

    private String getKeyFromImageAddress(String imageAddress){

        try{

            URL url = new URL(imageAddress);

            String decodingKey = URLDecoder.decode(url.getPath(), StandardCharsets.UTF_8);

            return decodingKey.substring(1); // 맨 앞의 '/' 제거
        }catch (MalformedURLException | StringIndexOutOfBoundsException e){
            throw new IllegalArgumentException("해당 이미지를 찾을 수 없습니다.");
        }
    }



}
