package cn.org.atool.fluent.mybatis.segment.model;

import cn.org.atool.fluent.mybatis.If;
import cn.org.atool.fluent.mybatis.base.crud.IQuery;
import cn.org.atool.fluent.mybatis.base.crud.IWrapper;
import cn.org.atool.fluent.mybatis.base.entity.IMapping;
import cn.org.atool.fluent.mybatis.base.intf.IDataByColumn;
import cn.org.atool.fluent.mybatis.base.mapper.IRichMapper;
import cn.org.atool.fluent.mybatis.base.model.ISqlOp;
import cn.org.atool.fluent.mybatis.base.model.SqlOp;
import cn.org.atool.fluent.mybatis.exception.FluentMybatisException;
import cn.org.atool.fluent.mybatis.mapper.MapperSql;
import cn.org.atool.fluent.mybatis.segment.BaseWrapper;
import cn.org.atool.fluent.mybatis.segment.fragment.*;
import cn.org.atool.fluent.mybatis.utility.CustomizedSql;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

import java.util.*;
import java.util.stream.Stream;

import static cn.org.atool.fluent.mybatis.If.isBlank;
import static cn.org.atool.fluent.mybatis.If.notBlank;
import static cn.org.atool.fluent.mybatis.segment.fragment.Fragments.SEG_ASTERISK;
import static cn.org.atool.fluent.mybatis.segment.fragment.Fragments.SEG_EMPTY;
import static cn.org.atool.fluent.mybatis.utility.MybatisUtil.notNull;
import static cn.org.atool.fluent.mybatis.utility.MybatisUtil.parseAlias;
import static cn.org.atool.fluent.mybatis.utility.StrConstant.*;
import static java.util.stream.Collectors.joining;

/**
 * WrapperInfo: 查询器或更新器xml需要用到信息
 *
 * @author darui.wu 2020/6/23 5:15 下午
 */
@SuppressWarnings({"rawtypes", "unchecked"})
@Getter
public class WrapperData implements IWrapperData, IDataByColumn {
    protected final IWrapper wrapper;
    /**
     * 自定义参数列表
     */
    protected final Parameters parameters;
    /**
     * select 前面是否加 DISTINCT 关键字
     */
    @Setter
    protected boolean distinct = false;
    /**
     * 查询字段
     */
    public final JoiningFrag select = JoiningFrag.get(COMMA_SPACE).setFilter(If::notBlank);

    /**
     * SQL 更新字段内容，例如：name='1', age=2
     */
    private final Map<IFragment, String> updates = new LinkedHashMap<>(16);
    /**
     * sql 中 hint 维护
     */
    private final Map<HintType, String> hints = new HashMap<>(4);
    /**
     * 字段别名列表
     */
    private final Set<String> fieldAlias = new HashSet<>();

    /* =========WHERE条件部分======== */
    /**
     * where, group, having ,order by对象列表
     */
    @Getter(AccessLevel.NONE)
    private MergeSegments segments = new MergeSegments();


    /**
     * 外部传入的where条件, 只在 {@link IRichMapper#logicDelete(IQuery)}场景下使用
     *
     * @param query IQuery
     */
    public void replacedByQuery(IQuery query) {
        this.segments = query.data().segments();
        this.ignoreLockVersion = query.data().ignoreLockVersion;
        query.data().sharedParameter(this);
    }

    @Override
    public MergeSegments segments() {
        return this.segments;
    }

    /**
     * 分页参数
     */
    @Setter
    @Getter(AccessLevel.NONE)
    protected PagedOffset paged;

    public PagedOffset paged() {
        return this.paged;
    }

    /**
     * 按条件更新时, 跳过检查乐观锁条件字段
     * 默认必须有乐观锁
     */
    @Setter
    @Getter(AccessLevel.NONE)
    private boolean ignoreLockVersion = false;

    public boolean ignoreVersion() {
        return ignoreLockVersion;
    }

    public WrapperData(IWrapper wrapper) {
        this(wrapper, new Parameters());
    }

    public WrapperData(IWrapper wrapper, Parameters parameters) {
        notNull(wrapper, "IQuery/IUpdate must not null!");
        notNull(parameters, "Parameters must not null!");
        this.wrapper = wrapper;
        this.parameters = parameters;
    }

    /**
     * 返回表名+表别名
     *
     * @return IFragment
     */
    public IFragment table() {
        return m -> {
            String alias = this.wrapper.getTableAlias();
            String table = this.wrapper.table(true).get(m);
            return table + (isBlank(alias) ? EMPTY : SPACE + alias);
        };
    }

