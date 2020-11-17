package cn.org.atool.fluent.mybatis.model;

import cn.org.atool.fluent.mybatis.base.IEntity;
import cn.org.atool.fluent.mybatis.base.IRefs;
import cn.org.atool.fluent.mybatis.base.crud.FormSetter;
import cn.org.atool.fluent.mybatis.base.crud.IQuery;
import cn.org.atool.fluent.mybatis.base.mapper.IEntityMapper;
import cn.org.atool.fluent.mybatis.utility.FormHelper;

import static cn.org.atool.fluent.mybatis.base.model.SqlOpStr.*;

/**
 * 简单表单查询
 *
 * @param <S>
 * @author wudarui
 */
public interface IFormQuery<S extends FormSetter<S>> extends IQuery<IEntity, IFormQuery<S>> {
    /**
     * 对应的实体Entity类型
     *
     * @return
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
    IFormQuery<S> distinct();

    @Override
    IFormQuery<S> selectAll();

    @Override
    IFormQuery<S> selectId();

    @Override
    IFormQuery<S> limit(int limit);

    @Override
    IFormQuery<S> limit(int start, int limit);

    @Override
    IFormQuery<S> last(String lastSql);

    /**
     * 是否存在对应条件数据
     *
     * @return
     */
    default boolean exists() {
        IEntityMapper mapper = IRefs.instance().findMapper(this.entityClass());
        int count = mapper.count(this);
        return count > 0;
    }

    /**
     * 分页查询数据
     *
     * @return
     */
    default IPagedList paged() {
        return FormHelper.paged(this);
    }
}