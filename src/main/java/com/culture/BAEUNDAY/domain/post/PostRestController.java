package com.culture.BAEUNDAY.domain.post;


import com.culture.BAEUNDAY.domain.post.DTO.PostRequest;
import com.culture.BAEUNDAY.domain.post.DTO.PostResponse;
import com.culture.BAEUNDAY.jwt.Custom.CustomUserDetails;
import com.culture.BAEUNDAY.utils.ApiUtils;
import com.culture.BAEUNDAY.utils.PageResponse;
import com.culture.BAEUNDAY.utils.s3.ForImageResponseDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/posts")
@RequiredArgsConstructor
@Tag(name = "포스트 api", description = "포스트 관련 기능")
public class PostRestController {

    private final PostService postService;

    @Operation(summary = "모든 프로그램 조회 ( 홈 ) sort = recent/heart/deadline , status = ING/AVAILBLE/END , fee = FREE/UNDER_3/BETWEEN3_5/BETWEEN5_10/OVER_10 ")
    @GetMapping
    public ResponseEntity<?> findAllPost(@RequestParam(value = "sort", defaultValue = "recent") String sort,
                                         @RequestParam(value = "status", required = false) Status status,
                                         @RequestParam(value = "feeRange", required = false) FeeRange feeRange,
                                         @RequestParam(value = "province", required = false, defaultValue = "전국") Province province,
                                         @RequestParam(value = "city", required = false)  String city,
                                         @RequestParam(value = "cursor", required = false) String cursor,
                                         @RequestParam(value = "cursorId", required = false) Long cursorId ) {
        // 문자열 provinceStr을 Province enum으로 변환

        PageResponse<?, PostResponse.FindAllDTO> responseDTO = postService.findAll(sort,status, feeRange, province, city, cursor,cursorId);
        return ResponseEntity.ok(ApiUtils.success(responseDTO));
    }


    @Operation(summary = "특정 프로그램 조회")
    @GetMapping("/{postId}")
    public  ResponseEntity<?> findPostById( @AuthenticationPrincipal CustomUserDetails userDetails,
                                            @PathVariable(name = "postId") Long postId ){
        PageResponse<?, PostResponse.FindByIdDTO> responseDTO = postService.findById(postId, userDetails.getUsername());
        return ResponseEntity.ok(ApiUtils.success(responseDTO));

    }
    @Operation(summary = "프로그램 생성")
    @PostMapping(consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<?> create(@AuthenticationPrincipal CustomUserDetails userDetails,
                                    @RequestPart(value = "image") MultipartFile image,
                                    @RequestPart(value = "info") @Valid PostRequest.PostRequestDto request
    ){
        ForImageResponseDTO responseDTO = postService.create(userDetails.getUsername(), image, request);
        return ResponseEntity.ok(ApiUtils.success(responseDTO));
    }

    @Operation(summary = "프로그램 수정")
    @PutMapping(value = "/{postId}", consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<?> update(@AuthenticationPrincipal CustomUserDetails userDetails,
                                    @PathVariable(name = "postId") Long postId,
                                    @RequestParam(value = "imageAddress", required = false) String imageAddress,
                                    @RequestPart(value = "image", required = false) MultipartFile image,
                                    @RequestPart(value = "info") PostRequest.PostRequestDto request
    ){
        ForImageResponseDTO responseDTO = postService.update(userDetails.getUsername(), postId, imageAddress, image, request);
        return ResponseEntity.ok(ApiUtils.success(responseDTO));
    }


    @Operation(summary = "프로그램 삭제")
    @DeleteMapping("/{postId}")
    public ResponseEntity<?> delete(@AuthenticationPrincipal CustomUserDetails userDetails,
                                    @PathVariable(name = "postId") Long postId
            ){
        postService.delete(userDetails.getUsername(), postId);
        return ResponseEntity.ok().body(ApiUtils.success("프로그램 삭제 완료"));
    }


}
