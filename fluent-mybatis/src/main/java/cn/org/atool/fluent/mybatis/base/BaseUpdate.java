package cn.org.atool.fluent.mybatis.base;

import cn.org.atool.fluent.mybatis.base.IEntity;
import cn.org.atool.fluent.mybatis.base.IQuery;
import cn.org.atool.fluent.mybatis.base.IUpdate;
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
    U extends IUpdate<E, U, NQ>,
    NQ extends IQuery<E, NQ>>
    extends BaseWrapper<E, U, NQ>
    implements IUpdate<E, U, NQ> {

    protected BaseUpdate(String table, Class entityClass, Class queryClass) {
        super(table, entityClass, queryClass);
    }

    @Override
    public U limit(int limit) {
        this.wrapperData.setPaged(new PagedOffset(0, limit));
        return (U) this;
    }
}