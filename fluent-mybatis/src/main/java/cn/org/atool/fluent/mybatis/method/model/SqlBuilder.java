package cn.org.atool.fluent.mybatis.method.model;

import cn.org.atool.fluent.mybatis.base.Executor;
import cn.org.atool.fluent.mybatis.method.metadata.TableMeta;

import java.util.List;
import java.util.function.Consumer;

import static cn.org.atool.fluent.mybatis.segment.model.StrConstant.*;
import static cn.org.atool.fluent.mybatis.utility.SqlProviderUtils.*;

/**
 * Script
 *
 * @author darui.wu
 * @create 2020/5/14 3:39 下午
 */
public class SqlBuilder {
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
    public SqlBuilder quotas(String format, Object... args) {
        if (args == null || args.length == 0) {
            buff.append(replace(format));
        } else {
            buff.append(String.format(replace(format), args));
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
     * update tableName
     *
     * @param table
     * @return
     */
    public SqlBuilder update(TableMeta table, boolean isSpec) {
        this.prefixComment(isSpec);
        this.newLine().append("UPDATE ");
        return this.byTable(table, isSpec);
    }

    private SqlBuilder byTable(TableMeta table, boolean isSpec) {
        if (isSpec) {
            this.choose("SPEC_TABLE != null and SPEC_TABLE != ''",
                "${SPEC_TABLE}",
                table.getTableName());
        } else {
            this.append(table.getTableName());
        }
        return this.newLine();
    }

    public SqlBuilder begin(StatementType statementType, String statementId, Class parameterType) {
        return this.begin(statementType, statementId, parameterType, null);
    }

    public SqlBuilder begin(StatementType statementType, String statementId, Class parameterType, Class resultType) {
        switch (statementType) {
            case delete:
            case insert:
            case update:
                return this.quotas("<%s id='%s' parameterType='%s'>", statementType.name(), statementId, parameterType.getName())
                    .newLine();
            case select:
            default:
                if (resultType == null) {
                    return this.quotas("<%s id='%s' parameterType='%s' resultMap='BaseResultMap'>", statementType.name(), statementId, parameterType.getName())
                        .newLine();
                } else {
                    return this.quotas("<%s id='%s' parameterType='%s' resultType='%s'>", statementType.name(), statementId, parameterType.getName(), resultType.getName())
                        .newLine();
                }
        }
    }

    public SqlBuilder end(StatementType statementType) {
        return this.newLine().append("</%s>", statementType.name());
    }

    /**
     * insert into tableName
     *
     * @param table  表结构
     * @param isSpec 是否指定表名
     * @return SqlBuilder
     */
    public SqlBuilder insert(TableMeta table, boolean isSpec) {
        this.prefixComment(isSpec);
        this.newLine().append("INSERT INTO ");
        return this.byTable(table, isSpec);
    }

    /**
     * delete from table
     *
     * @param table  表结构
     * @param isSpec 是否指定表名
     * @return SqlBuilder
     */
    public SqlBuilder delete(TableMeta table, boolean isSpec) {
        this.prefixComment(isSpec);
        this.newLine().append("DELETE FROM ");
        return this.byTable(table, isSpec);
    }

    /**
     * select columns from table
     *
     * @param table     表结构
     * @param isWrapper 是否是自定义的wrapper参数
     * @param isSpec    是否指定表名
     * @return SqlBuilder
     */
    public SqlBuilder select(TableMeta table, boolean isWrapper, boolean isSpec) {
        this.prefixComment(isSpec);
        this.newLine().append("SELECT ");
        if (isWrapper) {
            this.ifThen(Wrapper_Distinct_True, DISTINCT);
            this.choose(Wrapper_Select_Not_Null, Wrapper_Select_Var, replace("<include refid='SELECT_COLUMNS'/>"));
        } else {
            this.quotas("<include refid='SELECT_COLUMNS'/>");
        }
        this.append(" FROM ");
        return this.byTable(table, isSpec);
    }

    /**
     * select count(*)
     *
     * @param table  表结构
     * @param isSpec 是否指定表名
     * @return SqlBuilder
     */
    public SqlBuilder selectCount(TableMeta table, boolean isSpec) {
        this.prefixComment(isSpec);
        this.append("SELECT COUNT(")
            .choose(Wrapper_Select_Not_Null, Wrapper_Select_Var, ASTERISK)
            .append(") FROM ");
        return this.byTable(table, isSpec);
    }

    /**
     * 替换单引号为双引号
     *
     * @param input
     * @return
     */
    private static String replace(String input) {
        return input.replace('\'', '"');
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
     * 强制检查wrapper对象不空, 否则报错
     *
     * @return
     */
    public SqlBuilder checkWrapper() {
        this.ifThen(Wrapper_Exists, () -> {
        });
        return this;
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
            this.newLine().ifThen(Spec_Comment_Not_Null, "${SPEC_COMMENT}").newLine();
        }
        return this;
    }

    /**
     * 有分页和无分页处理分开
     *
     * @param noPagedXml   无分页语句
     * @param withPagedXml 有分页语句
     * @return
     */
    public SqlBuilder choosePaged(String noPagedXml, String withPagedXml) {
        this.choose(Wrapper_Page_Is_Null, noPagedXml, withPagedXml);
        return this;
    }

    /**
     * 直接在sql中添加 limit x, y 语句
     *
     * @return
     */
    public SqlBuilder limitDirectly() {
        return this.ifThen(Wrapper_Page_Not_Null,
            () -> this.append(" LIMIT %s, %s ", Wrapper_Paged_Offset, Wrapper_Paged_Size));
    }
}