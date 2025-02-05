package com.culture.BAEUNDAY.domain.user;

import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {

    boolean existsByName(String name);

    boolean existsByUsername(String username);

    User findByUsername(String username);
}
