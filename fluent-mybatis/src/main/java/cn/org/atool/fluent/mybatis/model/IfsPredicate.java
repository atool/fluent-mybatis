package cn.org.atool.fluent.mybatis.model;

import cn.org.atool.fluent.mybatis.base.model.SqlOp;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 值对结构
 *
 * @param <V>
 */
public class IfsPredicate<Predicate, V> {
    public final Predicate predicate;

    private final V value;

    private final V[] values;

    public IfsPredicate(Predicate predicate, V value, V... values) {
        this.predicate = predicate;
        this.value = value;
        this.values = values;
    }

    public Object value(SqlOp op) {
        if (op == null) {
            return value;
        }
        switch (op) {
            case IN:
                List list = new ArrayList<>();
                list.add(value);
                Arrays.stream(values).forEach(list::add);
                return list;
            default:
                return value;
        }
    }
}
