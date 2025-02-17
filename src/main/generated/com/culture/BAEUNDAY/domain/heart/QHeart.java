package com.culture.BAEUNDAY.domain.heart;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QHeart is a Querydsl query type for Heart
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QHeart extends EntityPathBase<Heart> {

    private static final long serialVersionUID = -1498541160L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QHeart heart = new QHeart("heart");

    public final DateTimePath<java.time.LocalDateTime> createdDate = createDateTime("createdDate", java.time.LocalDateTime.class);

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final com.culture.BAEUNDAY.domain.post.QPost post;

    public final com.culture.BAEUNDAY.domain.user.QUser user;

    public QHeart(String variable) {
        this(Heart.class, forVariable(variable), INITS);
    }

    public QHeart(Path<? extends Heart> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QHeart(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QHeart(PathMetadata metadata, PathInits inits) {
        this(Heart.class, metadata, inits);
    }

    public QHeart(Class<? extends Heart> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.post = inits.isInitialized("post") ? new com.culture.BAEUNDAY.domain.post.QPost(forProperty("post"), inits.get("post")) : null;
        this.user = inits.isInitialized("user") ? new com.culture.BAEUNDAY.domain.user.QUser(forProperty("user")) : null;
    }

}

