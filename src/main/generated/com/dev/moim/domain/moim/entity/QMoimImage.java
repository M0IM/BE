package com.dev.moim.domain.moim.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QMoimImage is a Querydsl query type for MoimImage
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QMoimImage extends EntityPathBase<MoimImage> {

    private static final long serialVersionUID = 1033142186L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QMoimImage moimImage = new QMoimImage("moimImage");

    public final com.dev.moim.global.common.QBaseEntity _super = new com.dev.moim.global.common.QBaseEntity(this);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final StringPath imageKeyName = createString("imageKeyName");

    public final QMoim moim;

    //inherited
    public final DateTimePath<java.time.LocalDateTime> updatedAt = _super.updatedAt;

    public QMoimImage(String variable) {
        this(MoimImage.class, forVariable(variable), INITS);
    }

    public QMoimImage(Path<? extends MoimImage> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QMoimImage(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QMoimImage(PathMetadata metadata, PathInits inits) {
        this(MoimImage.class, metadata, inits);
    }

    public QMoimImage(Class<? extends MoimImage> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.moim = inits.isInitialized("moim") ? new QMoim(forProperty("moim")) : null;
    }

}

