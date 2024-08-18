package com.dev.moim.domain.moim.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QUserMoim is a Querydsl query type for UserMoim
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QUserMoim extends EntityPathBase<UserMoim> {

    private static final long serialVersionUID = -872200932L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QUserMoim userMoim = new QUserMoim("userMoim");

    public final com.dev.moim.global.common.QBaseEntity _super = new com.dev.moim.global.common.QBaseEntity(this);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final EnumPath<com.dev.moim.domain.moim.entity.enums.JoinStatus> joinStatus = createEnum("joinStatus", com.dev.moim.domain.moim.entity.enums.JoinStatus.class);

    public final QMoim moim;

    public final EnumPath<com.dev.moim.domain.moim.entity.enums.MoimRole> moimRole = createEnum("moimRole", com.dev.moim.domain.moim.entity.enums.MoimRole.class);

    public final EnumPath<com.dev.moim.domain.moim.entity.enums.ProfileStatus> profileStatus = createEnum("profileStatus", com.dev.moim.domain.moim.entity.enums.ProfileStatus.class);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> updatedAt = _super.updatedAt;

    public final com.dev.moim.domain.account.entity.QUser user;

    public final com.dev.moim.domain.account.entity.QUserProfile userProfile;

    public QUserMoim(String variable) {
        this(UserMoim.class, forVariable(variable), INITS);
    }

    public QUserMoim(Path<? extends UserMoim> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QUserMoim(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QUserMoim(PathMetadata metadata, PathInits inits) {
        this(UserMoim.class, metadata, inits);
    }

    public QUserMoim(Class<? extends UserMoim> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.moim = inits.isInitialized("moim") ? new QMoim(forProperty("moim")) : null;
        this.user = inits.isInitialized("user") ? new com.dev.moim.domain.account.entity.QUser(forProperty("user")) : null;
        this.userProfile = inits.isInitialized("userProfile") ? new com.dev.moim.domain.account.entity.QUserProfile(forProperty("userProfile"), inits.get("userProfile")) : null;
    }

}

