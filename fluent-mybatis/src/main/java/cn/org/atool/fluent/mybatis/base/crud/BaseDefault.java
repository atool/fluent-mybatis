package cn.org.atool.fluent.mybatis.base.crud;

import cn.org.atool.fluent.mybatis.base.IEntity;
import cn.org.atool.fluent.mybatis.functions.TableDynamic;
import cn.org.atool.fluent.mybatis.metadata.DbType;
import cn.org.atool.fluent.mybatis.segment.model.Parameters;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.util.function.Supplier;

import static cn.org.atool.fluent.mybatis.If.notBlank;

/**
 * all entity default setter
 *
 * @param <E> entity
 * @param <Q> query
 * @param <U> update
 * @param <D> defaults
 * @author darui.wu
 */
@SuppressWarnings({"unchecked", "UnusedReturnValue"})
@Getter
@Setter
@Accessors(chain = true)
public abstract class BaseDefault<E extends IEntity, Q extends IQuery<E>, U extends IUpdate<E>, D extends BaseDefault<E, Q, U, D>>
    implements IDefaultSetter, IDefaultGetter {

    private TableDynamic dynamic;

    private String tableName;

    private String schema;

    private DbType dbType;

    protected BaseDefault(String tableName, String schema, DbType dbType) {
        this.tableName = tableName;
        this.schema = schema;
        this.dbType = dbType;
    }

    @Override
    public void setEntityByDefault(IEntity entity) {
        this.setInsertDefault(entity);
    }

    @Override
    public Q defaultQuery() {
        Q query = this.query();
        this.setQueryDefault(query);
        return query;
    }

    @Override
    public U defaultUpdater() {
        U updater = this.updater();
        this.setUpdateDefault(updater);
        return updater;
    }

    protected abstract Q aliasQuery(String alias, Parameters parameters);

    /**
     * 自动分配表别名查询构造器(join查询的时候需要定义表别名)
     * 如果要自定义别名, 使用方法 {@link #aliasQuery(String)}
     */
    @Override
    public Q aliasQuery() {
        Q query = this.aliasQuery(Parameters.alias(), new Parameters());
        this.setQueryDefault(query);
        return query;
    }

    /**
     * 显式指定表别名(join查询的时候需要定义表别名)
     */
    @Override
    public Q aliasQuery(String alias) {
        Q query = this.aliasQuery(alias, new Parameters());
        this.setQueryDefault(query);
        return query;
    }

    /**
     * 关联查询, 根据fromQuery自动设置别名和关联?参数
     * 如果要自定义别名, 使用方法 {@link #aliasWith(String, BaseQuery)}
     */
    @Override
    public Q aliasWith(BaseQuery fromQuery) {
        Parameters parameters = fromQuery.getWrapperData().getParameters();
        Q query = this.aliasQuery(Parameters.alias(), parameters);
        this.setQueryDefault(query);
        return query;
    }

    /**
     * 关联查询, 显式设置别名, 根据fromQuery自动关联?参数
     */
    @Override
    public Q aliasWith(String alias, BaseQuery fromQuery) {
        Q query = this.aliasQuery(alias, fromQuery.getWrapperData().getParameters());
        this.setQueryDefault(query);
        return query;
    }

    /**
     * 设置表名动态设置
     */
    public D setTableDynamic(TableDynamic dynamic) {
        this.dynamic = dynamic;
        return (D) this;
    }

    /**
     * 获取表名
     */
    public Supplier<String> table() {
        if (dynamic == null) {
            if ((dbType == DbType.DERBY || dbType == DbType.POSTGRE_SQL) && notBlank(schema)) {
                return () -> this.schema + "." + this.dbType.wrap(this.tableName);
            } else {
                return () -> this.dbType.wrap(this.tableName);
            }
        } else {
            return () -> dynamic.get(this.tableName);
        }
    }
}