package com.culture.BAEUNDAY.domain.user.repository;

import com.culture.BAEUNDAY.domain.user.Refresh;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RefreshRepository extends JpaRepository<Refresh, Long> {

    Boolean existsByRefresh(String refresh);

    @Transactional
    void deleteByRefresh(String refresh);

}
