package cn.org.atool.fluent.mybatis.segment;

import cn.org.atool.fluent.mybatis.base.IEntity;
import cn.org.atool.fluent.mybatis.base.IRefs;
import cn.org.atool.fluent.mybatis.base.crud.IBaseQuery;
import cn.org.atool.fluent.mybatis.base.crud.IWrapper;
import cn.org.atool.fluent.mybatis.base.model.FieldMapping;
import cn.org.atool.fluent.mybatis.metadata.DbType;
import cn.org.atool.fluent.mybatis.metadata.TableMeta;
import cn.org.atool.fluent.mybatis.metadata.TableMetaHelper;
import cn.org.atool.fluent.mybatis.segment.model.Parameters;
import cn.org.atool.fluent.mybatis.segment.model.WrapperData;
import lombok.Getter;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

import static cn.org.atool.fluent.mybatis.If.isBlank;
import static cn.org.atool.fluent.mybatis.If.notBlank;
import static cn.org.atool.fluent.mybatis.mapper.StrConstant.EMPTY;
import static cn.org.atool.fluent.mybatis.utility.MybatisUtil.isColumnName;
import static cn.org.atool.fluent.mybatis.utility.MybatisUtil.notNull;

/**
 * 查询条件封装
 *
 * @param <E>  对应的实体类
 * @param <W>  更新器或查询器
 * @param <NQ> 对应的嵌套查询器
 * @author darui.wu
 */
public abstract class BaseWrapper<
    E extends IEntity,
    W extends IWrapper<E, W, NQ>,
    NQ extends IBaseQuery<E, NQ>
    >
    implements IWrapper<E, W, NQ> {
    private static final long serialVersionUID = 2674302532927710150L;

    @Getter
    protected Supplier<String> table;
    /**
     * 表别名
     */
    @Getter
    protected final String tableAlias;

    @Getter
    protected WrapperData wrapperData;

    protected BaseWrapper(String tableAlias) {
        this.tableAlias = tableAlias;
    }

    protected BaseWrapper(Supplier<String> table, String tableAlias, Class<E> entityClass, Class queryClass) {
        this(table, tableAlias, new Parameters(), entityClass, queryClass);
    }

    protected BaseWrapper(Supplier<String> table, String tableAlias, Parameters parameters, Class<E> entityClass, Class queryClass) {
        notNull(entityClass, "entityClass must not null,please set entity before use this method!");
        this.table = table;
        this.tableAlias = isBlank(tableAlias) ? EMPTY : tableAlias.trim();
        this.wrapperData = new WrapperData(table, this.tableAlias, parameters, entityClass, queryClass);
    }

    /**
     * 如果有主键字段返回主键字段
     * 如果没有定义主键，返回null
     *
     * @return 主键字段
     */
    public String primary() {
        return null;
    }

    /**
     * 表所有字段列表
     *
     * @return 所有字段
     */
    protected abstract List<String> allFields();

    protected TableMeta getTableMeta() {
        return TableMetaHelper.getTableInfo(this.getWrapperData().getEntityClass());
    }

    /**
     * 给字段名称追加上表别名
     *
     * @param column 字段
     * @return taleAlias.column
     */
    protected String appendAlias(String column) {
        if (notBlank(this.tableAlias) && isColumnName(column)) {
            return tableAlias + "." + column;
        } else {
            return column;
        }
    }

    /**
     * 通过Wrapper直接设置变量共享关系
     *
     * @param parameters 参数
     * @deprecated 避免使用set开头命名
     */
    @Deprecated
    protected void setSharedParameter(Parameters parameters) {
        this.sharedParameter(parameters);
    }

    /**
     * 通过Wrapper直接设置变量共享关系
     *
     * @param parameters 参数
     */
    protected void sharedParameter(Parameters parameters) {
        this.wrapperData.getParameters().sharedParameter(parameters);
    }

    /**
     * 通过Wrapper直接设置变量共享关系
     *
     * @param wrapper BaseWrapper
     */
    protected void sharedParameter(BaseWrapper wrapper) {
        this.wrapperData.getParameters().sharedParameter(wrapper.getWrapperData().getParameters());
    }

    /**
     * 返回字段对应的column映射
     *
     * @param column 数据库字段名称
     * @return 字段映射
     */
    public FieldMapping column(String column) {
        return this.column2mapping().get(column);
    }

    /**
     * 数据库类型
     *
     * @return DbType
     */
    public DbType dbType() {
        DbType dbType = this.myDbType();
        return dbType == null ? IRefs.instance().defaultDbType() : dbType;
    }

    protected DbType myDbType() {
        return null;
    }

    /**
     * 返回字段映射关系
     *
     * @return 字段映射
     */
    protected Map<String, FieldMapping> column2mapping() {
        return new HashMap<>();
    }
}