    @Override
    public IFragment select() {
        if (this.select.isEmpty()) {
            Optional<IMapping> mapping = wrapper.mapping();
            return mapping.map(IMapping::getSelectAll).orElse(SEG_ASTERISK);
        } else {
            return this.select;
        }
    }

    /**
     * 用户完整自定义的sql语句
     */
    private CachedFrag customizedSql = SEG_EMPTY;

    private final List<Union> unions = new ArrayList<>();

    /**
     * 增加union查询
     *
     * @param union union key
     * @param query union query
     */
    public void union(String union, IQuery query) {
        this.unions.add(new Union(union, query));
    }

    /**
     * 自定义的完整sql语句设置
     *
     * @param sql       sql
     * @param parameter 参数
     */
    public void customizedSql(String sql, Object parameter) {
        if (parameter == null) {
            this.customizedSql = CachedFrag.set(sql);
        } else {
            String newSql = CustomizedSql.rewriteSql(sql, this.parameters, parameter);
            this.customizedSql = CachedFrag.set(newSql);
        }
    }

    /**
     * 不同数据库分页查询
     *
     * @param sql 非分页查询sql
     * @return sql segment
     */
    public IFragment wrappedByPaged(String sql) {
        PagedOffset paged = this.paged();
        if (paged == null) {
            return CachedFrag.set(sql);
        }
        return m -> {
            Parameters p = this.getParameters();
            String offset = p.putParameter(null, paged.getOffset());
            String size = p.putParameter(null, paged.getLimit());
            String endOffset = p.putParameter(null, paged.getEndOffset());
            return m.db().paged(sql, offset, size, endOffset);
        };
    }

    @Override
    public IFragment sql(boolean withPaged) {
        IFragment withoutPagedSegment = this.withoutPaged();
        IFragment withPagedSegment = this.withPaged(withPaged, withoutPagedSegment);
        return this.union(withPagedSegment);
    }

    /**
     * 添加limit语句
     */
    private IFragment withPaged(boolean withPaged, IFragment withoutPaged) {
        if (!withPaged || this.paged == null) {
            return withoutPaged;
        } else {
            return m -> {
                Parameters p = this.getParameters();
                String offset = p.putParameter(null, paged.getOffset());
                String size = p.putParameter(null, paged.getLimit());
                String endOffset = p.putParameter(null, paged.getEndOffset());
                return m.db().paged(withoutPaged.get(m), offset, size, endOffset);
            };
        }
    }

    private IFragment withoutPaged() {
        if (customizedSql.notEmpty()) {
            return customizedSql;
        }
        return m -> {
            MapperSql text = new MapperSql();
            text.SELECT(m, this.table(), this, this.select());
            text.WHERE_GROUP_ORDER_BY(m, this);
            return text.toString();
        };
    }

    private IFragment union(IFragment segment) {
        segment = unions.isEmpty() ? segment : BracketFrag.set(segment);
        for (Union union : this.unions) {
            segment = segment.plus(SPACE + union.key + SPACE).plus(BracketFrag.set(union.query));
        }
        return segment;
    }

    @Override
    public IFragment update() {
        return m -> this.updates.entrySet().stream()
            .map(i -> i.getKey().get(m) + " = " + i.getValue())
            .collect(joining(COMMA_SPACE));
    }

    /*
     * ============================================================
     *                          以下是数据操作部分
     * ============================================================
     */
    /**
     * 相等的条件
     * key: 字段, value: 所有可能的值
     */
    private final Map<String, List<Object>> eqWhere = new HashMap<>();

    @Override
    public <T> T valueByColumn(String column) {
        return (T) this.eqWhere.get(column);
    }

    @Override
    public Class entityClass() {
        if (this.wrapper instanceof BaseWrapper) {
            return ((BaseWrapper) this.wrapper).getEntityClass();
        } else {
            return null;
        }
    }

    private synchronized List<Object> getEqValues(String key) {
        if (!eqWhere.containsKey(key)) {
            eqWhere.put(key, new ArrayList<>());
        }
        return eqWhere.get(key);
    }

    public void addEqWhere(Map<String, List<Object>> values) {
        for (Map.Entry<String, List<Object>> entry : values.entrySet()) {
            getEqValues(entry.getKey()).addAll(entry.getValue());
        }
    }

