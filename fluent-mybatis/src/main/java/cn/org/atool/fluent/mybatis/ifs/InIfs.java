package cn.org.atool.fluent.mybatis.ifs;

import java.util.Collection;
import java.util.function.Predicate;

/**
 * 对 IN collection进行条件选择
 *
 * @author wudarui
 */
@SuppressWarnings("all")
public class InIfs<T> extends Ifs<Collection> {
    public InIfs<T> when(Predicate<Collection> predicate, T... values) {
        this.predicates.add(new IfsPredicate(predicate, values));
        return this;
    }

    public InIfs<T> other(T... values) {
        this.predicates.add(new IfsPredicate(v -> true, values));
        return this;
    }

    @Override
    public InIfs<T> when(Predicate<Collection> predicate, Collection value) {
        this.predicates.add(new IfsPredicate(predicate, value == null ? null : value.toArray()));
        return this;
    }

    @Override
    public InIfs<T> other(Collection value) {
        return this.when(v -> true, value);
    }
}
