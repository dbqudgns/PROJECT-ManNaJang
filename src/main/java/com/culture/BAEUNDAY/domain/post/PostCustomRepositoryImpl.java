package com.culture.BAEUNDAY.domain.post;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.JPQLQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public class PostCustomRepositoryImpl extends QuerydslRepositorySupport implements PostCustomRepository {

    @Autowired
    private JPAQueryFactory jpaQueryFactory;
    private QPost post = QPost.post;
    public PostCustomRepositoryImpl() {
        super(Post.class);
    }

    @Override
    public List<Post> findAllById(Long cursor, Long cursorId, Status status, FeeRange feeRange, Pageable pageable) {
        JPQLQuery<Post> query = jpaQueryFactory
                .selectFrom(post)
                .where(
                        eqStatus(status),
                        eqFee(feeRange),
                        eqCursorForId(cursor)
                )
                .orderBy(post.id.desc())
                .limit(pageable.getPageSize());
        return query.fetch();
    }
    @Override
    public List<Post> findAllByHeart(Integer cursor, Long cursorId, Status status, FeeRange feeRange, Pageable pageable) {

        JPQLQuery<Post> query = jpaQueryFactory
                .selectFrom(post)
                .where(
                        eqCursorForHeart(cursor).and(eqStatus(status)).and(eqFee(feeRange))
                        .or(eqCursorIdForHeart(cursor,cursorId)).and(eqStatus(status)).and(eqFee(feeRange))
                )
                .orderBy(post.numsOfHeart.desc(),post.id.desc())
                .limit(pageable.getPageSize());
        return query.fetch();
    }
    @Override
    public List<Post> findAllByDateTime(LocalDateTime cursor, Long cursorId, Status status, FeeRange feeRange, Pageable pageable) {
        JPQLQuery<Post> query = jpaQueryFactory
                .selectFrom(post)
                .where(
                        eqCursorForRecent(cursor).and(eqStatus(status)).and(eqFee(feeRange))
                        .or(eqCursorIdForRecent(cursor,cursorId)).and(eqStatus(status)).and(eqFee(feeRange))
                )
                .orderBy(post.deadline.desc(),post.id.desc())
                .limit(pageable.getPageSize());
        return query.fetch();
    }

    private BooleanExpression eqStatus(Status status){
        if ( status == null ) {return null;}
        return post.status.eq(status);
    }
    private BooleanExpression eqFee(FeeRange feeRange){
        if ( feeRange == null  ) {return null;}
        return post.feeRange.eq(feeRange);
    }

    private BooleanExpression eqCursorForId(Long cursor){
        if ( cursor == null ) { return null;}
        return post.id.lt(cursor);
    }
    private BooleanExpression eqCursorForHeart(Integer cursor){
        if ( cursor == null ) { return null;}
        return post.numsOfHeart.lt(cursor);
    }
    private BooleanExpression eqCursorIdForHeart(Integer cursor, Long cursorId){
        if ( cursor == null ) { return null;}
        return post.numsOfHeart.eq(cursor).and(post.id.lt(cursorId));
    }
    private BooleanExpression eqCursorForRecent(LocalDateTime cursor){
        if ( cursor == null ) { return null;}
        return post.deadline.lt(cursor);
    }
    private BooleanExpression eqCursorIdForRecent(LocalDateTime cursor, Long cursorId){
        if ( cursor == null ) { return null;}
        return post.deadline.eq(cursor).and(post.id.lt(cursorId));
    }


}
