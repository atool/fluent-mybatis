package cn.org.atool.fluent.mybatis.base;

import com.baomidou.mybatisplus.core.conditions.AbstractWrapper;

/**
 * BaseQueryAnd
 *
 * @author darui.wu
 * @create 2020/6/4 5:39 下午
 */
@Deprecated
public class BaseQueryAnd<W extends AbstractWrapper> extends cn.org.atool.fluent.mybatis.condition.base.BaseQueryAnd<W> {
    protected BaseQueryAnd(W wrapper) {
        super(wrapper);
    }
}