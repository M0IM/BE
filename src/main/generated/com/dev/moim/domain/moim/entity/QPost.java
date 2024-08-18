package com.dev.moim.domain.moim.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QPost is a Querydsl query type for Post
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QPost extends EntityPathBase<Post> {

    private static final long serialVersionUID = -1276113269L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QPost post = new QPost("post");

    public final com.dev.moim.global.common.QBaseEntity _super = new com.dev.moim.global.common.QBaseEntity(this);

    public final ListPath<Comment, QComment> commentList = this.<Comment, QComment>createList("commentList", Comment.class, QComment.class, PathInits.DIRECT2);

    public final StringPath content = createString("content");

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final QMoim moim;

    public final ListPath<PostBlock, QPostBlock> postBlockList = this.<PostBlock, QPostBlock>createList("postBlockList", PostBlock.class, QPostBlock.class, PathInits.DIRECT2);

    public final ListPath<PostImage, QPostImage> postImageList = this.<PostImage, QPostImage>createList("postImageList", PostImage.class, QPostImage.class, PathInits.DIRECT2);

    public final ListPath<PostLike, QPostLike> postLikeList = this.<PostLike, QPostLike>createList("postLikeList", PostLike.class, QPostLike.class, PathInits.DIRECT2);

    public final EnumPath<com.dev.moim.domain.moim.entity.enums.PostType> postType = createEnum("postType", com.dev.moim.domain.moim.entity.enums.PostType.class);

    public final StringPath title = createString("title");

    //inherited
    public final DateTimePath<java.time.LocalDateTime> updatedAt = _super.updatedAt;

    public final QUserMoim userMoim;

    public QPost(String variable) {
        this(Post.class, forVariable(variable), INITS);
    }

    public QPost(Path<? extends Post> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QPost(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QPost(PathMetadata metadata, PathInits inits) {
        this(Post.class, metadata, inits);
    }

    public QPost(Class<? extends Post> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.moim = inits.isInitialized("moim") ? new QMoim(forProperty("moim")) : null;
        this.userMoim = inits.isInitialized("userMoim") ? new QUserMoim(forProperty("userMoim"), inits.get("userMoim")) : null;
    }

}

