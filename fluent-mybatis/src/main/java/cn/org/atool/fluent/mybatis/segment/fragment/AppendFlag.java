package cn.org.atool.fluent.mybatis.segment.fragment;

import cn.org.atool.fluent.mybatis.base.entity.IMapping;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static cn.org.atool.fluent.mybatis.utility.StrConstant.EMPTY;

/**
 * 追加Fragment
 *
 * @author darui.wu
 */
public class AppendFlag implements IFragment {
    private String cached;

    private final List<Object> list = new ArrayList<>();

    public static AppendFlag set(IFragment frag) {
        return frag instanceof AppendFlag ? (AppendFlag) frag : new AppendFlag().append(frag);
    }

    public AppendFlag() {
    }

    public AppendFlag append(Object... args) {
        for (Object o : args) {
            if (this.allowed(o)) {
                this.list.add(o);
            }
        }
        return this;
    }

    public AppendFlag append(String text, int from, int to) {
        this.list.add(text.substring(from, to));
        return this;
    }

    @Override
    public String get(IMapping mapping) {
        if (this.cached == null) {
            this.cached = list.stream().map(o -> {
                if (o instanceof IFragment) {
                    return ((IFragment) o).get(mapping);
                } else {
                    return String.valueOf(o);
                }
            }).collect(Collectors.joining());
        }
        return this.cached;
    }

    @Override
    public boolean notEmpty() {
        return !this.list.isEmpty();
    }

    private boolean allowed(Object o) {
        if (o instanceof IFragment) {
            return ((IFragment) o).notEmpty();
        } else if (o instanceof String) {
            return !EMPTY.equals(o);
        } else {
            return true;
        }
    }

    @Override
    public String toString() {
        return this.list.stream().map(Objects::toString).collect(Collectors.joining());
    }
}