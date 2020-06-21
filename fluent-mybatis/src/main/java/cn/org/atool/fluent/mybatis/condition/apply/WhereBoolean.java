package cn.org.atool.fluent.mybatis.condition.apply;

import cn.org.atool.fluent.mybatis.condition.base.BaseWhere;
import cn.org.atool.fluent.mybatis.condition.base.Wrapper;
import cn.org.atool.fluent.mybatis.interfaces.IEntity;
import cn.org.atool.fluent.mybatis.interfaces.IQuery;

/**
 * AndBoolean
 *
 * @param <E> 对应的实体类
 * @param <W> 更新器或查询器
 * @param <Q> 对应的嵌套查询器
 * @author darui.wu
 */
public class WhereBoolean<E extends IEntity, W extends Wrapper<E, W, Q>, Q extends IQuery<E, Q>> extends WhereObject<E, Boolean, W, Q> {
    public WhereBoolean(BaseWhere queryAnd, String column, String property) {
        super(queryAnd, column, property);
    }

    /**
     * column = true
     *
     * @return self
     */
    public W isTrue() {
        return super.eq(true);
    }

    /**
     * column = false
     *
     * @return self
     */
    public W isFalse() {
        return super.eq(false);
    }
}