package cn.org.atool.fluent.mybatis.model;

import cn.org.atool.fluent.mybatis.base.IEntity;
import cn.org.atool.fluent.mybatis.base.IRefs;
import cn.org.atool.fluent.mybatis.base.crud.BaseFormSetter;
import cn.org.atool.fluent.mybatis.base.crud.IQuery;
import cn.org.atool.fluent.mybatis.base.mapper.IEntityMapper;

import static cn.org.atool.fluent.mybatis.base.model.SqlOpStr.*;

/**
 * 简单表单查询
 *
 * @param <E>
 * @param <S>
 * @author wudarui
 */
@SuppressWarnings({"unchecked", "rawtypes", "unused"})
public interface IFormQuery<E extends IEntity, S extends BaseFormSetter> extends IQuery<E> {
    /**
     * 对应的实体Entity类型
     *
     * @return 实体类类型
     */
    Class<? extends IEntity> entityClass();

    S op(String op);

    default S eq() {
        return this.op(OP_EQ);
    }

    default S ne() {
        return this.op(OP_NE);
    }

    default S gt() {
        return this.op(OP_GT);
    }

    default S ge() {
        return this.op(OP_GE);
    }

    default S lt() {
        return this.op(OP_LT);
    }

    default S le() {
        return this.op(OP_LE);
    }

    default S like() {
        return this.op(OP_LIKE);
    }

    @Override
    IFormQuery<E, S> distinct();

    @Override
    IFormQuery<E, S> selectAll();

    @Override
    IFormQuery<E, S> selectId();

    @Override
    IFormQuery<E, S> limit(int limit);

    @Override
    IFormQuery<E, S> limit(int start, int limit);

    @Override
    IFormQuery<E, S> last(String lastSql);

    /**
     * 是否存在对应条件数据
     *
     * @return true: exist
     */
    default boolean exists() {
        IEntityMapper mapper = IRefs.mapper(this.entityClass());
        int count = mapper.count(this);
        return count > 0;
    }
}