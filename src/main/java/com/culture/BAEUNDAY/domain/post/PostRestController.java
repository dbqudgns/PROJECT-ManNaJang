package com.culture.BAEUNDAY.domain.post;


import com.culture.BAEUNDAY.domain.post.DTO.PostRequest;
import com.culture.BAEUNDAY.domain.post.DTO.PostResponse;
import com.culture.BAEUNDAY.jwt.Custom.CustomUserDetails;
import com.culture.BAEUNDAY.utils.ApiUtils;
import com.culture.BAEUNDAY.utils.PageResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/posts")
@RequiredArgsConstructor
@Tag(name = "포스트 api", description = "포스트 관련 기능")
public class PostRestController {

    private final PostService postService;

    @Operation(summary = "모든 프로그램 조회 ( 홈 ) sort = id/heart/recent , status = ING/AVAILBLE/END , fee = FREE/UNDER_3/BETWEEN3_5/BETWEEN5_10/OVER_10 ")
    @GetMapping
    public ResponseEntity<?> findAllPost(@RequestParam(value = "sort", defaultValue = "id") String sort,
                                         @RequestParam(value = "status", required = false) Status status,
                                         @RequestParam(value = "fee", required = false) Fee fee,
                                         @RequestParam(value = "cursor", required = false) String cursor,
                                         @RequestParam(value = "cursorId", required = false) Long cursorId ) {

        PageResponse<?, PostResponse.FindAllDTO> responseDTO = postService.findAll(sort,status,fee,cursor,cursorId);
        return ResponseEntity.ok(ApiUtils.success(responseDTO));
    }

    @Operation(summary = "특정 프로그램 조회")
    @GetMapping("/{postId}")
    public  ResponseEntity<?> findPostById(@PathVariable(name = "postId") Long postId ){
        PageResponse<?, PostResponse.FindByIdDTO> responseDTO = postService.findById(postId);
        return ResponseEntity.ok(ApiUtils.success(responseDTO));

    }
    @Operation(summary = "프로그램 생성")
    @PostMapping
    public ResponseEntity<?> create(@AuthenticationPrincipal CustomUserDetails userDetails,
                                    @RequestBody @Valid PostRequest.PostRequestDto request
    ){
        postService.create(userDetails.getUsername(), request);
        return ResponseEntity.ok().body(ApiUtils.success("프로그램 생성 완료"));
    }

    @Operation(summary = "프로그램 수정")
    @PutMapping("/{postId}")
    public ResponseEntity<?> update(@AuthenticationPrincipal CustomUserDetails userDetails,
                                    @PathVariable(name = "postId") Long postId,
                                    @RequestBody PostRequest.PostRequestDto request
    ){
        postService.update(userDetails.getUsername(), postId, request);
        return ResponseEntity.ok().body(ApiUtils.success("프로그램 수정 완료"));
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
