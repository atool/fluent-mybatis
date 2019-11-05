package cn.org.atool.mybatis.fluent.and;

import cn.org.atool.mybatis.fluent.util.MybatisUtil;
import com.baomidou.mybatisplus.core.conditions.AbstractWrapper;

import java.util.Collection;
import java.util.function.Predicate;
import java.util.function.Supplier;

public class AndObject<T, Q extends AbstractWrapper> {
    protected final String column;

    protected final String property;

    protected final Q wrapper;

    public AndObject(Q wrapper, String column, String property) {
        this.column = column;
        this.property = property;
        this.wrapper = wrapper;
    }

    public Q isNull() {
        return (Q) wrapper.isNull(column);
    }

    public Q isNull(boolean condition) {
        return condition ? this.isNull() : wrapper;
    }

    public Q isNotNull() {
        return (Q) wrapper.isNotNull(column);
    }

    public Q isNotNull(boolean condition) {
        return condition ? this.isNotNull() : wrapper;
    }
    // eq

    /**
     * @param value
     * @return
     */
    public Q eq(T value) {
        MybatisUtil.assertNotNull(property, value);
        return (Q) wrapper.eq(column, value);
    }

    public Q eq(boolean condition, T value) {
        return condition ? this.eq(value) : wrapper;
    }

    public Q eq(boolean condition, Supplier<T> supplier) {
        return condition ? this.eq(supplier.get()) : wrapper;
    }

    public Q eq_IfNotNull(T value) {
        return this.eq(value != null, value);
    }

    public Q eq(Predicate<T> predicate, T value) {
        return this.eq(predicate.test(value), value);
    }

    public Q eq(Predicate<T> predicate, Supplier<T> supplier) {
        return eq(predicate, supplier.get());
    }

    // ne
    public Q ne(T value) {
        MybatisUtil.assertNotNull(property, value);
        return (Q) wrapper.ne(column, value);
    }

    public Q ne(boolean condition, T value) {
        return condition ? this.ne(value) : wrapper;
    }

    public Q ne(boolean condition, Supplier<T> supplier) {
        return condition ? this.ne(supplier.get()) : wrapper;
    }

    public Q ne_IfNotNull(T value) {
        return this.ne(value != null, value);
    }

    public Q ne(Predicate<T> predicate, T value) {
        return this.ne(predicate.test(value), value);
    }

    public Q ne(Predicate<T> predicate, Supplier<T> supplier) {
        return ne(predicate, supplier.get());
    }

    //gt
    public Q gt(T value) {
        MybatisUtil.assertNotNull(property, value);
        return (Q) wrapper.gt(column, value);
    }

    public Q gt(boolean condition, T value) {
        return condition ? this.gt(value) : wrapper;
    }

    public Q gt(boolean condition, Supplier<T> supplier) {
        return condition ? this.gt(supplier.get()) : wrapper;
    }

    public Q gt_IfNotNull(T value) {
        return this.gt(value != null, value);
    }

    public Q gt(Predicate<T> predicate, T value) {
        return this.gt(predicate.test(value), value);
    }

    public Q gt(Predicate<T> predicate, Supplier<T> supplier) {
        return gt(predicate, supplier.get());
    }

    //ge
    public Q ge(T value) {
        MybatisUtil.assertNotNull(property, value);
        return (Q) wrapper.ge(column, value);
    }

    public Q ge(boolean condition, T value) {
        return condition ? this.ge(value) : wrapper;
    }

    public Q ge(boolean condition, Supplier<T> supplier) {
        return condition ? this.ge(supplier.get()) : wrapper;
    }

    public Q ge_IfNotNull(T value) {
        return this.ge(value != null, value);
    }

    public Q ge(Predicate<T> predicate, T value) {
        return this.ge(predicate.test(value), value);
    }

    public Q ge(Predicate<T> predicate, Supplier<T> supplier) {
        return ge(predicate, supplier.get());
    }

    //lt
    public Q lt(T value) {
        MybatisUtil.assertNotNull(property, value);
        return (Q) wrapper.lt(column, value);
    }

    public Q lt(boolean condition, T value) {
        return condition ? this.lt(value) : wrapper;
    }

