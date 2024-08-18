package com.dev.moim.domain.moim.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QIndividualPlan is a Querydsl query type for IndividualPlan
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QIndividualPlan extends EntityPathBase<IndividualPlan> {

    private static final long serialVersionUID = -1010042483L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QIndividualPlan individualPlan = new QIndividualPlan("individualPlan");

    public final com.dev.moim.global.common.QBaseEntity _super = new com.dev.moim.global.common.QBaseEntity(this);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    public final DateTimePath<java.time.LocalDateTime> date = createDateTime("date", java.time.LocalDateTime.class);

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final StringPath title = createString("title");

    //inherited
    public final DateTimePath<java.time.LocalDateTime> updatedAt = _super.updatedAt;

    public final com.dev.moim.domain.account.entity.QUser user;

    public QIndividualPlan(String variable) {
        this(IndividualPlan.class, forVariable(variable), INITS);
    }

    public QIndividualPlan(Path<? extends IndividualPlan> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QIndividualPlan(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QIndividualPlan(PathMetadata metadata, PathInits inits) {
        this(IndividualPlan.class, metadata, inits);
    }

    public QIndividualPlan(Class<? extends IndividualPlan> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.user = inits.isInitialized("user") ? new com.dev.moim.domain.account.entity.QUser(forProperty("user")) : null;
    }

}

