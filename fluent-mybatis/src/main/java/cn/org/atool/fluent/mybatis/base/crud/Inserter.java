package cn.org.atool.fluent.mybatis.base.crud;

import cn.org.atool.fluent.common.kits.StrKey;
import cn.org.atool.fluent.mybatis.base.IEntity;
import cn.org.atool.fluent.mybatis.base.entity.IMapping;
import cn.org.atool.fluent.mybatis.base.intf.IOptMapping;
import cn.org.atool.fluent.mybatis.functions.SqlFunction;
import cn.org.atool.fluent.mybatis.utility.RefKit;

import java.util.*;

/**
 * 插入实例操作
 */
public class Inserter implements IOptMapping {
    public static Inserter instance() {
        return new Inserter();
    }

    private final List<IEntity> entities = new ArrayList<>();

    public <E extends IEntity> Inserter insert(E entity) {
        this.entities.add(entity);
        return this;
    }

    public boolean notPk() {
        return this.entities.isEmpty() || this.entities.get(0).findPk() == null;
    }

    public <E extends IEntity> Inserter insert(E[] entities) {
        this.entities.addAll(Arrays.asList(entities));
        return this;
    }

    @SuppressWarnings("unchecked")
    public <E extends IEntity> Inserter insert(E entity1, E entity2, E... entities) {
        this.entities.add(entity1);
        this.entities.add(entity2);
        this.entities.addAll(Arrays.asList(entities));
        return this;
    }

    public <E extends IEntity> Inserter insert(Collection<E> entities) {
        this.entities.addAll(entities);
        return this;
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    public <E extends IEntity> Collection<E> entities() {
        return (List) this.entities;
    }

    /**
     * 获取mybatis占位符sql语句和参数上下文
     *
     * @param insertFunction IInsertFunction方法
     * @return sql语句 + 上下文
     */
    @SuppressWarnings("rawtypes")
    public StrKey sql(SqlFunction.IInsertFunction insertFunction) {
        return SqlFunction.sql(this, insertFunction);
    }

    @Override
    public Optional<IMapping> mapping() {
        if (entities.isEmpty()) {
            throw new RuntimeException("insert objects can't be null!");
        } else {
            IMapping mapping = RefKit.byEntity(entities.get(0).entityClass());
            return Optional.of(mapping);
        }
    }
}
