package com.dev.moim.domain.moim.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QCommentLike is a Querydsl query type for CommentLike
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QCommentLike extends EntityPathBase<CommentLike> {

    private static final long serialVersionUID = 1987634763L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QCommentLike commentLike = new QCommentLike("commentLike");

    public final com.dev.moim.global.common.QBaseEntity _super = new com.dev.moim.global.common.QBaseEntity(this);

    public final QComment comment;

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    public final NumberPath<Long> id = createNumber("id", Long.class);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> updatedAt = _super.updatedAt;

    public final com.dev.moim.domain.account.entity.QUser user;

    public QCommentLike(String variable) {
        this(CommentLike.class, forVariable(variable), INITS);
    }

    public QCommentLike(Path<? extends CommentLike> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QCommentLike(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QCommentLike(PathMetadata metadata, PathInits inits) {
        this(CommentLike.class, metadata, inits);
    }

    public QCommentLike(Class<? extends CommentLike> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.comment = inits.isInitialized("comment") ? new QComment(forProperty("comment"), inits.get("comment")) : null;
        this.user = inits.isInitialized("user") ? new com.dev.moim.domain.account.entity.QUser(forProperty("user")) : null;
    }

}

