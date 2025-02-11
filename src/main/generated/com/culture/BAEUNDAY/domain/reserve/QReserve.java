package com.culture.BAEUNDAY.domain.reserve;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QReserve is a Querydsl query type for Reserve
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QReserve extends EntityPathBase<Reserve> {

    private static final long serialVersionUID = 1832987524L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QReserve reserve = new QReserve("reserve");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final EnumPath<MyStatus> myStatus = createEnum("myStatus", MyStatus.class);

    public final com.culture.BAEUNDAY.domain.post.QPost post;

    public final DateTimePath<java.time.LocalDateTime> reservationDate = createDateTime("reservationDate", java.time.LocalDateTime.class);

    public final EnumPath<Status> status = createEnum("status", Status.class);

    public final com.culture.BAEUNDAY.domain.user.QUser user;

    public QReserve(String variable) {
        this(Reserve.class, forVariable(variable), INITS);
    }

    public QReserve(Path<? extends Reserve> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QReserve(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QReserve(PathMetadata metadata, PathInits inits) {
        this(Reserve.class, metadata, inits);
    }

    public QReserve(Class<? extends Reserve> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.post = inits.isInitialized("post") ? new com.culture.BAEUNDAY.domain.post.QPost(forProperty("post"), inits.get("post")) : null;
        this.user = inits.isInitialized("user") ? new com.culture.BAEUNDAY.domain.user.QUser(forProperty("user")) : null;
    }

}

