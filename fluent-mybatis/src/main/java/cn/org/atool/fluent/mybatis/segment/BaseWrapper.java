package cn.org.atool.fluent.mybatis.segment;

import cn.org.atool.fluent.mybatis.base.IEntity;
import cn.org.atool.fluent.mybatis.base.crud.IBaseQuery;
import cn.org.atool.fluent.mybatis.base.crud.IWrapper;
import cn.org.atool.fluent.mybatis.base.model.FieldMapping;
import cn.org.atool.fluent.mybatis.exception.FluentMybatisException;
import cn.org.atool.fluent.mybatis.metadata.TableMeta;
import cn.org.atool.fluent.mybatis.metadata.TableMetaHelper;
import cn.org.atool.fluent.mybatis.segment.model.Parameters;
import cn.org.atool.fluent.mybatis.segment.model.WrapperData;
import lombok.Getter;

import java.util.List;
import java.util.function.Supplier;

import static cn.org.atool.fluent.mybatis.If.isBlank;
import static cn.org.atool.fluent.mybatis.If.notBlank;
import static cn.org.atool.fluent.mybatis.mapper.StrConstant.EMPTY;
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

    protected final Supplier<String> table;
    /**
     * 表别名
     */
    @Getter
    protected final String tableAlias;

    @Getter
    protected final WrapperData wrapperData;

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
     * 判断字段是否在范围内
     *
     * @param column 字段
     * @return 如果不是合法字段，抛出异常
     * @throws FluentMybatisException 字段校验异常
     */
    protected void validateColumn(String column) throws FluentMybatisException {
        if (notBlank(column) && !this.allFields().contains(column)) {
            throw new FluentMybatisException("the column[" + column + "] was not found in table[" + this.wrapperData.getTable() + "].");
        }
    }

    /**
     * 表所有字段列表
     *
     * @return
     */
    protected abstract List<String> allFields();

    protected TableMeta getTableMeta() {
        return TableMetaHelper.getTableInfo(this.getWrapperData().getEntityClass());
    }

    /**
     * 给字段名称追加上表别名
     *
     * @param column
     * @return
     */
    protected String appendAlias(String column) {
        if (notBlank(this.tableAlias) && FieldMapping.isColumnName(column)) {
            return tableAlias + "." + column;
        } else {
            return column;
        }
    }

    /**
     * 通过Wrapper直接设置变量共享关系
     *
     * @param parameters
     */
    protected void setSharedParameter(Parameters parameters) {
        this.wrapperData.getParameters().setSharedParameter(parameters);
    }

    /**
     * 通过Wrapper直接设置变量共享关系
     *
     * @param wrapper
     */
    protected void setSharedParameter(BaseWrapper wrapper) {
        this.wrapperData.getParameters().setSharedParameter(wrapper.getWrapperData().getParameters());
    }
}