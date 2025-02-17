package com.culture.BAEUNDAY.domain.reserve.DTO;

import com.culture.BAEUNDAY.domain.post.Province;
import com.culture.BAEUNDAY.domain.reserve.MyStatus;
import com.culture.BAEUNDAY.domain.reserve.Status;
import lombok.Builder;

import java.time.LocalDateTime;
@Builder
public record ReserveResponseDto(
        Long id,
        Long postId,
        String title,
        String imgURL,
        Province province,
        String city,
        Integer fee,
        Status status,
        MyStatus myStatus,
        LocalDateTime startDate,
        LocalDateTime reservationDate){

}
