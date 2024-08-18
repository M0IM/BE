package com.dev.moim.domain.account.entity;

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

    private static final long serialVersionUID = 1711476109L;

    public static final QUser user = new QUser("user");

    public final com.dev.moim.global.common.QBaseEntity _super = new com.dev.moim.global.common.QBaseEntity(this);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    public final StringPath deviceId = createString("deviceId");

    public final StringPath email = createString("email");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final DateTimePath<java.time.LocalDateTime> inactive_date = createDateTime("inactive_date", java.time.LocalDateTime.class);

    public final BooleanPath isEventAlarm = createBoolean("isEventAlarm");

    public final BooleanPath isPushAlarm = createBoolean("isPushAlarm");

    public final StringPath password = createString("password");

    public final EnumPath<com.dev.moim.domain.account.entity.enums.Provider> provider = createEnum("provider", com.dev.moim.domain.account.entity.enums.Provider.class);

    public final StringPath providerId = createString("providerId");

    public final NumberPath<Double> rating = createNumber("rating", Double.class);

    public final EnumPath<com.dev.moim.domain.account.entity.enums.UserStatus> status = createEnum("status", com.dev.moim.domain.account.entity.enums.UserStatus.class);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> updatedAt = _super.updatedAt;

    public final ListPath<UserProfile, QUserProfile> userProfileList = this.<UserProfile, QUserProfile>createList("userProfileList", UserProfile.class, QUserProfile.class, PathInits.DIRECT2);

    public final EnumPath<com.dev.moim.domain.account.entity.enums.UserRank> userRank = createEnum("userRank", com.dev.moim.domain.account.entity.enums.UserRank.class);

    public final EnumPath<com.dev.moim.domain.account.entity.enums.UserRole> userRole = createEnum("userRole", com.dev.moim.domain.account.entity.enums.UserRole.class);

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

