package cn.org.atool.fluent.mybatis.segment.model;

import cn.org.atool.fluent.mybatis.If;
import cn.org.atool.fluent.mybatis.base.crud.IQuery;
import cn.org.atool.fluent.mybatis.base.model.Column;
import cn.org.atool.fluent.mybatis.base.model.ISqlOp;
import cn.org.atool.fluent.mybatis.exception.FluentMybatisException;
import cn.org.atool.fluent.mybatis.segment.WhereSegmentList;
import cn.org.atool.fluent.mybatis.utility.CustomizedSql;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

import java.util.*;
import java.util.function.Supplier;

import static cn.org.atool.fluent.mybatis.If.isBlank;
import static cn.org.atool.fluent.mybatis.If.notBlank;
import static cn.org.atool.fluent.mybatis.mapper.StrConstant.*;
import static cn.org.atool.fluent.mybatis.utility.MybatisUtil.*;
import static java.util.stream.Collectors.joining;

/**
 * WrapperInfo: 查询器或更新器xml需要用到信息
 *
 * @author darui.wu 2020/6/23 5:15 下午
 */
@SuppressWarnings({"rawtypes", "unchecked"})
@Getter
public class WrapperData implements IWrapperData {
    /**
     * select 前面是否加 DISTINCT 关键字
     */
    @Setter
    protected boolean isDistinct = false;
    /**
     * 查询字段
     */
    protected List<String> sqlSelect = new ArrayList<>(8);
    /**
     * 表名
     */
    @Getter(AccessLevel.NONE)
    protected Supplier<String> table;

    @Setter
    private String alias;
    /**
     * 自定义参数列表
     */
    protected final Parameters parameters;
    /**
     * where, group, having ,order by对象列表
     */
    protected final MergeSegments mergeSegments = new MergeSegments();

    /**
     * 分页参数
     */
    @Setter
    protected PagedOffset paged;

    /**
     * SQL 更新字段内容，例如：name='1', age=2
     */
    private final Map<String, String> updates = new LinkedHashMap<>(16);
    /**
     * sql 中 hint 维护
     */
    private final Map<HintType, String> hints = new HashMap<>(4);
    /**
     * 实体类型
     */
    private final Class entityClass;
    /**
     * 对应的嵌套查询类
     */
    private final Class queryClass;
    /**
     * 字段别名列表
     */
    private final Set<String> fieldAlias = new HashSet<>();
    /**
     * 按条件更新时, 跳过检查乐观锁条件字段
     * 默认必须有乐观锁
     */
    @Setter
    private boolean ignoreLockVersion = false;

    public WrapperData() {
        this.parameters = new Parameters();
        this.queryClass = null;
        this.entityClass = null;
    }

    public WrapperData(Supplier<String> table, String alias, Parameters parameters, Class entityClass, Class queryClass) {
        notNull(entityClass, "entityClass must not null,please set entity before use this method!");
        this.table = table;
        this.alias = alias;
        this.parameters = parameters;
        this.entityClass = entityClass;
        this.queryClass = queryClass;
    }

    public String getTable() {
        return isBlank(alias) ? this.table.get() : this.table.get() + " " + this.alias;
    }

    @Override
    public String getSqlSelect() {
        if (this.sqlSelect.isEmpty()) {
            return null;
        } else {
            String sql = String.join(COMMA_SPACE, sqlSelect);
            return isBlank(sql) ? null : sql.trim();
        }
    }

    public List<String> sqlSelect() {
        return this.sqlSelect;
    }

    /**
     * 用户完整自定义的sql语句
     */
    private String customizedSql = null;

    private List<Union> unions = null;

