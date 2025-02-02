package com.culture.BAEUNDAY.domain.post;


import com.culture.BAEUNDAY.domain.post.DTO.PostResponse;
import com.culture.BAEUNDAY.utils.ApiUtils;
import com.culture.BAEUNDAY.utils.PageResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/posts")
@RequiredArgsConstructor
public class PostRestController {

    private final PostService postService;


    @GetMapping
    public ResponseEntity<?> findAllPost(@RequestParam(value = "sort") String sort,
                                         @RequestParam(value = "status") String status,
                                         @RequestParam(value = "fee") String fee,
                                         @RequestParam(value = "cursor", required = false) String cursor,
                                         @RequestParam(value = "cursorID", required = false) Long cursorId ) {

        PageResponse<?, PostResponse.FindAllDTO> responseDTO = postService.findAll(sort,status,fee,cursor,cursorId);
        return ResponseEntity.ok(ApiUtils.success(responseDTO));
    }


}