    /**
     * 增加条件设置
     *
     * @param keyWord  or and
     * @param column   设置条件的字段
     * @param operator 条件操作
     * @param paras    条件参数（填充 operator 中占位符?)
     */
    public void apply(KeyFrag keyWord, IFragment column, ISqlOp operator, Object... paras) {
        this.apply(keyWord, column, operator, (String) null, paras);
        if (!(column instanceof Column)) {
            return;
        }
        /* 采集相等的字段值, 供分表使用 */
        if (operator == SqlOp.EQ || operator == SqlOp.IN) {
            Stream.of(paras).forEach(c -> getEqValues(((Column) column).column).add(c));
        }
    }

    /**
     * 增加查询字段
     *
     * @param column 字段
     */
    public void select(String column) {
        if (notBlank(column)) {
            this.select.add(Column.set(this.wrapper, column));
            this.fieldAlias.addAll(parseAlias(column));
        }
    }

    public void select(IFragment column) {
        this.select.add(column);
    }

    /**
     * 增加条件设置
     *
     * @param keyWord  or and
     * @param column   设置条件的字段
     * @param operator 条件操作
     * @param format   格式化sql语句
     * @param args     条件参数（填充 operator 中占位符?)
     */
    public void apply(KeyFrag keyWord, IFragment column, ISqlOp operator, String format, Object... args) {
        if (keyWord == null) {
            throw new FluentMybatisException("the first segment should be: 'AND', 'OR', 'GROUP BY', 'HAVING' or 'ORDER BY'");
        }
        IFragment value = operator.operator(column, this.getParameters(), format, args);
        this.segments().add(keyWord, column, value);
    }

    public void apply(KeyFrag keyWord, IFragment column, ISqlOp operator, IFragment format, Object... args) {
        if (keyWord == null) {
            throw new FluentMybatisException("the first segment should be: 'AND', 'OR', 'GROUP BY', 'HAVING' or 'ORDER BY'");
        }
        IFragment segment = operator.operator(column, this.getParameters(), format, args);
        this.segments().add(keyWord, column, segment);
    }

    public void apply(KeyFrag keyWord, IFragment... segments) {
        if (keyWord == null) {
            throw new FluentMybatisException("the first segment should be: 'AND', 'OR', 'GROUP BY', 'HAVING' or 'ORDER BY'");
        }
        this.segments().add(keyWord, segments);
    }

    /**
     * 根据函数和变量构建占位符和设置占位符对应的变量值
     *
     * @param column      映射字段, 如果 = null, 表示非原始字段赋值
     * @param functionSql 函数
     * @param values      变量列表
     * @return 参数化后的sql
     */
    public String paramSql(Column column, String functionSql, Object[] values) {
        return this.parameters.paramSql(column, functionSql, values);
    }

    /**
     * 更新column字段值
     *
     * @param column 被更新字段
     * @param value  更新值
     */
    public void updateSet(Column column, Object value) {
        this.updateSql(column, QUESTION_MARK, value);
    }

    /**
     * 设置更新（自定义SQL）
     *
     * @param column      更新的字段
     * @param functionSql set function sql
     * @param values      对应的参数
     */
    public void updateSql(Column column, String functionSql, Object... values) {
        if (notBlank(functionSql)) {
            updates.put(column, this.paramSql(column, functionSql, values));
        }
    }

    public void hint(HintType type, String hint) {
        if (notBlank(hint)) {
            this.hints.put(type, hint);
        }
    }

    public String hint(HintType type) {
        String hint = this.hints.get(type);
        return isBlank(hint) ? SPACE : SPACE + hint + SPACE;
    }

    public void sharedParameter(WrapperData data) {
        this.getParameters().sharedParameter(data.getParameters());
    }

    public void sharedParameter(Parameters parameters) {
        this.getParameters().sharedParameter(parameters);
    }

    /**
     * 有 group by语句
     *
     * @return true: has group by
     */
    public boolean hasGroupBy() {
        return !this.segments().groupBy.isEmpty();
    }

    /**
     * 是否有更多数据
     *
     * @param total 总记录数
     * @return true: has next page
     */
    public boolean hasNext(long total) {
        return this.paged() != null && total > this.paged().getEndOffset();
    }

    public boolean hasSelect() {
        return !this.select.isEmpty();
    }

    private static class Union {
        final String key;

        final IQuery query;

        Union(String key, IQuery query) {
            this.key = key;
            this.query = query;
        }
    }
}