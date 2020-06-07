package cn.org.atool.fluent.mybatis.condition.base;

import cn.org.atool.fluent.mybatis.condition.base.MergeSegments;
import cn.org.atool.fluent.mybatis.condition.interfaces.ISqlSegment;

/**
 * 条件构造抽象类
 *
 * @author darui.wu
 */
public abstract class Wrapper<T> implements ISqlSegment, IProperty2Column {

    /**
     * 实体对象（子类实现）
     *
     * @return 泛型 T
     */
    public abstract T getEntity();

    public String getSqlSet() {
        return null;
    }

    public String getSqlComment() {
        return null;
    }

    /**
     * 获取 MergeSegments
     *
     * @return
     */
    public abstract MergeSegments getExpression();
}