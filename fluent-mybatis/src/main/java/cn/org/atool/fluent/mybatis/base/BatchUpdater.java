package cn.org.atool.fluent.mybatis.base;

import cn.org.atool.fluent.mybatis.base.crud.BatchUpdaterImpl;
import cn.org.atool.fluent.mybatis.base.crud.IBaseQuery;
import cn.org.atool.fluent.mybatis.base.crud.IBaseUpdate;
import cn.org.atool.fluent.mybatis.base.crud.IWrapper;

/**
 * 批量更新
 */
public interface BatchUpdater extends IWrapper {
    static BatchUpdater newBatch() {
        return new BatchUpdaterImpl();
    }

    BatchUpdater addUpdate(IBaseUpdate... updates);

    BatchUpdater addDelete(IBaseQuery... queries);

    BatchUpdater addInsert(IEntity... entities);
}