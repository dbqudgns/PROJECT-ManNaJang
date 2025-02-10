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
            UserDTO user,
            String title,
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
            Integer participants,
            Integer hearts,
            String content,
            String status,
            LocalDateTime createdDate,
            LocalDateTime dateTime

    ){
        public FindByIdDTO(
                Post post, User user, boolean isMyPost, boolean isHearted
        ){
            this(
                    post.getId(),
                    isMyPost,
                    isHearted,
                    new UserDTO(user),
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
                    post.getNumsOfParticipant(),
                    post.getNumsOfHeart(),
                    post.getContent(),
                    post.getStatus().toString(),
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
