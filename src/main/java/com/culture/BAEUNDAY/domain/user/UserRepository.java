package com.culture.BAEUNDAY.domain.user;

import com.culture.BAEUNDAY.domain.post.Post;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    boolean existsByName(String name);

    boolean existsByUsername(String username);


    User findByUsername(String username);

    @Query("select u from User u where u.username = :username")
    Optional<User> findByName(@Param("username") String username);

    @Query("select p from Post p where p.user.id = :userId and p.id < :cursor order by p.id desc ")
    List<Post> findByUserIdWithCursor(@Param("userId") Long id, @Param("cursor") Long cursor, Pageable request);
}