    /**
     * 增加union查询
     *
     * @param union union key
     * @param query union query
     */
    public void union(String union, IQuery query) {
        if (this.unions == null) {
            this.unions = new ArrayList<>();
        }
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
            this.customizedSql = sql;
        } else {
            this.customizedSql = CustomizedSql.rewriteSql(sql, this.parameters, parameter);
        }
    }

    @Override
    public String getQuerySql() {
        if (If.notBlank(customizedSql)) {
            return customizedSql;
        }
        String select = this.getSqlSelect();
        String where = this.getWhereSql();
        String sql = "SELECT" + SPACE +
            (isBlank(select) ? ASTERISK : select.trim()) + SPACE +
            "FROM" + SPACE +
            this.getTable() + SPACE +
            (isBlank(where) ? EMPTY : "WHERE " + where.trim()) + SPACE +
            this.getGroupBy().trim() + SPACE +
            this.getOrderBy().trim() + SPACE +
            this.getLastSql().trim();
        sql = sql.trim();
        if (unions == null || unions.isEmpty()) {
            return sql;
        } else {
            return "(" + sql + ")" + unions.stream().map(Union::sql).collect(joining(SPACE));
        }
    }

    @Override
    public String getUpdateStr() {
        String sql = this.updates.entrySet().stream().map(i -> i.getKey() + " = " + i.getValue()).collect(joining(COMMA_SPACE));
        return isBlank(sql) ? null : sql.trim();
    }

    @Override
    public String getMergeSql() {
        String sql = mergeSegments.sql();
        return isBlank(sql) ? null : sql.trim();
    }

    @Override
    public String getWhereSql() {
        return this.mergeSegments.whereSql();
    }

    @Override
    public String getGroupBy() {
        return this.mergeSegments.groupBy();
    }

    @Override
    public String getOrderBy() {
        return this.mergeSegments.orderBy();
    }

    @Override
    public String getLastSql() {
        return this.mergeSegments.last();
    }

    /*
     * ============================================================
     *                          以下是数据操作部分
     * ============================================================
     */

    /**
     * 附加sql,只允许执行一次
     *
     * @param lastSql 附加sql
     */
    public void last(String lastSql) {
        this.mergeSegments.setLastSql(lastSql);
    }

    /**
     * 增加条件设置
     *
     * @param keyWord  or and
     * @param column   设置条件的字段
     * @param operator 条件操作
     * @param paras    条件参数（填充 operator 中占位符?)
     */
    public void apply(KeyWordSegment keyWord, Column column, ISqlOp operator, Object... paras) {
        this.apply(keyWord, column, operator, null, paras);
    }

    /**
     * 增加查询字段
     *
     * @param column 字段
     */
    public void addSelectColumn(String column) {
        if (notBlank(column)) {
            this.sqlSelect.add(column);
            this.fieldAlias.addAll(parseAlias(column));
        }
    }

    /**
     * 解析别名列表
     *
     * @param column 字段
     * @return ignore
     */
    static List<String> parseAlias(String column) {
        int pos = -1;
        List<String> list = new ArrayList<>();
        StringBuilder buff = new StringBuilder();
        for (char c : (column + SPACE).toCharArray()) {
            if (pos <= 0 && isSpace(c)) {
                pos = 0;
            } else if (pos == 0 && (c == 'a' || c == 'A')) {
                pos = 1;
            } else if (pos == 1 && (c == 's' || c == 'S')) {
                pos = 2;
            } else if ((pos == 2 || pos == 3) && isSpace(c)) {
                pos = 3;
            } else if (pos >= 3 && (isLetter(c) || isDigit(c))) {
                pos = 4;
                buff.append(c);
            } else if (pos == 4 && (isSpace(c) || c == ',')) {
                list.add(buff.toString());
                buff = new StringBuilder();
                pos = -1;
            } else {
                pos = -1;
            }
        }
        return list;
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
    public void apply(KeyWordSegment keyWord, Column column, ISqlOp operator, String format, Object... args) {
        if (keyWord == null) {
            throw new FluentMybatisException("the first segment should be: 'AND', 'OR', 'GROUP BY', 'HAVING' or 'ORDER BY'");
        }
        String segment = operator.operator(column, this.getParameters(), format, args);
        this.getMergeSegments().add(keyWord, column.columnSegment(), () -> segment);
    }

    public void apply(KeyWordSegment keyWord, ISqlSegment... segments) {
        if (keyWord == null) {
            throw new FluentMybatisException("the first segment should be: 'AND', 'OR', 'GROUP BY', 'HAVING' or 'ORDER BY'");
        }
        this.getMergeSegments().add(keyWord, segments);
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
            updates.put(column.wrapColumn(), this.paramSql(column, functionSql, values));
        }
    }

    /**
     * 获取where条件字段
     *
     * @return ignore
     */
    public List<String> findWhereColumns() {
        WhereSegmentList list = this.getMergeSegments().getWhere();
        if (list == null || list.getSegments() == null) {
            return Collections.EMPTY_LIST;
        }
        List<String> columns = new ArrayList<>();
        for (ISqlSegment segment : list.getSegments()) {
            if (segment instanceof ColumnSegment) {
                columns.add(segment.getSqlSegment());
            }
        }
        return columns;
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

    public void sharedParameter(WrapperData wrapperData) {
        this.getParameters().sharedParameter(wrapperData.getParameters());
    }

    public void sharedParameter(Parameters parameters) {
        this.getParameters().sharedParameter(parameters);
    }

    public ISqlSegment[] whereSegments() {
        return this.mergeSegments.getWhere().getSegments().toArray(new ISqlSegment[0]);
    }

    public boolean hasUnion() {
        return this.unions != null && !this.unions.isEmpty();
    }

    public List<Union> unions() {
        return this.unions;
    }

    @Getter
    public static class Union {
        private final String key;

        private final IQuery query;

        Union(String key, IQuery query) {
            this.key = key;
            this.query = query;
        }

        public String sql() {
            return key + SPACE + "(" + query.getWrapperData().getQuerySql().trim() + ")";
        }
    }
}