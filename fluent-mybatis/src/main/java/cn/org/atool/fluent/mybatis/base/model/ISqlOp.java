package cn.org.atool.fluent.mybatis.base.model;

import cn.org.atool.fluent.mybatis.segment.model.Parameters;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static cn.org.atool.fluent.mybatis.If.isEmpty;
import static cn.org.atool.fluent.mybatis.If.notBlank;
import static cn.org.atool.fluent.mybatis.mapper.StrConstant.STR_FORMAT;

public interface ISqlOp {
    /**
     * 操作参数个数
     *
     * @return
     */
    int getArgSize();

    /**
     * 注册新的自定义操作符
     *
     * @param sqlOp
     */
    static void register(ISqlOp sqlOp) {
        registers.add(sqlOp);
    }

    /**
     * 返回匹配的操作符实例
     *
     * @param op
     * @return
     */
    static ISqlOp get(String op) {
        try {
            return SqlOp.valueOf(op);
        } catch (IllegalArgumentException e) {
            for (ISqlOp item : registers) {
                if (Objects.equals(item.name(), op)) {
                    return item;
                }
            }
            throw e;
        }
    }

    List<ISqlOp> registers = new ArrayList<>();

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
    default String operator(Parameters parameters, String expression, Object... paras) {
        final String placeHolder = this.getPlaceHolder();
        String sql = placeHolder;
        if (notBlank(expression)) {
            sql = String.format(this.getFormat(), expression);
        } else if (placeHolder.contains(STR_FORMAT)) {
            sql = SqlOp.placeHolder(placeHolder, paras);
        }
        return isEmpty(paras) ? sql : parameters.paramSql(sql, paras);
    }

    String getFormat();

    String getPlaceHolder();

    String name();
}
