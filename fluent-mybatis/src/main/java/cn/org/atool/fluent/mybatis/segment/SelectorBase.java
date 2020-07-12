package cn.org.atool.fluent.mybatis.segment;

import cn.org.atool.fluent.mybatis.base.model.FieldMapping;
import cn.org.atool.fluent.mybatis.base.IQuery;
import cn.org.atool.fluent.mybatis.base.impl.BaseQuery;

import static cn.org.atool.fluent.mybatis.utility.MybatisUtil.isNotEmpty;

/**
 * BaseSelector: 查询字段构造
 *
 * @author darui.wu
 * @create 2020/6/21 3:13 下午
 */
public abstract class SelectorBase<
    S extends SelectorBase<S, Q>,
    Q extends IQuery<?, Q>
    >
    extends BaseSegment<SelectorApply<S, Q>, Q> {

    private final SelectorApply<S, Q> apply = new SelectorApply(this);

    protected SelectorBase(Q query) {
        super(query);
    }

    /**
     * 增加查询字段
     *
     * @param columns 查询字段
     * @return 查询字段选择器
     */
    public S apply(String... columns) {
        if (isNotEmpty(columns)) {
            this.getQuery().select(columns);
        }
        return (S) this;
    }

    @Override
    protected SelectorApply<S, Q> process(FieldMapping field) {
        return this.apply.setCurrentField(field);
    }

    private BaseQuery getQuery() {
        return (BaseQuery) this.wrapper;
    }
}