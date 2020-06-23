package cn.org.atool.fluent.mybatis.interfaces.model;

import cn.org.atool.fluent.mybatis.segment.model.ParameterPair;

import java.util.Collection;
import java.util.stream.Stream;

import static cn.org.atool.fluent.mybatis.segment.model.StrConstant.QUESTION_MARK;
import static cn.org.atool.fluent.mybatis.segment.model.StrConstant.STR_FORMAT;
import static cn.org.atool.fluent.mybatis.utility.MybatisUtil.isEmpty;
import static java.util.stream.Collectors.joining;

/**
 * SQL 操作符
 *
 * @author darui.wu
 */
public enum SqlOp {
    /**
     * is null
     */
    IS_NULL("IS NULL"),
    /**
     * is not null
     */
    IS_NOT_NULL("IS NOT NULL"),
    /**
     * 等于
     */
    EQ("= ?"),
    /**
     * 不等于
     */
    NE("<> ?"),
    /**
     * 大于
     */
    GT("> ?"),
    /**
     * 大于等于
     */
    GE(">= ?"),
    /**
     * 小于
     */
    LT("< ?"),
    /**
     * 小于等于
     */
    LE("<= ?"),
    /**
     * 在...之间
     */
    BETWEEN("BETWEEN ? AND ?"),
    /**
     * 不在...之间
     */
    NOT_BETWEEN("NOT BETWEEN ? AND ?"),
    /**
     * like
     */
    LIKE("LIKE ?"),
    /**
     * not like
     */
    NOT_LIKE("NOT LIKE ?"),
    /**
     * 在...之中
     */
    IN("IN (%s)"),
    /**
     * 不在...之中
     */
    NOT_IN("NOT IN (%s)"),
    /**
     * 存在子查询有值
     */
    EXISTS("EXISTS (%s)"),
    /**
     * 不存在子查询有值
     */
    NOT_EXISTS("NOT EXISTS (%s)"),
    /**
     * 子查询
     */
    BRACKET("( %s )"),
    /**
     * 保留不动
     */
    RETAIN("%s");

    private final String keyWord;

    SqlOp(final String keyWord) {
        this.keyWord = keyWord;
    }

    /**
     * sql 操作符
     *
     * @param parameters 查询语句中所有的变量
     * @param format     格式化SQL
     * @param paras      参数列表
     * @return sql片段
     */
    public String operator(ParameterPair parameters, String format, Object... paras) {
        String sql = this.keyWord;
        if (format != null) {
            sql = String.format(this.keyWord, format);
        } else if (this.keyWord.contains(STR_FORMAT)) {
            sql = this.placeHolder(paras);
        }
        if (isEmpty(paras)) {
            return sql;
        } else {
            return parameters.paramSql(sql, paras);
        }
    }

    /**
     * 占位符形式格式化
     *
     * @param values 参数列表
     * @return sql片段
     */
    private String placeHolder(Object... values) {
        String placeHolder = "";
        if (values.length == 1 && values[0] instanceof Collection) {
            Collection list = (Collection) values[0];
            placeHolder = (String) list.stream().map(v -> QUESTION_MARK).collect(joining(", "));
        } else {
            placeHolder = Stream.of(values).map(v -> QUESTION_MARK).collect(joining(", "));
        }
        return String.format(this.keyWord, placeHolder);
    }
}