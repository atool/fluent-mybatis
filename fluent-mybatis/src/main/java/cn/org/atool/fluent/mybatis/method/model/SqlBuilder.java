package cn.org.atool.fluent.mybatis.method.model;

import cn.org.atool.fluent.mybatis.function.Executor;
import com.baomidou.mybatisplus.core.metadata.TableInfo;

import java.util.List;
import java.util.function.Consumer;

/**
 * Script
 *
 * @author darui.wu
 * @create 2020/5/14 3:39 下午
 */
public class SqlBuilder {
    public static final String COMMA = ",";

    public static final String NEWLINE = "\n";

    private final StringBuilder buff = new StringBuilder();

    private boolean endNewLine = false;

    private SqlBuilder() {
    }

    public static SqlBuilder instance() {
        return new SqlBuilder();
    }

    /**
     * 追加字符串
     *
     * @param format
     * @param args
     * @return
     */
    public SqlBuilder append(String format, Object... args) {
        if (args == null || args.length == 0) {
            buff.append(format);
        } else {
            buff.append(String.format(format, args));
        }
        this.endNewLine = false;
        return this;
    }

    public SqlBuilder append(Executor executor) {
        executor.execute();
        return this;
    }

    /**
     * 追加字符串, 但把format中单引号替换为双引号
     *
     * @param format
     * @param args
     * @return
     */
    private SqlBuilder quotas(String format, Object... args) {
        if (args == null || args.length == 0) {
            buff.append(format.replace('\'', '"'));
        } else {
            buff.append(String.format(format.replace('\'', '"'), args));
        }
        this.endNewLine = false;
        return this;
    }

    /**
     * 追加换行符
     *
     * @return
     */
    public SqlBuilder newLine() {
        if (!this.endNewLine) {
            buff.append(NEWLINE);
        }
        this.endNewLine = true;
        return this;
    }

    @Override
    public String toString() {
        return buff.toString();
    }

    /**
     * 标签: &lt;script>
     *
     * @return
     */
    public SqlBuilder beginScript() {
        return this.append("<script>").newLine();
    }

    /**
     * 标签: &lt;/script>
     *
     * @return
     */
    public String endScript() {
        return this.append("</script>").toString();
    }


    /**
     * update tableName
     *
     * @param table
     * @return
     */
    public SqlBuilder update(TableInfo table, boolean isSpec) {
        this.prefixComment(isSpec);
        this.newLine().append("UPDATE ");
        return this.byTable(table, isSpec);
    }

    private SqlBuilder byTable(TableInfo table, boolean isSpec) {
        if (isSpec) {
            this.choose("SPEC_TABLE != null and SPEC_TABLE != ''",
                "${SPEC_TABLE}",
                table.getTableName());
        } else {
            this.append(table.getTableName());
        }
        return this.newLine();
    }

    /**
     * insert into tableName
     *
     * @param table
     * @return
     */
    public SqlBuilder insert(TableInfo table, boolean isSpec) {
        this.prefixComment(isSpec);
        this.newLine().append("INSERT INTO ");
        return this.byTable(table, isSpec);
    }

    /**
     * delete from table
     *
     * @param table
     * @return
     */
    public SqlBuilder delete(TableInfo table, boolean isSpec) {
        this.prefixComment(isSpec);
        this.newLine().append("DELETE FROM ");
        return this.byTable(table, isSpec);
    }

    /**
     * select columns from table
     *
     * @param table
     * @param isSpec
     * @return
     */
    public SqlBuilder select(TableInfo table, boolean isSelected, boolean isSpec) {
        this.prefixComment(isSpec);
        this.newLine().append("SELECT ");
        if (isSelected) {
            this.choose("ew != null and ew.sqlSelect != null", "${ew.sqlSelect}", this.getSelectColumns(table));
        } else {
            this.append(this.getSelectColumns(table));
        }
        this.append(" FROM ");
        return this.byTable(table, isSpec);
    }

    public SqlBuilder selectCount(TableInfo table, boolean isSpec) {
        this.prefixComment(isSpec);
        this.append("SELECT COUNT(")
            .choose("ew != null and ew.sqlSelect != null", "${ew.sqlSelect}", "1")
            .append(") FROM ");
        return this.byTable(table, isSpec);
    }

    /**
     * SQL 查询所有表字段
     *
     * @param table 表信息
     * @return 查询字段列表
     */
    protected String getSelectColumns(TableInfo table) {
        /* 假设存在 resultMap 映射返回 */
        String selectColumns = "*";
        if (table.getResultMap() == null || (table.getResultMap() != null && table.isInitResultMap())) {
            selectColumns = table.getAllSqlSelect();
        }
        return selectColumns;
    }

    /**
     * 将list处理完毕后joining起来追加
     *
     * @param list     列表
     * @param consumer 映射函数
     * @return
     */
    public <T> SqlBuilder eachJoining(List<T> list, Consumer<T> consumer) {
        for (T item : list) {
            if (!this.endNewLine) {
                this.newLine();
            }
            consumer.accept(item);
        }
        return this;
    }

    /**
     * 直接设置 column = value
     *
     * @param format
     * @param propertyOrDefault
     * @param column
     * @return
     */
    public SqlBuilder value(String format, String propertyOrDefault, String column) {
        if (propertyOrDefault != null) {
            this.append(replace(format, propertyOrDefault, column));
        }
        return this;
    }

