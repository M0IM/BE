package com.dev.moim.domain.moim.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QExitReason is a Querydsl query type for ExitReason
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QExitReason extends EntityPathBase<ExitReason> {

    private static final long serialVersionUID = -1974566227L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QExitReason exitReason = new QExitReason("exitReason");

    public final com.dev.moim.global.common.QBaseEntity _super = new com.dev.moim.global.common.QBaseEntity(this);

    public final StringPath content = createString("content");

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final QMoim moim;

    //inherited
    public final DateTimePath<java.time.LocalDateTime> updatedAt = _super.updatedAt;

    public final com.dev.moim.domain.account.entity.QUser user;

    public QExitReason(String variable) {
        this(ExitReason.class, forVariable(variable), INITS);
    }

    public QExitReason(Path<? extends ExitReason> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QExitReason(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QExitReason(PathMetadata metadata, PathInits inits) {
        this(ExitReason.class, metadata, inits);
    }

    public QExitReason(Class<? extends ExitReason> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.moim = inits.isInitialized("moim") ? new QMoim(forProperty("moim")) : null;
        this.user = inits.isInitialized("user") ? new com.dev.moim.domain.account.entity.QUser(forProperty("user")) : null;
    }

}

