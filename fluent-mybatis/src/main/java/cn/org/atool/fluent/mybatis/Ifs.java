package cn.org.atool.fluent.mybatis;

import cn.org.atool.fluent.mybatis.model.IfsPredicate;
import lombok.AccessLevel;
import lombok.Getter;

import java.util.ArrayList;
import java.util.Collection;
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
    public final List<IfsPredicate<Predicate, Object>> predicates = new ArrayList<>();

    Ifs() {
    }

    public Ifs<T> when(Predicate<T> predicate, T value) {
        this.predicates.add(new IfsPredicate<>(predicate, value));
        return this;
    }

    public Ifs<T> when(Predicate<Collection> predicate, Object value, Object... values) {
        this.predicates.add(new IfsPredicate<>(predicate, value, values));
        return this;
    }

    public Ifs<T> other(T value, T... values) {
        this.predicates.add(new IfsPredicate<>(v -> true, value, values));
        return this;
    }
}
