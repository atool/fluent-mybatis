package cn.org.atool.fluent.mybatis.segment.fragment;

import cn.org.atool.fluent.mybatis.base.entity.IMapping;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.stream.Stream;

import static cn.org.atool.fluent.mybatis.If.isBlank;
import static cn.org.atool.fluent.mybatis.mapper.StrConstant.EMPTY;
import static cn.org.atool.fluent.mybatis.mapper.StrConstant.SEMICOLON;
import static cn.org.atool.fluent.mybatis.segment.fragment.Column.columnEquals;
import static cn.org.atool.fluent.mybatis.segment.fragment.Fragments.SEG_EMPTY;
import static java.util.stream.Collectors.joining;

/**
 * IFragment List
 *
 * @author darui.wu
 */
@Accessors(chain = true)
public class JoiningFrag implements IFragment {
    @Setter
    private String delimiter;
    /**
     * 结果过滤器
     */
    @Setter
    private Predicate<String> filter = null;

    @Getter
    protected final List<IFragment> segments = new ArrayList<>();

    public JoiningFrag(String delimiter) {
        this.delimiter = isBlank(delimiter) ? EMPTY : delimiter;
    }

    @Override
    public boolean notEmpty() {
        return !this.segments.isEmpty();
    }

    @Override
    public String get(IMapping mapping) {
        if (this.isEmpty()) {
            return EMPTY;
        } else {
            return merge(mapping);
        }
    }

    private String merge(IMapping mapping) {
        List<String> list = new ArrayList<>(segments.size());
        for (IFragment seg : segments) {
            String text = seg.get(mapping);
            if (filter == null || filter.test(text)) {
                list.add(text);
            }
        }
        String text = String.join(delimiter, list);
        return text.trim();
    }

    public JoiningFrag add(IFragment... segments) {
        Stream.of(segments).filter(Objects::nonNull).filter(IFragment::notEmpty).forEach(this.segments::add);
        return this;
    }

    public JoiningFrag add(String segment) {
        this.segments.add(CachedFrag.set(segment));
        return this;
    }

    public int size() {
        return this.segments.size();
    }

    public static JoiningFrag get(String delimiter) {
        return new JoiningFrag(delimiter);
    }

    public static JoiningFrag get() {
        return new JoiningFrag(EMPTY);
    }

    public boolean isEmpty() {
        return this.segments.isEmpty();
    }

    public void removeColumn(String column) {
        segments.removeIf(segment -> columnEquals(segment, column));
    }

    public void forEach(Consumer<IFragment> consumer) {
        this.segments.forEach(consumer);
    }

    public IFragment last() {
        if (segments.size() > 0) {
            return this.segments.get(segments.size() - 1);
        } else {
            return SEG_EMPTY;
        }
    }

    public void clear() {
        this.segments.clear();
    }

    public IFragment[] toArray() {
        return this.segments.toArray(new IFragment[0]);
    }

    @Override
    public String toString() {
        return this.segments.stream()
            .map(IFragment::toString)
            .collect(joining(delimiter == null ? SEMICOLON : delimiter));
    }
}