package cn.org.atool.fluent.mybatis.base.impl;

import cn.org.atool.fluent.mybatis.base.IEntity;
import cn.org.atool.fluent.mybatis.base.IRich;

import java.util.List;

import static cn.org.atool.fluent.mybatis.mapper.FluentConst.*;

/**
 * 只需要Entity属性就可以执行的数据操作方法
 *
 * @author darui.wu
 */
public interface IBaseRich extends IRich, IEntity {
    /**
     * 持久化entity到数据库, 调用 EntityMapper.insert(Entity)方法
     *
     * @param <E>
     * @return
     */
    default <E extends IEntity> E save() {
        this.invoke(false, Rich_Entity_Save);
        return (E) this;
    }

    /**
     * 按entity的主键更新entity非空字段, 调用 EntityMapper.updateById(id)方法
     *
     * @param <E>
     * @return
     */
    default <E extends IEntity> E updateById() {
        if (this.findPk() == null) {
            throw new RuntimeException("the primary of entity can't be null.");
        }
        this.invoke(false, Rich_Entity_UpdateById);
        return (E) this;
    }

    /**
     * 根据id查找entity, 调用 EntityMapper.findById(id)方法
     *
     * @param <E>
     * @return
     */
    default <E extends IEntity> E findById() {
        if (this.findPk() == null) {
            throw new RuntimeException("the primary of entity can't be null.");
        }
        IEntity entity = this.invoke(false, Rich_Entity_FindById);
        return (E) entity;
    }

    /**
     * 物理删除entity, 调用 EntityMapper.deleteById(id)方法
     */
    default void deleteById() {
        if (this.findPk() == null) {
            throw new RuntimeException("the primary of entity can't be null.");
        }
        this.invoke(false, Rich_Entity_DeleteById);
    }

    /**
     * entity非空字段作为条件查询列表, 调用 EntityMapper.listByMap(map)方法
     *
     * @param <E>
     * @return
     */
    default <E extends IEntity> List<E> listByNotNull() {
        List list = this.invoke(false, RichEntity_ListByNotNull);
        return list;
    }
}