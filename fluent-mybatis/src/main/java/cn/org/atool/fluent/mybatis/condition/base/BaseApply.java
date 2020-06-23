package cn.org.atool.fluent.mybatis.condition.base;

import cn.org.atool.fluent.mybatis.annotation.FieldMeta;
import cn.org.atool.fluent.mybatis.interfaces.IWrapper;

/**
 * BaseApply
 *
 * @param <SEGMENT> BaseApply子类
 * @param <W>       查询（更新）器
 * @author darui.wu
 */
public abstract class BaseApply<
    SEGMENT extends BaseSegment,
    W extends IWrapper<?, W, ?>
    > {

    protected final SEGMENT segment;
    /**
     * 当前被操作的字段
     */
    protected FieldMeta current;

    BaseApply(SEGMENT segment) {
        this.segment = segment;
    }

    /**
     * 设置当前被操作的字段
     *
     * @param current 字段定义
     * @param <APPLY> 操作者类型
     * @return 返回操作自身
     */
    <APPLY extends BaseApply<SEGMENT, W>> APPLY setCurrentField(FieldMeta current) {
        this.current = current;
        return (APPLY) this;
    }
}