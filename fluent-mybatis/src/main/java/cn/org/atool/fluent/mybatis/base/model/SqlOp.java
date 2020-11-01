package cn.org.atool.fluent.mybatis.base.model;

import cn.org.atool.fluent.mybatis.segment.model.Parameters;
import lombok.Getter;

import java.util.Collection;
import java.util.stream.Stream;

import static cn.org.atool.fluent.mybatis.If.isEmpty;
import static cn.org.atool.fluent.mybatis.mapper.StrConstant.QUESTION_MARK;
import static cn.org.atool.fluent.mybatis.mapper.StrConstant.STR_FORMAT;
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
    IS_NULL("IS NULL", 0),
    /**
     * is not null
     */
    IS_NOT_NULL("IS NOT NULL", 0),
    /**
     * 等于
     */
    EQ("= ?", 1),
    /**
     * 不等于
     */
    NE("<> ?", 1),
    /**
     * 大于
     */
    GT("> ?", 1),
    /**
     * 大于等于
     */
    GE(">= ?", 1),
    /**
     * 小于
     */
    LT("< ?", 1),
    /**
     * 小于等于
     */
    LE("<= ?", 1),
    /**
     * 在...之间
     */
    BETWEEN("BETWEEN ? AND ?", 2),
    /**
     * 不在...之间
     */
    NOT_BETWEEN("NOT BETWEEN ? AND ?", 2),
    /**
     * like
     */
    LIKE("LIKE ?", 1),
    /**
     * not like
     */
    NOT_LIKE("NOT LIKE ?", 1),
    /**
     * 在...之中
     */
    IN("IN (%s)", -1),
    /**
     * 不在...之中
     */
    NOT_IN("NOT IN (%s)", -1),
    /**
     * 存在子查询有值
     */
    EXISTS("EXISTS (%s)", -1),
    /**
     * 不存在子查询有值
     */
    NOT_EXISTS("NOT EXISTS (%s)", -1),
    /**
     * 子查询
     */
    BRACKET("( %s )", -1),
    /**
     * 保留不动
     */
    RETAIN("%s", -1);

    private final String keyWord;
    /**
     * 参数个数
     * 0: 无can
     * -1: 不限定
     */
    @Getter
    private final int argSize;

    SqlOp(final String keyWord, int argSize) {
        this.keyWord = keyWord;
        this.argSize = argSize;
    }

    /**
     * sql 操作符
     *
     * @param parameters 查询语句中所有的变量
     * @param format     格式化SQL
     * @param paras      参数列表
     * @return sql片段
     */
    public String operator(Parameters parameters, String format, Object... paras) {
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