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
import org.springframework.transaction.annotation.Transactional;

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

    @Transactional
    public String reserve(String username, ReserveRequestDto requestDto) {

        User participant = userService.findUserByUsernameOrThrow(username);
        Post post = postService.getPostById(requestDto.postId());
        User host = userService.findUserByUsernameOrThrow(post.getUser().getUsername());
        Optional<Reserve> reserve = reserveJPARepository.findByUserAndPost(participant,post);

        if (reserve.isPresent()) {
            reserveJPARepository.delete(reserve.get());
            post.removeParticipant(reserve.get());
            return ("신청 취소");
        }else {
            if (post.getNumsOfParticipant() >= post.getMaxP() ){
                throw new IllegalArgumentException("수강인원이 다 모집되어 신청할 수 없습니다.");
            }
            if (host.equals(participant)){
                throw new IllegalArgumentException("본인이 작성한 프로그램은 신청할 수 없습니다.");
            }
            Reserve newReserve = Reserve.builder()
                    .user(participant)
                    .post(post)
                    .reservationDate(requestDto.reservationDate())
                    .status(Status.PAYMENT)
                    .myStatus(MyStatus.NOT_OPEN).build();
            reserveJPARepository.save(newReserve);
            post.addParticipant(newReserve);
            return ("신청 완료") ;
        }
    }

    @Transactional
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
                            .myStatus(reserve.getMyStatus())
                            .startDate(post.getStartDateTime())
                            .reservationDate(reserve.getReservationDate())
                            .build()
            );
        }
        Reserve lastReserve = reserves.get(size - 1);
        Long nextCursor = lastReserve.getId();
        return new PageResponse<>(new CursorResponse<>(hasNext, size, nextCursor, nextCursor), reserveList);



    }
}
