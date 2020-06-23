package cn.org.atool.fluent.mybatis.condition;

import cn.org.atool.fluent.mybatis.annotation.FieldMeta;
import cn.org.atool.fluent.mybatis.condition.model.KeyWordSegment;
import cn.org.atool.fluent.mybatis.condition.model.SqlOp;
import cn.org.atool.fluent.mybatis.interfaces.IQuery;

import static cn.org.atool.fluent.mybatis.condition.model.KeyWordSegment.HAVING;
import static cn.org.atool.fluent.mybatis.condition.model.SqlOp.RETAIN;
import static cn.org.atool.fluent.mybatis.condition.model.StrConstant.EMPTY;

/**
 * BaseHaving: having设置
 *
 * @author darui.wu
 * @create 2020/6/21 6:22 下午
 */
public abstract class HavingBase<
    H extends HavingBase<H, Q>,
    Q extends IQuery<?, Q>
    >
    extends BaseSegment<HavingApply<H, Q>, Q> {

    private final HavingApply<H, Q> apply = new HavingApply<>((H) this);

    protected HavingBase(Q query) {
        super(query);
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
        this.wrapper.getWrapperData().apply(KeyWordSegment.HAVING, function, op, args);
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
        this.wrapper.getWrapperData().apply(HAVING, EMPTY, this.wrapper.getWrapperData().paramSql(function, args), RETAIN);
        return (H) this;
    }

    @Override
    public HavingApply<H, Q> set(FieldMeta field) {
        return this.apply.setCurrentField(field);
    }
}