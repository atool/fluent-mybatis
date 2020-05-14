package cn.org.atool.fluent.mybatis.method.model;

import java.util.List;
import java.util.Objects;
import java.util.function.Function;

import static java.util.stream.Collectors.joining;

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
        buff.append(NEWLINE);
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
        buff.append(NEWLINE);
        return this;
    }

    /**
     * 追加换行符
     *
     * @return
     */
    public SqlBuilder newLine() {
        buff.append(NEWLINE);
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
        return this.append("<script>");
    }

    /**
     * 标签: &lt;/script>
     *
     * @return
     */
    public SqlBuilder endScript() {
        return this.append("</script>");
    }

    /**
     * 标签: &lt;trim prefix="{}" suffix="{}" suffixOverrides="{}">
     *
     * @param prefix
     * @param suffix
     * @param suffixOverrides
     * @return
     */
    public SqlBuilder beginTrim(String prefix, String suffix, String suffixOverrides) {
        return this.quotas("<trim prefix='%s' suffix='%s' suffixOverrides='%s'>", prefix, suffix, suffixOverrides);
    }

    /**
     * 标签: &lt;/trim>
     *
     * @return
     */
    public SqlBuilder endTrim() {
        return this.append("</trim>");
    }

    /**
     * 标签： &lt;foreach collection="{}" item="{}" index="index" separator="{}">
     *
     * @param collection
     * @param item
     * @param separator
     * @return
     */
    public SqlBuilder beginForeach(String collection, String item, String separator) {
        return this.quotas("<foreach collection='%s' item='%s' index='index' separator='%s'>", collection, item, separator);
    }

    /**
     * 标签: &lt;/foreach>
     *
     * @return
     */
    public SqlBuilder endForeach() {
        return this.append("</foreach>");
    }

    /**
     * 标签: &lt;set>
     *
     * @return
     */
    public SqlBuilder beginSet() {
        return this.append("<set>");
    }

    /**
     * 标签: &lt;/set>
     *
     * @return
     */
    public SqlBuilder endSet() {
        return this.append("</set>");
    }

    /**
     * update tableName
     *
     * @param tableName
     * @return
     */
    public SqlBuilder update(String tableName) {
        return this.append("UPDATE ").append(tableName);
    }

    /**
     * insert into tableName
     *
     * @param tableName
     * @return
     */
    public SqlBuilder insert(String tableName) {
        return this.append("INSERT INTO ").append(tableName);
    }

    /**
     * where
     *
     * @return
     */
    public SqlBuilder beginWhere() {
        return this.append("<where>");
    }


    /**
     * where
     *
     * @return
     */
    public SqlBuilder endWhere() {
        return this.append("</where>");
    }

    /**
     * 将list处理完毕后joining起来追加
     *
     * @param list      列表
     * @param apply     映射函数
     * @param delimiter 分割符
     * @return
     */
    public <T> SqlBuilder joinEach(List<T> list, Function<T, String> apply, String delimiter) {
        String value = list.stream().map(apply::apply).filter(Objects::nonNull).collect(joining(delimiter));
        return this.append(value);
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

    final static String VALUE_IF_NOT_NULL = "<if test='%s != null'>%s,</if>";

    /**
     * <pre>
     * &lt;if test="property != null>
     *  value,
     * &lt;/if>
     * </pre>
     *
     * @param prefix
     * @param property
     * @param value
     * @return
     */
    public SqlBuilder ifValue(String prefix, String property, String value) {
        return this.quotas(VALUE_IF_NOT_NULL,
            prefix + property,
            value);
    }

    final static String SET_IF_NOT_NULL = "<if test='%s != null'>%s=#{%s},</if>";

    /**
     * <pre>
     * &lt;if test="prefix.property != null>
     *  column=#{prefix.property},
     * &lt;/if>
     * </pre>
     *
     * @param prefix
     * @param property
     * @param column
     * @return
     */
    public SqlBuilder ifSet(String prefix, String property, String column) {
        return this.quotas(SET_IF_NOT_NULL,
            prefix + property,
            column,
            prefix + property
        );
    }

    final static String AND_IF_NOT_NULL = "<if test='%s != null'>AND %s=#{%s}</if>";

    /**
     * <pre>
     * &lt;if test="prefix.property != null>
     *  and column=#{prefix.property}
     * &lt;/if>
     * </pre>
     *
     * @param prefix
     * @param property
     * @param column
     * @return
     */
    public SqlBuilder ifAnd(String prefix, String property, String column) {
        return this.quotas(AND_IF_NOT_NULL,
            prefix + property,
            column,
            prefix + property
        );
    }

    final static String CHOOSE_NOTNULL_OR_DEFAULT =
        "<choose><when test='%s != null'>%s,</when><otherwise>%s,</otherwise></choose>";

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
     * @param prefix
     * @param property
     * @param defaultValue
     * @return
     */
    public SqlBuilder choose(String prefix, String property, String defaultValue) {
        return this.quotas(CHOOSE_NOTNULL_OR_DEFAULT,
            prefix + property,
            safeParam(prefix + property),
            defaultValue
        );
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
}