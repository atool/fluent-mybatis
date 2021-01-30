package cn.org.atool.fluent.mybatis;

import cn.org.atool.fluent.mybatis.model.Pair;
import lombok.AccessLevel;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

/**
 * 分支设置
 *
 * @param <T>
 * @author wudarui
 */
@Getter
public class Ifs<T> {
    /**
     * 选择分支
     */
    @Getter(AccessLevel.NONE)
    public final List<Pair<Predicate, Object>> switches = new ArrayList<>();

    private boolean hasOther = false;

    private Object other;

    Ifs() {
    }

    public Ifs<T> when(T value, Predicate<T> predicate) {
        this.switches.add(new Pair<>(predicate, value));
        return this;
    }

    public Ifs<T> other(T value) {
        this.hasOther = true;
        this.other = value;
        return Ifs.this;
    }
}
