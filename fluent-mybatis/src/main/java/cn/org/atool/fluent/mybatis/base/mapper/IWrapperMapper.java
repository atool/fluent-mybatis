package cn.org.atool.fluent.mybatis.base.mapper;

import cn.org.atool.fluent.mybatis.annotation.FluentMybatis;
import cn.org.atool.fluent.mybatis.base.IEntity;
import cn.org.atool.fluent.mybatis.base.crud.IDefaultSetter;
import cn.org.atool.fluent.mybatis.base.crud.IQuery;
import cn.org.atool.fluent.mybatis.base.crud.IUpdate;
import cn.org.atool.fluent.mybatis.base.entity.IMapping;
import cn.org.atool.fluent.mybatis.base.model.FieldMapping;
import cn.org.atool.fluent.mybatis.base.model.UniqueType;
import cn.org.atool.fluent.mybatis.mapper.PrinterMapper;

import java.util.List;
import java.util.function.Consumer;

/**
 * 以下方法在EntityMapper中实现接口default方法
 *
 * @author wudarui
 */
@SuppressWarnings({"unchecked", "rawtypes"})
public interface IWrapperMapper<E extends IEntity, Q extends IQuery<E>, U extends IUpdate<E>>
    extends IEntityMapper<E>, IRichMapper<E> {
    /**
     * 不实际执行sql语句, 仅仅返回构造好的mybatis SQL语句
     *
     * @param simulators 模拟执行数据操作, 例: m -> m.listEntity(query)
     * @param <M>        IEntityMapper
     * @return sql列表
     */
    default List<String> print(Consumer<PrinterMapper>... simulators) {
        return this.print(0, simulators);
    }

    /**
     * 不实际执行sql语句, 仅仅返回构造好的mybatis SQL语句
     *
     * @param mode       0: '?'占位符模式; 1: 变量替换模式; 2: mybatis变量占位模式
     * @param simulators 模拟执行数据操作, 例: m -> m.listEntity(query)
     * @param <M>        IEntityMapper
     * @return sql列表
     */
    default List<String> print(int mode, Consumer<PrinterMapper>... simulators) {
        PrinterMapper handler = new PrinterMapper(mode, this);
        for (Consumer<PrinterMapper> simulator : simulators) {
            simulator.accept(handler);
        }
        return handler.getSql();
    }

    /**
     * 返回对应的默认构造器
     *
     * @return IMapping
     */
    IMapping mapping();

    /**
     * 构造空查询条件
     * 默认条件设置{@link FluentMybatis#defaults()}, 具体定义继承 {@link IDefaultSetter#setQueryDefault(IQuery)}
     *
     * @return ignore
     */
    default Q query() {
        return mapping().query();
    }

    default Q emptyQuery() {
        return mapping().emptyQuery();
    }

    /**
     * 构造设置了默认条件的Updater
     * 默认条件设置{@link FluentMybatis#defaults()}, 具体定义继承 {@link IDefaultSetter#setUpdateDefault(IUpdate)}
     *
     * @return ignore
     */
    default U updater() {
        return mapping().updater();
    }

    /**
     * 构造空更新条件
     *
     * @return ignore
     */
    default U emptyUpdater() {
        return mapping().emptyUpdater();
    }

    /**
     * 主键字段名称
     *
     * @return ignore
     */
    default FieldMapping primaryField() {
        return this.mapping().findField(UniqueType.PRIMARY_ID)
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