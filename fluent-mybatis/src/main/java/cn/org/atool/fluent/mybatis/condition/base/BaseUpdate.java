package cn.org.atool.fluent.mybatis.condition.base;

import cn.org.atool.fluent.mybatis.condition.model.PagedOffset;
import cn.org.atool.fluent.mybatis.interfaces.IEntity;
import cn.org.atool.fluent.mybatis.interfaces.IQuery;
import cn.org.atool.fluent.mybatis.interfaces.IUpdate;

/**
 * AbstractUpdateWrapper
 *
 * @param <E> 对应的实体类
 * @param <U> 更新器
 * @param <Q> 对应的查询器
 * @author darui.wu
 * @date 2020/6/17 4:24 下午
 */
public abstract class BaseUpdate<
    E extends IEntity,
    U extends IUpdate<E, U, Q>,
    Q extends IQuery<E, Q>>
    extends BaseWrapper<E, U, Q>
    implements IUpdate<E, U, Q> {
    private static final long serialVersionUID = 6181348549200073762L;

    protected BaseUpdate(String table, Class entityClass, Class queryClass) {
        super(table, entityClass, queryClass);
    }

    @Override
    public U limit(int limit) {
        this.wrapperData.setPaged(new PagedOffset(0, limit));
        return (U) this;
    }
}