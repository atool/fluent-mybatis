package cn.org.atool.fluent.mybatis.condition;

import cn.org.atool.fluent.mybatis.condition.interfaces.ISqlSegment;
import cn.org.atool.fluent.mybatis.condition.segments.MergeSegments;

/**
 * 条件构造抽象类
 *
 * @author darui.wu
 */
public abstract class Wrapper<T> implements ISqlSegment {

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