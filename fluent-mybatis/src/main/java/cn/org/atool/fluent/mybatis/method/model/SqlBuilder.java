package cn.org.atool.fluent.mybatis.method.model;

import cn.org.atool.fluent.mybatis.function.Executor;

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
     * @param tableName
     * @return
     */
    public SqlBuilder update(String tableName) {
        return this.newLine().append("UPDATE ").append(tableName).newLine();
    }

    /**
     * insert into tableName
     *
     * @param tableName
     * @return
     */
    public SqlBuilder insert(String tableName) {
        return this.newLine().append("INSERT INTO ").append(tableName).newLine();
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
     * 设置 column_name = #{entity.property}
     *
     * @param column
     * @param prefix
     * @param property
     * @return
     */
    public SqlBuilder setVariable(String column, String prefix, String property) {
        return this.append("%s=#{%s},", column, prefix + property);
    }

    /**
     * 直接设置 column = value
     *
     * @param column
     * @param value
     * @return
     */
    public SqlBuilder setValue(String column, String value) {
        return this.append("%s=%s,", column, value);
    }

    /**
     * 设置 column_name = #{entity.property}
     *
     * @param column
     * @param prefix
     * @param property
     * @return
     */
    public SqlBuilder andVariable(String column, String prefix, String property) {
        return this.append("AND %s=#{%s} ", column, prefix + property);
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
        this.newLine();
        executor.execute();
        this.newLine();
        return this.newLine().append("</if>");
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
        this.newLine().quotas("<if test='%s'>", ifCondition);
        this.newLine().append(value).newLine();
        return this.newLine().append("</if>");
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
     * @param ifCondition
     * @param value
     * @param defaultValue
     * @return
     */
    public SqlBuilder choose(String ifCondition, String value, String defaultValue) {
        this.newLine().append("<choose>").newLine()
            .quotas("<when test='%s'>", ifCondition).newLine()
            .append(value).newLine()
            .append("</when>").newLine()
            .append("<otherwise>").newLine()
            .append(defaultValue).newLine()
            .append("</otherwise>").newLine()
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
        this.quotas("<foreach collection='%s' item='%s' index='index' separator='%s'>", collection, item, separator).newLine();
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
}