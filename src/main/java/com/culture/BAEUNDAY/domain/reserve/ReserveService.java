package com.culture.BAEUNDAY.domain.reserve;


import com.culture.BAEUNDAY.domain.post.Post;
import com.culture.BAEUNDAY.domain.post.PostService;
import com.culture.BAEUNDAY.domain.reserve.DTO.ReserveRequestDto;
import com.culture.BAEUNDAY.domain.reserve.DTO.ReserveResponseDto;
import com.culture.BAEUNDAY.domain.user.User;
import com.culture.BAEUNDAY.domain.user.UserService;
import com.culture.BAEUNDAY.utils.CursorRequest;
import com.culture.BAEUNDAY.utils.CursorResponse;
import com.culture.BAEUNDAY.utils.PageResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class ReserveService {

    private final ReserveJPARepository reserveJPARepository;
    private final PostService postService;
    private final UserService userService;
    private static final int PAGE_SIZE_PLUS_ONE = 4+1;
    public String reserve(String username, ReserveRequestDto requestDto) {

        User user = userService.findUserByUsernameOrThrow(username);
        Post post = postService.getPostById(requestDto.postId());
        Optional<Reserve> reserve = reserveJPARepository.findByUserAndPost(user,post);

        if (reserve.isPresent()) {
            reserveJPARepository.delete(reserve.get());
            //TODO : 게시글 신청자수 감소 -> 구현 할지말지 정확X
            return ("신청 취소");
        }else {
            Reserve newReserve = Reserve.builder().user(user).post(post).reservationDate(requestDto.reservationDate()).status(Status.PAYMENT).build();
            reserveJPARepository.save(newReserve);
            //TODO : 게시글 신청자수 증가 -> 구현 할지말지 정확X
            return ("신청 완료") ;
        }
    }


    public PageResponse<?, List<ReserveResponseDto>> get(String username, String filter, String cursor, Long cursorId) {

        User user = userService.findUserByUsernameOrThrow(username);
        CursorRequest<Long> request = new CursorRequest<>(PAGE_SIZE_PLUS_ONE, cursor, Long.class, cursorId);
        List<Reserve> reserves ;
        if (filter != null ) {
            Status status = Status.valueOf(filter);
            reserves = reserveJPARepository.findByUserAndFilterWithCursor(user,status,request.cursor,request.request);
        }else {
            reserves =  reserveJPARepository.findByUserWithCursor(user, request.cursor, request.request);
        }
        if ( reserves.isEmpty() ) {
            return new PageResponse<>(new CursorResponse<>(false,0,null,null),null);
        }

        int size = reserves.size();
        boolean hasNext = false;
        if ( size == PAGE_SIZE_PLUS_ONE ) {
            hasNext = true;
            reserves.remove(size - 1);
            size -= 1;
        }

        List<ReserveResponseDto> reserveList= new ArrayList<>();
        for (Reserve reserve : reserves) {
            Post post = postService.getPostById(reserve.getPost().getId());
            reserveList.add(
                    ReserveResponseDto.builder()
                            .id(reserve.getId())
                            .postId(post.getId())
                            .title(post.getTitle())
                            .imgURL(post.getImgURL())
                            .province(post.getProvince())
                            .city(post.getCity())
                            .fee(post.getFee())
                            .status(reserve.getStatus())
                            .startDate(post.getStartDate())
                            .reservationDate(reserve.getReservationDate())
                            .build()
            );
        }
        Reserve lastReserve = reserves.get(size - 1);
        Long nextCursor = lastReserve.getId();
        return new PageResponse<>(new CursorResponse<>(hasNext, size, nextCursor, nextCursor), reserveList);



    }
}
