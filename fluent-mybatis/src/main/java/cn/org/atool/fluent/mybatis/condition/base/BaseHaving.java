package cn.org.atool.fluent.mybatis.condition.base;

import cn.org.atool.fluent.mybatis.condition.model.KeyWordSegment;
import cn.org.atool.fluent.mybatis.condition.model.SqlOp;
import cn.org.atool.fluent.mybatis.interfaces.IQuery;

import static cn.org.atool.fluent.mybatis.condition.model.Constants.EMPTY;
import static cn.org.atool.fluent.mybatis.condition.model.KeyWordSegment.HAVING;
import static cn.org.atool.fluent.mybatis.condition.model.SqlOp.RETAIN;

/**
 * BaseHaving: having设置
 *
 * @author darui.wu
 * @create 2020/6/21 6:22 下午
 */
public abstract class BaseHaving<H extends BaseHaving<H>> {

    private IQuery query;

    protected BaseHaving(IQuery query) {
        this.query = query;
    }

    /**
     * 设置having条件
     *
     * @param function having函数
     * @param op       比较操作
     * @param args     参数列表
     * @return Having设置器
     */
    public H apply(String function, SqlOp op, Object... args) {
        this.query.apply(KeyWordSegment.HAVING, function, op, args);
        return (H) this;
    }

    /**
     * HAVING ( sql语句 )
     * <p>例1: having("sum(age) &gt; 10")</p>
     * <p>例2: having("sum(age) &gt; ?", 10)</p>
     *
     * @param function having sql 语句
     * @param args     函数参数列表
     * @return Having设置器
     */
    public H apply(String function, Object... args) {
        this.query.apply(HAVING, EMPTY, this.query.getParameters().paramSql(function, args), RETAIN);
        return (H) this;
    }
}