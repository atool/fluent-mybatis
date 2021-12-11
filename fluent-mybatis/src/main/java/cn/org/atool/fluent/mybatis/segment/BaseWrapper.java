package cn.org.atool.fluent.mybatis.segment;

import cn.org.atool.fluent.mybatis.base.IEntity;
import cn.org.atool.fluent.mybatis.base.crud.IBaseQuery;
import cn.org.atool.fluent.mybatis.base.crud.IQuery;
import cn.org.atool.fluent.mybatis.base.crud.IWrapper;
import cn.org.atool.fluent.mybatis.base.entity.IMapping;
import cn.org.atool.fluent.mybatis.base.model.FieldMapping;
import cn.org.atool.fluent.mybatis.base.model.UniqueType;
import cn.org.atool.fluent.mybatis.functions.StringSupplier;
import cn.org.atool.fluent.mybatis.metadata.TableMeta;
import cn.org.atool.fluent.mybatis.metadata.TableMetaHelper;
import cn.org.atool.fluent.mybatis.segment.fragment.IFragment;
import cn.org.atool.fluent.mybatis.segment.model.Parameters;
import cn.org.atool.fluent.mybatis.segment.model.WrapperData;
import lombok.AccessLevel;
import lombok.Getter;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static cn.org.atool.fluent.mybatis.If.isBlank;
import static cn.org.atool.fluent.mybatis.utility.MybatisUtil.notNull;
import static cn.org.atool.fluent.mybatis.utility.StrConstant.EMPTY;

/**
 * 查询条件封装
 *
 * @param <E>  对应的实体类
 * @param <W>  更新器或查询器
 * @param <NQ> 对应的嵌套查询器
 * @author darui.wu
 */
@SuppressWarnings({"rawtypes", "unchecked"})
public abstract class BaseWrapper<
    E extends IEntity,
    W extends IWrapper<E, W, NQ>,
    NQ extends IBaseQuery<E, NQ>
    >
    implements IWrapper<E, W, NQ> {
    private static final long serialVersionUID = 2674302532927710150L;

    protected IFragment table;
    /**
     * 表别名
     */
    private StringSupplier tableAlias;

    @Override
    public String getTableAlias() {
        return tableAlias == null ? null : tableAlias.get();
    }

    protected void setTableAlias(StringSupplier tableAlias) {
        this.tableAlias = tableAlias;
    }

    protected void setTableAlias(String tableAlias) {
        this.tableAlias = () -> isBlank(tableAlias) ? EMPTY : tableAlias.trim();
    }

    @Getter(AccessLevel.NONE)
    protected WrapperData data;

    @Getter
    protected Class entityClass;

    protected BaseWrapper(String tableAlias) {
        this.setTableAlias(tableAlias);
    }

    protected BaseWrapper(StringSupplier tableAlias) {
        this.setTableAlias(tableAlias);
    }

    protected BaseWrapper(IFragment table, StringSupplier tableAlias, Class<E> entityClass) {
        this(table, tableAlias, new Parameters(), entityClass);
    }

    protected BaseWrapper(IFragment table, StringSupplier tableAlias, Parameters parameters, Class<E> entityClass) {
        notNull(entityClass, "entityClass must not null,please set entity before use this method!");
        this.data = new WrapperData(this, parameters);
        this.table = table == null ? this.table() : table;
        this.setTableAlias(tableAlias);
        this.entityClass = entityClass;
    }

    private IFragment table() {
        return m -> {
            IMapping mapping = this.mapping().orElse(m);
            return mapping.table(this.data).get(m);
        };
    }

    /**
     * 返回字段映射关系
     *
     * @return 字段映射关系
     */
    public Optional<IMapping> mapping() {
        return Optional.empty();
    }

    @Override
    public IFragment table(boolean notFoundError) {
        if (this.table != null && this.table.notEmpty()) {
            return this.table;
        } else if (notFoundError) {
            return this.mapping().map(m -> m.table(this.data)).orElseThrow(() -> new RuntimeException("table name not found."));
        } else {
            return this.mapping().map(m -> m.table(this.data)).orElse(null);
        }
    }

    /**
     * 返回指定类型字段名称
     * 如果没有指定类型字段，返回null
     *
     * @return 指定类型的字段
     */
    public String fieldName(UniqueType type) {
        return this.mapping().flatMap(m -> m.findField(type)).map(c -> c.column).orElse(null);
    }

    /**
     * 表所有字段列表
     *
     * @return 所有字段
     */
    public List<String> allFields() {
        return this.mapping().map(IMapping::getAllColumns).orElse(Collections.emptyList());
    }

    public WrapperData data() {
        return data;
    }

    protected TableMeta getTableMeta() {
        return TableMetaHelper.getTableInfo(this.entityClass);
    }

    /**
     * 通过Wrapper直接设置变量共享关系
     *
     * @param parameters 参数
     */
    protected void sharedParameter(Parameters parameters) {
        this.data.getParameters().sharedParameter(parameters);
    }

    /**
     * 通过Wrapper直接设置变量共享关系
     *
     * @param wrapper BaseWrapper
     */
    protected void sharedParameter(BaseWrapper wrapper) {
        this.data.getParameters().sharedParameter(wrapper.data().getParameters());
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
     * IQuery的union操作
     *
     * @return union后的IQuery
     */
    protected IQuery union(String key, IQuery... queries) {
        if (this.data.paged() != null) {
            throw new RuntimeException("Limit syntax is not supported for union queries.");
        }
        if (queries == null || queries.length == 0) {
            throw new IllegalArgumentException("The size of parameter[queries] should be greater than zero.");
        }
        for (IQuery query : queries) {
            this.data.union(key, query);
            query.data().sharedParameter(this.data);
        }
        return (IQuery) this;
    }

    /**
     * 返回字段映射关系
     *
     * @return 字段映射
     */
    private Map<String, FieldMapping> column2mapping() {
        return this.mapping().map(IMapping::getColumnMap).orElse(Collections.EMPTY_MAP);
    }
}