    public Q lt(boolean condition, Supplier<T> supplier) {
        return condition ? this.lt(supplier.get()) : wrapper;
    }

    public Q lt_IfNotNull(T value) {
        return this.lt(value != null, value);
    }

    public Q lt(Predicate<T> predicate, T value) {
        return this.lt(predicate.test(value), value);
    }

    public Q lt(Predicate<T> predicate, Supplier<T> supplier) {
        return lt(predicate, supplier.get());
    }

    //le
    public Q le(T value) {
        MybatisUtil.assertNotNull(property, value);
        return (Q) wrapper.le(column, value);
    }

    public Q le(boolean condition, T value) {
        return condition ? this.le(value) : wrapper;
    }

    public Q le(boolean condition, Supplier<T> supplier) {
        return condition ? this.le(supplier.get()) : wrapper;
    }

    public Q le_IfNotNull(T value) {
        return this.le(value != null, value);
    }

    public Q le(Predicate<T> predicate, T value) {
        return this.le(predicate.test(value), value);
    }

    public Q le(Predicate<T> predicate, Supplier<T> supplier) {
        return le(predicate, supplier.get());
    }

    //in
    public Q in(Collection<T> values) {
        MybatisUtil.assertNotEmpty(property, values);
        return (Q) wrapper.in(column, values);
    }

    public Q in(boolean condition, Collection<T> values) {
        return condition ? this.in(values) : wrapper;
    }

    public Q in(boolean condition, Supplier<Collection<T>> supplier) {
        return condition ? this.in(supplier.get()) : wrapper;
    }

    public Q in_IfNotEmpty(Collection<T> values) {
        return this.in(values != null && !values.isEmpty(), values);
    }

    public Q in(Predicate<Collection<T>> predicate, Collection<T> values) {
        return this.in(predicate.test(values), values);
    }

    public Q in(Predicate<Collection<T>> predicate, Supplier<Collection<T>> supplier) {
        return in(predicate, supplier.get());
    }

    public Q in(T... values) {
        MybatisUtil.assertNotEmpty(property, values);
        return (Q) wrapper.in(column, values);
    }

    public Q in(boolean condition, T... values) {
        return condition ? this.in(values) : wrapper;
    }

    public Q in_IfNotEmpty(T... values) {
        return this.in(values != null && values.length > 0, values);
    }

    //not in
    public Q notIn(Collection<T> values) {
        MybatisUtil.assertNotEmpty(property, values);
        return (Q) wrapper.notIn(column, values);
    }

    public Q notIn(boolean condition, Collection<T> values) {
        return condition ? this.notIn(values) : wrapper;
    }

    public Q notIn(boolean condition, Supplier<Collection<T>> supplier) {
        return condition ? this.notIn(supplier.get()) : wrapper;
    }

    public Q notIn_IfNotEmpty(Collection<T> values) {
        return this.notIn(values != null && !values.isEmpty(), values);
    }

    public Q notIn(Predicate<Collection<T>> predicate, Collection<T> values) {
        return this.notIn(predicate.test(values), values);
    }

    public Q notIn(Predicate<Collection<T>> predicate, Supplier<Collection<T>> supplier) {
        return notIn(predicate, supplier.get());
    }

    public Q notIn(T... values) {
        MybatisUtil.assertNotEmpty(property, values);
        return (Q) wrapper.notIn(column, values);
    }

    public Q notIn(boolean condition, T... values) {
        return condition ? this.notIn(values) : wrapper;
    }

    public Q notIn_IfNotEmpty(T... values) {
        return this.notIn(values != null && values.length > 0, values);
    }

    //between
    public Q between(T value1, T value2) {
        MybatisUtil.assertNotNull(property, value1, value2);
        return (Q) wrapper.between(column, value1, value2);
    }

    public Q between(boolean condition, T value1, T value2) {
        return condition ? this.between(value1, value2) : wrapper;
    }

    //not between
    public Q notBetween(T value1, T value2) {
        MybatisUtil.assertNotNull(property, value1, value2);
        return (Q) wrapper.notBetween(column, value1, value2);
    }

    public Q notBetween(boolean condition, T value1, T value2) {
        return condition ? this.notBetween(value1, value2) : wrapper;
    }
}
