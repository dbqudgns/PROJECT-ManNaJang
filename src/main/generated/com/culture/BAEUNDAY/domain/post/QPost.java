package com.culture.BAEUNDAY.domain.post;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QPost is a Querydsl query type for Post
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QPost extends EntityPathBase<Post> {

    private static final long serialVersionUID = 549065448L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QPost post = new QPost("post");

    public final StringPath address = createString("address");

    public final StringPath city = createString("city");

    public final StringPath content = createString("content");

    public final DateTimePath<java.time.LocalDateTime> createdDate = createDateTime("createdDate", java.time.LocalDateTime.class);

    public final DateTimePath<java.time.LocalDateTime> deadline = createDateTime("deadline", java.time.LocalDateTime.class);

    public final DateTimePath<java.time.LocalDateTime> endDateTime = createDateTime("endDateTime", java.time.LocalDateTime.class);

    public final NumberPath<Integer> fee = createNumber("fee", Integer.class);

    public final EnumPath<FeeRange> feeRange = createEnum("feeRange", FeeRange.class);

    public final ListPath<com.culture.BAEUNDAY.domain.heart.Heart, com.culture.BAEUNDAY.domain.heart.QHeart> hearts = this.<com.culture.BAEUNDAY.domain.heart.Heart, com.culture.BAEUNDAY.domain.heart.QHeart>createList("hearts", com.culture.BAEUNDAY.domain.heart.Heart.class, com.culture.BAEUNDAY.domain.heart.QHeart.class, PathInits.DIRECT2);

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final StringPath imgURL = createString("imgURL");

    public final NumberPath<Integer> maxP = createNumber("maxP", Integer.class);

    public final NumberPath<Integer> minP = createNumber("minP", Integer.class);

    public final NumberPath<Integer> numsOfHeart = createNumber("numsOfHeart", Integer.class);

    public final NumberPath<Integer> numsOfParticipant = createNumber("numsOfParticipant", Integer.class);

    public final EnumPath<Province> province = createEnum("province", Province.class);

    public final ListPath<com.culture.BAEUNDAY.domain.reserve.Reserve, com.culture.BAEUNDAY.domain.reserve.QReserve> reserves = this.<com.culture.BAEUNDAY.domain.reserve.Reserve, com.culture.BAEUNDAY.domain.reserve.QReserve>createList("reserves", com.culture.BAEUNDAY.domain.reserve.Reserve.class, com.culture.BAEUNDAY.domain.reserve.QReserve.class, PathInits.DIRECT2);

    public final DateTimePath<java.time.LocalDateTime> startDateTime = createDateTime("startDateTime", java.time.LocalDateTime.class);

    public final EnumPath<Status> status = createEnum("status", Status.class);

    public final StringPath title = createString("title");

    public final com.culture.BAEUNDAY.domain.user.QUser user;

    public QPost(String variable) {
        this(Post.class, forVariable(variable), INITS);
    }

    public QPost(Path<? extends Post> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QPost(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QPost(PathMetadata metadata, PathInits inits) {
        this(Post.class, metadata, inits);
    }

    public QPost(Class<? extends Post> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.user = inits.isInitialized("user") ? new com.culture.BAEUNDAY.domain.user.QUser(forProperty("user")) : null;
    }

}

