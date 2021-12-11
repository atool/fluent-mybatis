package cn.org.atool.fluent.mybatis.segment.fragment;

import cn.org.atool.fluent.mybatis.base.entity.IMapping;

import static cn.org.atool.fluent.mybatis.segment.fragment.Fragments.SEG_EMPTY;
import static cn.org.atool.fluent.mybatis.utility.StrConstant.EMPTY;

/**
 * 对IFragment对象进行缓存
 *
 * @author wudarui
 */
public class CachedFrag implements IFragment {
    private String cached;

    private final IFragment segment;

    private CachedFrag(String cached) {
        this.cached = cached;
        this.segment = SEG_EMPTY;
    }

    protected CachedFrag(IFragment segment) {
        this.segment = segment == null ? SEG_EMPTY : segment;
    }

    @Override
    public boolean notEmpty() {
        if (this.cached != null) {
            return !EMPTY.equals(this.cached);
        } else {
            return this.segment.notEmpty();
        }
    }

    @Override
    public String get(IMapping mapping) {
        if (this.cached == null) {
            this.cached = segment.get(mapping);
        }
        return this.cached;
    }

    @Override
    public String toString() {
        return this.cached == null ? this.segment.toString() : this.cached;
    }

    public static CachedFrag set(String column) {
        return new CachedFrag(column);
    }

    public static CachedFrag set(IFragment column) {
        return new CachedFrag(column);
    }
}