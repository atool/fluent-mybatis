package cn.org.atool.fluent.mybatis.base;


import cn.org.atool.fluent.mybatis.condition.interfaces.Update;

/**
 * BaseUpdateSet
 *
 * @author darui.wu
 */
@Deprecated
public class BaseUpdateSet<U extends Update> extends cn.org.atool.fluent.mybatis.condition.base.BaseUpdateSet<U> {
    protected BaseUpdateSet(U wrapper) {
        super(wrapper);
    }
}