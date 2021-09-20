package cn.org.atool.fluent.mybatis.segment.fragment;

import cn.org.atool.fluent.mybatis.base.entity.IMapping;

import java.util.Arrays;

/**
 * 格式化片段
 *
 * @author darui.wu
 */
public class FormatFrag implements IFragment {
    private final String format;

    private final Object[] args;

    private FormatFrag(String format, Object[] args) {
        this.format = format;
        this.args = args;
    }

    @Override
    public String get(IMapping mapping) {
        Object[] _args = new Object[0];
        if (args != null && args.length > 0) {
            _args = Arrays.stream(this.args)
                .map(o -> o instanceof IFragment ? ((IFragment) o).get(mapping) : String.valueOf(o))
                .toArray();
        }
        return String.format(format, _args);
    }

    public static FormatFrag format(String format, Object... args) {
        return new FormatFrag(format, args);
    }

    @Override
    public String toString() {
        return this.format + ", args size=" + this.args.length;
    }
}