package com.dev.moim.domain.moim.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QPostBlock is a Querydsl query type for PostBlock
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QPostBlock extends EntityPathBase<PostBlock> {

    private static final long serialVersionUID = 384771266L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QPostBlock postBlock = new QPostBlock("postBlock");

    public final com.dev.moim.global.common.QBaseEntity _super = new com.dev.moim.global.common.QBaseEntity(this);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final QPost post;

    //inherited
    public final DateTimePath<java.time.LocalDateTime> updatedAt = _super.updatedAt;

    public final com.dev.moim.domain.account.entity.QUser user;

    public QPostBlock(String variable) {
        this(PostBlock.class, forVariable(variable), INITS);
    }

    public QPostBlock(Path<? extends PostBlock> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QPostBlock(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QPostBlock(PathMetadata metadata, PathInits inits) {
        this(PostBlock.class, metadata, inits);
    }

    public QPostBlock(Class<? extends PostBlock> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.post = inits.isInitialized("post") ? new QPost(forProperty("post"), inits.get("post")) : null;
        this.user = inits.isInitialized("user") ? new com.dev.moim.domain.account.entity.QUser(forProperty("user")) : null;
    }

}

