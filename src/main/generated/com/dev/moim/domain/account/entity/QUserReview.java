package com.dev.moim.domain.account.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QUserReview is a Querydsl query type for UserReview
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QUserReview extends EntityPathBase<UserReview> {

    private static final long serialVersionUID = -45624507L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QUserReview userReview = new QUserReview("userReview");

    public final com.dev.moim.global.common.QBaseEntity _super = new com.dev.moim.global.common.QBaseEntity(this);

    public final StringPath content = createString("content");

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final NumberPath<Double> rating = createNumber("rating", Double.class);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> updatedAt = _super.updatedAt;

    public final QUser user;

    public final NumberPath<Long> writerId = createNumber("writerId", Long.class);

    public QUserReview(String variable) {
        this(UserReview.class, forVariable(variable), INITS);
    }

    public QUserReview(Path<? extends UserReview> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QUserReview(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QUserReview(PathMetadata metadata, PathInits inits) {
        this(UserReview.class, metadata, inits);
    }

    public QUserReview(Class<? extends UserReview> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.user = inits.isInitialized("user") ? new QUser(forProperty("user")) : null;
    }

}

