package com.dev.moim.domain.moim.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QMoim is a Querydsl query type for Moim
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QMoim extends EntityPathBase<Moim> {

    private static final long serialVersionUID = -1276202959L;

    public static final QMoim moim = new QMoim("moim");

    public final com.dev.moim.global.common.QBaseEntity _super = new com.dev.moim.global.common.QBaseEntity(this);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final StringPath introduceVideoKeyName = createString("introduceVideoKeyName");

    public final StringPath introduceVideoTitle = createString("introduceVideoTitle");

    public final StringPath introduction = createString("introduction");

    public final StringPath location = createString("location");

    public final EnumPath<com.dev.moim.domain.moim.entity.enums.MoimCategory> moimCategory = createEnum("moimCategory", com.dev.moim.domain.moim.entity.enums.MoimCategory.class);

    public final ListPath<MoimImage, QMoimImage> moimImages = this.<MoimImage, QMoimImage>createList("moimImages", MoimImage.class, QMoimImage.class, PathInits.DIRECT2);

    public final StringPath name = createString("name");

    //inherited
    public final DateTimePath<java.time.LocalDateTime> updatedAt = _super.updatedAt;

    public final ListPath<UserMoim, QUserMoim> userMoimList = this.<UserMoim, QUserMoim>createList("userMoimList", UserMoim.class, QUserMoim.class, PathInits.DIRECT2);

    public QMoim(String variable) {
        super(Moim.class, forVariable(variable));
    }

    public QMoim(Path<? extends Moim> path) {
        super(path.getType(), path.getMetadata());
    }

    public QMoim(PathMetadata metadata) {
        super(Moim.class, metadata);
    }

}

