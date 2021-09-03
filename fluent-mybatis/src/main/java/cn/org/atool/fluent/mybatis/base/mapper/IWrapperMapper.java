package cn.org.atool.fluent.mybatis.base.mapper;

import cn.org.atool.fluent.mybatis.annotation.FluentMybatis;
import cn.org.atool.fluent.mybatis.base.IEntity;
import cn.org.atool.fluent.mybatis.base.crud.BaseDefaults;
import cn.org.atool.fluent.mybatis.base.crud.IDefaultSetter;
import cn.org.atool.fluent.mybatis.base.crud.IQuery;
import cn.org.atool.fluent.mybatis.base.crud.IUpdate;
import cn.org.atool.fluent.mybatis.base.entity.IMapping;
import cn.org.atool.fluent.mybatis.base.model.FieldMapping;
import cn.org.atool.fluent.mybatis.base.model.FieldType;

/**
 * 以下方法在EntityMapper中实现接口default方法
 */
@SuppressWarnings("unchecked")
public interface IWrapperMapper<E extends IEntity, Q extends IQuery<E>, U extends IUpdate<E>> {
    /**
     * 返回对应的默认构造器
     *
     * @return
     */
    IMapping mapping();

    /**
     * 构造设置了默认条件的Query
     * 默认条件设置{@link FluentMybatis#defaults()}, 具体定义继承 {@link IDefaultSetter#setQueryDefault(IQuery)}
     *
     * @return ignore
     */
    default Q defaultQuery() {
        return (Q) ((BaseDefaults) mapping()).defaultQuery();
    }

    /**
     * 构造设置了默认条件的Updater
     * 默认条件设置{@link FluentMybatis#defaults()}, 具体定义继承 {@link IDefaultSetter#setUpdateDefault(IUpdate)}
     *
     * @return ignore
     */
    default U defaultUpdater() {
        return (U) ((BaseDefaults) mapping()).defaultUpdater();
    }

    /**
     * 构造空查询条件
     *
     * @return ignore
     */
    default Q query() {
        return ((BaseDefaults) mapping()).query();
    }

    /**
     * 构造空更新条件
     *
     * @return ignore
     */
    default U updater() {
        return ((BaseDefaults) mapping()).updater();
    }

    /**
     * 主键字段名称
     *
     * @return ignore
     */
    default FieldMapping primaryField() {
        return this.mapping().findField(FieldType.PRIMARY_ID)
            .orElseThrow(() -> new RuntimeException("primary key not found."));
    }

    /**
     * 对应的entity class类
     *
     * @return ignore
     */
    default Class<E> entityClass() {
        return (Class<E>) mapping().entityClass();
    }
}