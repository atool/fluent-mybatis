package cn.org.atool.fluent.mybatis.base.model;

import cn.org.atool.fluent.mybatis.segment.fragment.CachedFrag;
import cn.org.atool.fluent.mybatis.segment.fragment.IFragment;
import cn.org.atool.fluent.mybatis.segment.model.Parameters;

import static cn.org.atool.fluent.mybatis.If.*;
import static cn.org.atool.fluent.mybatis.segment.fragment.Fragments.SEG_EMPTY;
import static cn.org.atool.fluent.mybatis.utility.StrConstant.STR_FORMAT;

/**
 * 操作符定义接口类
 *
 * @author wudarui
 */
public interface ISqlOp {
    /**
     * 操作符名称
     *
     * @return 名称
     */
    String name();

    /**
     * 操作符字符拼接表达式
     *
     * @return 字符串格式化表达式
     */
    String getExpression();

    /**
     * 操作符参数占位符表达式
     *
     * @return 预编译表达式
     */
    String getPlaceHolder();

    /**
     * 操作参数个数
     *
     * @return 参数个数
     */
    int getArgSize();

    /**
     * sql 操作符
     * 如果自定义函数expression不为空, 则按自定义函数形式处理
     * 如果无自定义函数, 且是不定项参数方式(placeHolder中有%s), 则先处理不定项参数项为占位符'?'
     * <p/>
     * 最后根据占位符'?'和参数值, 给每个'?'分配具体的表达式项
     *
     * @param column     映射字段, 如果 = null, 表示非原始字段赋值
     * @param parameters 查询语句中所有的变量
     * @param expression 自定义函数或SQL片段
     * @param paras      参数列表
     * @return sql片段
     */
    default IFragment operator(IFragment column, Parameters parameters, String expression, Object... paras) {
        if (!column.notEmpty() && isBlank(expression) && isEmpty(paras)) {
            return SEG_EMPTY;
        }
        final String placeHolder = this.getPlaceHolder();
        String sql = placeHolder;
        if (notBlank(expression)) {
            sql = String.format(this.getExpression(), expression);
        } else if (placeHolder.contains(STR_FORMAT)) {
            sql = SqlOp.placeHolder(placeHolder, paras);
        }
        return CachedFrag.set(isEmpty(paras) ? sql : parameters.paramSql(column, sql, paras));
    }

    default IFragment operator(IFragment column, Parameters parameters, IFragment expression, Object... paras) {
        return m -> this.operator(column, parameters, expression.get(m), paras).get(m);
    }
}