package cn.org.atool.fluent.mybatis.segment.model;

import cn.org.atool.fluent.mybatis.base.model.SqlOp;
import cn.org.atool.fluent.mybatis.exception.FluentMybatisException;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

import java.util.*;

import static cn.org.atool.fluent.mybatis.If.isBlank;
import static cn.org.atool.fluent.mybatis.If.notBlank;
import static cn.org.atool.fluent.mybatis.mapper.StrConstant.*;
import static cn.org.atool.fluent.mybatis.utility.MybatisUtil.notNull;
import static java.util.stream.Collectors.joining;

/**
 * WrapperInfo: 查询器或更新器xml需要用到信息
 *
 * @author darui.wu
 * @create 2020/6/23 5:15 下午
 */
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
    protected Set<String> sqlSelect = new LinkedHashSet<>(8);
    /**
     * 表名
     */
    @Getter(AccessLevel.NONE)
    protected String table;

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
    @Getter
    private final Class entityClass;
    /**
     * 对应的嵌套查询类
     */
    private final Class queryClass;

    protected WrapperData() {
        this.parameters = new Parameters();
        this.queryClass = null;
        this.entityClass = null;
    }

    public WrapperData(String table, String alias, Parameters parameters, Class entityClass, Class queryClass) {
        notNull(entityClass, "entityClass must not null,please set entity before use this method!");
        this.table = table;
        this.alias = alias;
        this.parameters = parameters;
        this.entityClass = entityClass;
        this.queryClass = queryClass;
    }

    public String getTable() {
        return isBlank(alias) ? this.table : this.table + " " + this.alias;
    }

    @Override
    public String getSqlSelect() {
        if (this.sqlSelect.isEmpty()) {
            return null;
        } else {
            String sql = sqlSelect.stream().collect(joining(COMMA_SPACE));
            return isBlank(sql) ? null : sql.trim();
        }
    }

    public Set<String> sqlSelect() {
        return this.sqlSelect;
    }

    @Override
    public String getQuerySql() {
        String select = this.getSqlSelect();
        String sql = String.format(SELECT_FROM_WHERE, select == null ? ASTERISK : select, this.table, this.getMergeSql());
        return isBlank(sql) ? null : sql.trim();
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

    /**
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
    public void apply(KeyWordSegment keyWord, String column, SqlOp operator, Object... paras) {
        this.apply(keyWord, column, operator, null, paras);
    }

    /**
     * 增加查询字段
     *
     * @param column
     */
    public void addSelectColumn(String column) {
        if (notBlank(column) && !this.sqlSelect.contains(column)) {
            this.sqlSelect.add(column);
        }
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
    public void apply(KeyWordSegment keyWord, String column, SqlOp operator, String format, Object... args) {
        if (keyWord == null) {
            throw new FluentMybatisException("the first segment should be: 'AND', 'OR', 'GROUP BY', 'HAVING' or 'ORDER BY'");
        }
        String segment = operator.operator(this.getParameters(), format, args);
        this.getMergeSegments().add(keyWord, ColumnSegment.column(column), () -> segment);
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
     * @param functionSql 函数
     * @param values      变量列表
     * @return 参数化后的sql
     */
    public String paramSql(String functionSql, Object[] values) {
        return this.parameters.paramSql(functionSql, values);
    }

    /**
     * 更新column字段值
     *
     * @param column 字段
     * @param value  更新值
     */
    public void updateSet(String column, Object value) {
        this.updateSql(column, QUESTION_MARK, value);
    }

    /**
     * 设置更新（自定义SQL）
     *
     * @param column      更新的字段
     * @param functionSql set function sql
     * @param values      对应的参数
     */
    public void updateSql(String column, String functionSql, Object... values) {
        if (notBlank(functionSql)) {
            updates.put(column, this.paramSql(functionSql, values));
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
}