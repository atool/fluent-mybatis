package cn.org.atool.fluent.mybatis.base.model;

import cn.org.atool.fluent.mybatis.segment.model.Parameters;
import lombok.Getter;

import java.util.Collection;
import java.util.stream.Stream;

import static cn.org.atool.fluent.mybatis.If.isEmpty;
import static cn.org.atool.fluent.mybatis.If.notBlank;
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
    NOT_NULL("IS NOT NULL", 0),
    /**
     * 等于
     */
    EQ("= ?", "= %s", 1),
    /**
     * 不等于
     */
    NE("<> ?", "<> %s", 1),
    /**
     * 大于
     */
    GT("> ?", "> %s", 1),
    /**
     * 大于等于
     */
    GE(">= ?", ">= %s", 1),
    /**
     * 小于
     */
    LT("< ?", "< %s", 1),
    /**
     * 小于等于
     */
    LE("<= ?", "<= %s", 1),
    /**
     * 在...之间
     */
    BETWEEN("BETWEEN ? AND ?", "BETWEEN %s", 2),
    /**
     * 不在...之间
     */
    NOT_BETWEEN("NOT BETWEEN ? AND ?", "NOT BETWEEN %s", 2),
    /**
     * like
     */
    LIKE("LIKE ?", "LIKE %s", 1),
    /**
     * not like
     */
    NOT_LIKE("NOT LIKE ?", "NOT LIKE %s", 1),
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

    private final String placeHolder;

    private final String format;
    /**
     * 参数个数
     * 0: 无can
     * -1: 不限定
     */
    @Getter
    private final int argSize;

    SqlOp(final String placeHolder, int argSize) {
        this.placeHolder = placeHolder;
        this.format = placeHolder;
        this.argSize = argSize;
    }

    SqlOp(final String placeHolder, String format, int argSize) {
        this.placeHolder = placeHolder;
        this.format = format;
        this.argSize = argSize;
    }

    /**
     * sql 操作符
     * 如果自定义函数expression不为空, 则按自定义函数形式处理
     * 如果无自定义函数, 且是不定项参数方式(placeHolder中有%s), 则先处理不定项参数项为占位符'?'
     * <p/>
     * 最后根据占位符'?'和参数值, 给每个'?'分配具体的表达式项
     *
     * @param parameters 查询语句中所有的变量
     * @param expression 自定义函数或SQL片段
     * @param paras      参数列表
     * @return sql片段
     */
    public String operator(Parameters parameters, String expression, Object... paras) {
        String sql = this.placeHolder;
        if (notBlank(expression)) {
            sql = String.format(this.format, expression);
        } else if (this.placeHolder.contains(STR_FORMAT)) {
            sql = this.setPlaceHolder(paras);
        }
        return isEmpty(paras) ? sql : parameters.paramSql(sql, paras);
    }

    /**
     * 根据参数个数多少, 将"%s"替换为"?, ?"占位符串
     *
     * @param values 参数列表
     * @return sql片段
     */
    private String setPlaceHolder(Object... values) {
        String placeHolder = "";
        if (values.length == 1 && values[0] instanceof Collection) {
            Collection list = (Collection) values[0];
            placeHolder = (String) list.stream().map(v -> QUESTION_MARK).collect(joining(", "));
        } else {
            placeHolder = Stream.of(values).map(v -> QUESTION_MARK).collect(joining(", "));
        }
        return String.format(this.placeHolder, placeHolder);
    }
}