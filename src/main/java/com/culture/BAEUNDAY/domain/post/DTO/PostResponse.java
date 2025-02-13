package com.culture.BAEUNDAY.domain.post.DTO;

import com.culture.BAEUNDAY.domain.post.Post;
import com.culture.BAEUNDAY.domain.user.User;

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
            boolean isMyPost,
            boolean isHearted,
            boolean isReserved,
            UserDTO user,
            String title,
            String imgURL,
            Integer fee,
            LocalDateTime startDateTime,
            LocalDateTime endDateTime,
            String province,
            String city,
            String address,
            Integer minimumPeople,
            Integer maximumPeople,
            Integer participants,
            Integer hearts,
            String content,
            LocalDateTime createdDate,
            LocalDateTime deadline

    ){
        public FindByIdDTO(
                Post post, User user, boolean isMyPost, boolean isHearted, boolean isReserved
        ){
            this(
                    post.getId(),
                    isMyPost,
                    isHearted,
                    isReserved,
                    new UserDTO(user),
                    post.getTitle(),
                    post.getImgURL(),
                    post.getFee(),
                    post.getStartDateTime(),
                    post.getEndDateTime(),
                    post.getProvince().toString(),
                    post.getCity(),
                    post.getAddress(),
                    post.getMinP(),
                    post.getMaxP(),
                    post.getNumsOfParticipant(),
                    post.getNumsOfHeart(),
                    post.getContent(),
                    post.getCreatedDate(),
                    post.getDeadline()
            );
        }

        public record UserDTO(
                Long userId,
                String name,
                String profileImgURL,
                Integer manner
        ){
            public UserDTO(User user){
                this(
                        user.getId(),
                        user.getName(),
                        user.getProfileImg(),
                        user.getManner()
                );
            }
        }

    }
}
