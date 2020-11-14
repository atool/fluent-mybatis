package cn.org.atool.fluent.mybatis.model;

import cn.org.atool.fluent.mybatis.base.*;
import cn.org.atool.fluent.mybatis.base.FormSetter;

import static cn.org.atool.fluent.mybatis.base.model.SqlOpStr.*;

/**
 * 简单表单查询
 *
 * @param <E>
 * @param <C>
 * @author wudarui
 */
public interface IFormQuery<E extends IEntity, C extends FormSetter<IFormQuery<E, C>>>
    extends IQuery<E, IFormQuery<E, C>> {
    /**
     * 对应的实体Entity类型
     *
     * @return
     */
    Class<? extends IEntity> entityClass();

    C op(String op);

    default C eq() {
        return this.op(OP_EQ);
    }

    default C ne() {
        return this.op(OP_NE);
    }

    default C gt() {
        return this.op(OP_GT);
    }

    default C ge() {
        return this.op(OP_GE);
    }

    default C lt() {
        return this.op(OP_LT);
    }

    default C le() {
        return this.op(OP_LE);
    }

    default C like() {
        return this.op(OP_LIKE);
    }

    /**
     * 是否存在对应条件数据
     *
     * @return
     */
    default boolean exists() {
        IEntityMapper mapper = EntityRefs.instance().findMapper(this.entityClass());
        int count = mapper.count(this);
        return count > 0;
    }

    /**
     * 分页查询数据
     *
     * @return
     */
    default <P extends IPagedList<E>> P paged() {
        return (P) EntityRefs.paged(this);
    }
}