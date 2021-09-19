package cn.org.atool.fluent.mybatis.base.crud;

import cn.org.atool.fluent.mybatis.base.IEntity;
import cn.org.atool.fluent.mybatis.segment.BaseWrapper;
import cn.org.atool.fluent.mybatis.segment.fragment.IFragment;
import cn.org.atool.fluent.mybatis.segment.model.PagedOffset;

import static cn.org.atool.fluent.mybatis.mapper.StrConstant.EMPTY;

/**
 * AbstractUpdateWrapper
 *
 * @param <E>  对应的实体类
 * @param <U>  更新器
 * @param <NQ> 对应的查询器
 * @author darui.wu 2020/6/17 4:24 下午
 */
@SuppressWarnings({"rawtypes", "unchecked"})
public abstract class BaseUpdate<
    E extends IEntity,
    U extends IBaseUpdate<E, U, NQ>,
    NQ extends IBaseQuery<E, NQ>>
    extends BaseWrapper<E, U, NQ>
    implements IBaseUpdate<E, U, NQ> {

    protected BaseUpdate(String table, Class entityClass) {
        super(db -> table, EMPTY, entityClass);
    }

    protected BaseUpdate(IFragment table, String alias, Class entityClass) {
        super(table, alias, entityClass);
    }

    @Override
    public U limit(int limit) {
        this.data.setPaged(new PagedOffset(0, limit));
        return (U) this;
    }

    @Override
    public U last(String lastSql) {
        this.data.last(lastSql);
        return (U) this;
    }


    /**
     * 按条件更新时, 忽略乐观锁
     *
     * @return self
     */
    public U ignoreLockVersion() {
        this.data.setIgnoreLockVersion(true);
        return (U) this;
    }
}