    /**
     * <pre>
     * &lt;if test="if_condition">
     *  executor.execute()
     * &lt;/if>
     * </pre>
     *
     * @param ifCondition
     * @param executor
     * @return
     */
    public SqlBuilder ifThen(String ifCondition, Executor executor) {
        this.newLine().quotas("<if test='%s'>", ifCondition);
        executor.execute();
        return this.append("</if>").newLine();
    }

    /**
     * <pre>
     * &lt;if test="if_condition">
     *  value
     * &lt;/if>
     * </pre>
     *
     * @param ifCondition
     * @param value
     * @return
     */
    public SqlBuilder ifThen(String ifCondition, String value) {
        return this.newLine()
            .quotas("<if test='%s'>", ifCondition)
            .append(value)
            .append("</if>").newLine();
    }

    public SqlBuilder ifThen(String conditionFormat, String valueFormat, String propertyOrDefault, String column) {
        if (propertyOrDefault == null) {
            return this;
        }
        return this.newLine()
            .quotas("<if test='%s'>", replace(conditionFormat, propertyOrDefault, column))
            .append(replace(valueFormat, propertyOrDefault, column))
            .append("</if>").newLine();
    }

    private String replace(String format, String propertyOrDefault, String column) {
        String value = format.replaceAll("@property", propertyOrDefault);
        if (column != null) {
            value = value.replaceAll("@column", column.trim());
        }
        return value;
    }

    /**
     * 当字段不为空时，插入字段值
     * 当字段为空时，插入默认值
     * <pre>
     * &lt;choose>
     * &lt;when test='%s != null'>%s,&lt;/when>
     * &lt;otherwise>%s,&lt;/otherwise>
     * &lt;/choose>
     * </pre>
     *
     * @param conditionFormat
     * @param valueFormat
     * @param defaultValue
     * @param property
     * @param column
     * @return
     */
    public SqlBuilder choose(String conditionFormat, String valueFormat, String defaultValue, String property, String column) {
        if (property == null) {
            return this;
        }
        this.newLine().append("<choose>").newLine()
            .quotas("<when test='%s'>", replace(conditionFormat, property, column))
            .append(replace(valueFormat, property, column))
            .append("</when>").newLine()
            .append("<otherwise>").append(defaultValue).append("</otherwise>").newLine()
            .append("</choose>").newLine();
        return this;
    }

    public SqlBuilder choose(String condition, String value, String defaultValue) {
        this.newLine().append("<choose>").newLine()
            .quotas("<when test='%s'>", condition).append(value).append("</when>").newLine()
            .append("<otherwise>").append(defaultValue).append("</otherwise>").newLine()
            .append("</choose>").newLine();
        return this;
    }

    /**
     * mybatis变量: #{param}
     *
     * @param param
     * @return
     */
    public static String safeParam(final String param) {
        return "#{" + param + "}";
    }

    /**
     * 标签set
     * <pre>
     * &lt;set>
     *  sql fragment
     * &lt;/set>
     * </pre>
     *
     * @param executor
     * @return
     */
    public SqlBuilder set(Executor executor) {
        this.append("<set>").newLine();
        executor.execute();
        return this.newLine().append("</set>").newLine();
    }

    /**
     * 标签where
     * <pre>
     * &lt;where>
     *  sql fragment
     * &lt;/where>
     * </pre>
     *
     * @param executor
     * @return
     */
    public SqlBuilder where(Executor executor) {
        this.append("<where>").newLine();
        executor.execute();
        return this.newLine().append("</where>").newLine();
    }

    /**
     * 标签： &lt;foreach collection="{}" item="{}" index="index" separator="{}">
     *
     * @param collection
     * @param item
     * @param separator
     * @return
     */
    public SqlBuilder foreach(String collection, String item, String separator, Executor executor) {
        this.quotas("<foreach collection='%s' item='%s' index='k' separator='%s'>", collection, item, separator).newLine();
        executor.execute();
        return this.newLine().append("</foreach>").newLine();
    }

    /**
     * 标签
     * <pre>
     * &lt;trim prefix="{}" suffix="{}" suffixOverrides="{}">
     *  sql fragment
     * &lt;/trim>
     * </pre>
     *
     * @param prefix
     * @param suffix
     * @param suffixOverrides
     * @param executor
     * @return
     */
    public SqlBuilder trim(String prefix, String suffix, String suffixOverrides, Executor executor) {
        this.quotas("<trim prefix='%s' suffix='%s' suffixOverrides='%s'>", prefix, suffix, suffixOverrides).newLine();
        executor.execute();
        return this.newLine().append("</trim>").newLine();
    }

    /**
     * (value1, value2, value3)
     *
     * @param executor
     * @return
     */
    public SqlBuilder brackets(Executor executor) {
        return this.trim("(", ")", ",", executor);
    }

    public SqlBuilder brackets(String values) {
        return this.trim("(", ")", ",", () -> this.append(values));
    }

    /**
     * 方法前注释
     *
     * @param isSpec 是否增加方法注释
     * @return
     */
    private SqlBuilder prefixComment(boolean isSpec) {
        if (isSpec) {
            this.newLine().ifThen("SPEC_COMMENT != null and SPEC_COMMENT != ''", "${SPEC_COMMENT}").newLine();
        }
        return this;
    }


    /**
     * 方法后注释
     *
     * @return
     */
    public SqlBuilder suffixComment() {
        return this.ifThen("ew != null and ew.sqlComment != null", "${ew.sqlComment}");
    }
}