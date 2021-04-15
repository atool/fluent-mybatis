package cn.org.atool.fluent.mybatis.base;

import cn.org.atool.fluent.mybatis.base.crud.BatchCrudImpl;
import cn.org.atool.fluent.mybatis.base.crud.IBaseQuery;
import cn.org.atool.fluent.mybatis.base.crud.IBaseUpdate;
import cn.org.atool.fluent.mybatis.base.crud.IWrapper;

/**
 * 批量增删改(没有查)操作构造
 *
 * @author wudarui
 */
public interface BatchCrud extends IWrapper {
    /**
     * 构造批量增删改构造器
     *
     * @return
     */
    static BatchCrud batch() {
        return new BatchCrudImpl();
    }

    /**
     * 按顺序添加Insert语句
     *
     * @param entities
     * @return
     */
    BatchCrud addInsert(IEntity... entities);

    /**
     * 按顺序添加update语句
     *
     * @param updates
     * @return
     */
    BatchCrud addUpdate(IBaseUpdate... updates);

    /**
     * 按顺序添加delete语句
     *
     * @param deletes
     * @return
     */
    BatchCrud addDelete(IBaseQuery... deletes);
}