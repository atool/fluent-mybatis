package cn.org.atool.fluent.mybatis.base.mapper;

import cn.org.atool.fluent.mybatis.annotation.FluentMybatis;
import cn.org.atool.fluent.mybatis.base.IEntity;
import cn.org.atool.fluent.mybatis.base.crud.IDefaultSetter;
import cn.org.atool.fluent.mybatis.base.crud.IQuery;
import cn.org.atool.fluent.mybatis.base.crud.IUpdate;
import cn.org.atool.fluent.mybatis.base.model.FieldMapping;

/**
 * 以下方法在EntityMapper中实现接口default方法
 *
 * @param <E>
 */
public interface IWrapperMapper<E extends IEntity> {
    /**
     * 构造设置了默认条件的Query
     * 默认条件设置{@link FluentMybatis#defaults()}, 具体定义继承 {@link IDefaultSetter#setUpdateDefault(IUpdate)}
     *
     * @return
     */
    <Q extends IQuery<E, Q>> Q defaultQuery();

    /**
     * 构造设置了默认条件的Updater
     * 默认条件设置{@link FluentMybatis#defaults()}, 具体定义继承 {@link IDefaultSetter#setUpdateDefault(IUpdate)}
     *
     * @return
     */
    <U extends IUpdate<E, U, ?>> U defaultUpdater();

    /**
     * 构造空查询条件
     *
     * @return
     */
    <Q extends IQuery<E, Q>> Q query();

    /**
     * 构造空更新条件
     *
     * @return
     */
    <U extends IUpdate<E, U, ?>> U updater();

    /**
     * 主键字段名称
     *
     * @return
     */
    FieldMapping primaryField();

    /**
     * 对应的entity class类
     *
     * @return
     */
    Class<E> entityClass();
}