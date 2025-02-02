package com.culture.BAEUNDAY.domain.post.DTO;

import com.culture.BAEUNDAY.domain.post.Post;
import lombok.Getter;
import lombok.Setter;

public class PostResponse {

    @Getter
    @Setter
    public static class FindAllDTO {
        private Long postId;
        private String title;


        public FindAllDTO(Post post) {
            this.postId = post.getId();
            this.title = post.getTitle();
        }
    }
}
