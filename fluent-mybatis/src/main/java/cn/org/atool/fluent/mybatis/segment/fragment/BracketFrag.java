package cn.org.atool.fluent.mybatis.segment.fragment;

import cn.org.atool.fluent.mybatis.base.crud.IQuery;
import cn.org.atool.fluent.mybatis.base.entity.IMapping;

import static cn.org.atool.fluent.mybatis.mapper.MapperSql.brackets;

/**
 * 给segment对象加上()
 *
 * @author darui.wu
 */
@SuppressWarnings("rawtypes")
public class BracketFrag implements IFragment {
    private final IFragment fragment;

    private BracketFrag(IFragment fragment) {
        this.fragment = fragment;
    }

    @Override
    public String get(IMapping mapping) {
        return "(" + fragment.get(mapping) + ")";
    }

    @Override
    public String toString() {
        return brackets(this.fragment);
    }

    public static BracketFrag set(IFragment fragment) {
        return fragment instanceof BracketFrag ? (BracketFrag) fragment : new BracketFrag(fragment);
    }

    public static BracketFrag set(IQuery query) {
        return new BracketFrag(query.data().sql(true));
    }

    public static BracketFrag set(String segment) {
        return new BracketFrag(CachedFrag.set(segment));
    }
}