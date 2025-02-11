package com.culture.BAEUNDAY.domain.user;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QUser is a Querydsl query type for User
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QUser extends EntityPathBase<User> {

    private static final long serialVersionUID = -1114221112L;

    public static final QUser user = new QUser("user");

    public final ListPath<com.culture.BAEUNDAY.domain.comment.Comment, com.culture.BAEUNDAY.domain.comment.QComment> comments = this.<com.culture.BAEUNDAY.domain.comment.Comment, com.culture.BAEUNDAY.domain.comment.QComment>createList("comments", com.culture.BAEUNDAY.domain.comment.Comment.class, com.culture.BAEUNDAY.domain.comment.QComment.class, PathInits.DIRECT2);

    public final StringPath field = createString("field");

    public final ListPath<com.culture.BAEUNDAY.domain.heart.Heart, com.culture.BAEUNDAY.domain.heart.QHeart> hearts = this.<com.culture.BAEUNDAY.domain.heart.Heart, com.culture.BAEUNDAY.domain.heart.QHeart>createList("hearts", com.culture.BAEUNDAY.domain.heart.Heart.class, com.culture.BAEUNDAY.domain.heart.QHeart.class, PathInits.DIRECT2);

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final NumberPath<Integer> manner = createNumber("manner", Integer.class);

    public final StringPath name = createString("name");

    public final StringPath password = createString("password");

    public final StringPath profileImg = createString("profileImg");

    public final ListPath<com.culture.BAEUNDAY.domain.reply.Reply, com.culture.BAEUNDAY.domain.reply.QReply> replies = this.<com.culture.BAEUNDAY.domain.reply.Reply, com.culture.BAEUNDAY.domain.reply.QReply>createList("replies", com.culture.BAEUNDAY.domain.reply.Reply.class, com.culture.BAEUNDAY.domain.reply.QReply.class, PathInits.DIRECT2);

    public final EnumPath<Role> role = createEnum("role", Role.class);

    public final StringPath username = createString("username");

    public QUser(String variable) {
        super(User.class, forVariable(variable));
    }

    public QUser(Path<? extends User> path) {
        super(path.getType(), path.getMetadata());
    }

    public QUser(PathMetadata metadata) {
        super(User.class, metadata);
    }

}

