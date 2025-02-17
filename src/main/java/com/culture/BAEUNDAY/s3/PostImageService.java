package com.culture.BAEUNDAY.s3;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.*;
import com.culture.BAEUNDAY.domain.post.Post;
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
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class PostImageService {

    private final AmazonS3 amazonS3;

    @Value("${cloud.aws.s3.bucket-post}")
    private String bucket;

    public ForImageResponseDTO uploadImg(MultipartFile image) {


        if(image == null || image.isEmpty() || image.getOriginalFilename() == null) {
            throw new IllegalArgumentException("파일이 비어 있거나 파일명이 없습니다.");
        }

        String PostImgAddress = uploadImage(image); //새로운 이미지 주소

        return ForImageResponseDTO.builder()
                .postImg(PostImgAddress)
                .build();
    }

    public ForImageResponseDTO updateImg(Post post, String beforeImageAddress, MultipartFile image) {

        //수정할 이미지가 없다면 기존 게시글 이미지 경로명 반환
        if(image == null || image.isEmpty() || image.getOriginalFilename() == null) {
            return ForImageResponseDTO.builder()
                    .postImg(post.getImgURL())
                    .build();
        }

        //삭제할 게시글 이미지 경로명이 없을 경우 예외 던짐
        if (beforeImageAddress == null) {
            throw new IllegalArgumentException("삭제할 게시글 이미지 경로명이 없습니다.");
        }

        // 새로운 이미지가 제공된 경우
        // 기존 이미지가 존재한다면 삭제 후, 새 이미지 업로드
        deleteImg(beforeImageAddress);
        String newImageAddress = uploadImage(image);

        return ForImageResponseDTO.builder()
                .postImg(newImageAddress)
                .build();



    }

    private String uploadImage(MultipartFile image) {

        validateImageFileExtention(Objects.requireNonNull(image.getOriginalFilename()));

        try {

            return uploadImageToS3(image);


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

    public void deleteImg(String beforeImageAddress) {


        String key = getKeyFromImageAddress(beforeImageAddress);


        try {

            // 객체가 존재하는지 확인
            if (!amazonS3.doesObjectExist(bucket, key)) {
                throw new IllegalArgumentException("S3 저장소에서 삭제할 이미지가 존재하지 않습니다.");
            }

            amazonS3.deleteObject(new DeleteObjectRequest(bucket, key));

        } catch (IllegalArgumentException e) {
            throw e;
        } catch (Exception e) {
            throw new AmazonS3Exception("저장소에 이미지를 삭제하는데 실패하였습니다.");
        }

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
