package com.culture.BAEUNDAY.domain.post.DTO;

import com.culture.BAEUNDAY.domain.post.Post;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

public class PostResponse {

    public record FindAllDTO(
            List<PostDTO> postList
    ){
        public static FindAllDTO of(
                List<Post> postList
        ){
            return new FindAllDTO(
                    postList.stream()
                            .map(PostDTO::new)
                            .collect(Collectors.toList())
            );
        }
    }
    public record PostDTO(
            Long postId,
            String title,
            String imgURL,
            String province,
            String city,
            String address,
            Integer fee,
            String status,
            LocalDateTime deadline
    ){
        public PostDTO(Post post){
            this(
                    post.getId(),
                    post.getTitle(),
                    post.getImgURL(),
                    post.getProvince().toString(),
                    post.getCity(),
                    post.getAddress(),
                    post.getFee(),
                    post.getStatus().toString(),
                    post.getDeadline()
            );
        }
    }

    public record FindByIdDTO(
            Long id,
            String title,
//            UserDTO user,
            String imgURL,
            String subject,
            String goal,
            String outline,
            String targetStudent,
            String level,
            String contactMethod,
            Integer fee,
            LocalDateTime startDate,
            LocalDateTime endDate,
            String province,
            String city,

            String address,
            Integer minimumPeople,
            Integer maximumPeople,
            String content,
            String status,
            LocalDateTime createdDate,
            LocalDateTime dateTime

    ){
        public FindByIdDTO(
                Post post
//                , User user
        ){
            this(
                    post.getId(),
                    post.getTitle(),
                    post.getImgURL(),
                    post.getSubject(),
                    post.getGoal(),
                    post.getOutline(),
                    post.getTargetStudent(),
                    post.getLevel(),
                    post.getContactMethod(),
                    post.getFee(),
                    post.getStartDate(),
                    post.getEndDate(),
                    post.getProvince().toString(),
                    post.getCity(),
                    post.getAddress(),
                    post.getMinP(),
                    post.getMaxP(),
                    post.getContent(),
                    post.getStatus().toString(),
                    post.getCreatedDate(),
                    post.getDeadline()
            );
        }

//        public record UserDTO(
//                String name,
//                String profileImgURL,
//                String manner
//        ){
//            public UserDTO(User user){
//                this(
//                        user.getname(),
//                        user.getimgURL(),
//                        user.getmanner()
//                );
//            }
//        }

    }
}
