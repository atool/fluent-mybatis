package cn.org.atool.fluent.mybatis.base;

import com.baomidou.mybatisplus.core.conditions.AbstractWrapper;

/**
 * BaseWrapperOrder
 *
 * @author darui.wu
 * @create 2020/6/4 5:56 下午
 */
@Deprecated
public abstract class BaseWrapperOrder<W extends AbstractWrapper> extends cn.org.atool.fluent.mybatis.condition.base.BaseWrapperOrder<W> {
    protected BaseWrapperOrder(W query) {
        super(query);
    }
}