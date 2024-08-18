package com.dev.moim.domain.moim.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QUserPlan is a Querydsl query type for UserPlan
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QUserPlan extends EntityPathBase<UserPlan> {

    private static final long serialVersionUID = -872114689L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QUserPlan userPlan = new QUserPlan("userPlan");

    public final com.dev.moim.global.common.QBaseEntity _super = new com.dev.moim.global.common.QBaseEntity(this);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final BooleanPath isWriter = createBoolean("isWriter");

    public final QPlan plan;

    //inherited
    public final DateTimePath<java.time.LocalDateTime> updatedAt = _super.updatedAt;

    public final com.dev.moim.domain.account.entity.QUser user;

    public QUserPlan(String variable) {
        this(UserPlan.class, forVariable(variable), INITS);
    }

    public QUserPlan(Path<? extends UserPlan> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QUserPlan(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QUserPlan(PathMetadata metadata, PathInits inits) {
        this(UserPlan.class, metadata, inits);
    }

    public QUserPlan(Class<? extends UserPlan> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.plan = inits.isInitialized("plan") ? new QPlan(forProperty("plan"), inits.get("plan")) : null;
        this.user = inits.isInitialized("user") ? new com.dev.moim.domain.account.entity.QUser(forProperty("user")) : null;
    }

}

