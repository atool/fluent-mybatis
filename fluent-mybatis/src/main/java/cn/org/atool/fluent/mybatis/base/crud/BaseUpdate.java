package cn.org.atool.fluent.mybatis.base.crud;

import cn.org.atool.fluent.mybatis.base.IEntity;
import cn.org.atool.fluent.mybatis.segment.BaseWrapper;
import cn.org.atool.fluent.mybatis.segment.model.PagedOffset;

/**
 * AbstractUpdateWrapper
 *
 * @param <E>  对应的实体类
 * @param <U>  更新器
 * @param <NQ> 对应的查询器
 * @author darui.wu
 * @date 2020/6/17 4:24 下午
 */
public abstract class BaseUpdate<
    E extends IEntity,
    U extends IBaseUpdate<E, U, NQ>,
    NQ extends IBaseQuery<E, NQ>>
    extends BaseWrapper<E, U, NQ>
    implements IBaseUpdate<E, U, NQ> {

    protected BaseUpdate(String table, Class entityClass, Class queryClass) {
        super(table, entityClass, queryClass);
    }

    @Override
    public U limit(int limit) {
        this.wrapperData.setPaged(new PagedOffset(0, limit));
        return (U) this;
    }

    @Override
    public U last(String lastSql) {
        this.wrapperData.last(lastSql);
        return (U) this;
    }
}