package cn.org.atool.fluent.mybatis.ifs;

import cn.org.atool.fluent.mybatis.base.model.SqlOp;
import cn.org.atool.fluent.mybatis.utility.ObjectArray;

import java.util.Arrays;
import java.util.function.Predicate;

import static java.util.stream.Collectors.toList;

/**
 * 值对结构
 *
 * @author wudarui
 */
public class IfsPredicate {
    public final Predicate predicate;

    private final Object[] values;

    public IfsPredicate(Predicate predicate, Object... values) {
        this.predicate = predicate;
        this.values = convert(values);
    }

    private Object[] convert(Object[] values) {
        if (values == null || values.length != 1) {
            return values;
        }
        Object first = values[0];
        if (first == null) {
            return values;
        } else if (first.getClass().isArray()) {
            return ObjectArray.array(first);
        } else {
            return values;
        }
    }

    public Object value(SqlOp op) {
        if (op == null || values == null) {
            return this.firstValue();
        }
        switch (op) {
            case IN:
                return Arrays.stream(values).collect(toList());
            default:
                return this.firstValue();
        }
    }

    private Object firstValue() {
        return values == null || values.length == 0 ? null : values[0];
    }
}
