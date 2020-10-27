package cn.org.atool.fluent.mybatis.segment;

import cn.org.atool.fluent.mybatis.base.IWrapper;
import cn.org.atool.fluent.mybatis.base.model.FieldMapping;

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
     *
     * @return
     */
    public FieldMapping current() {
        return this.segment.current;
    }

    BaseApply(SEGMENT segment) {
        this.segment = segment;
    }
}