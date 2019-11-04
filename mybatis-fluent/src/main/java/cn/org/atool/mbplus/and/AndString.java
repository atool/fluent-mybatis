package cn.org.atool.mbplus.and;

import cn.org.atool.mbplus.util.MybatisUtil;
import com.baomidou.mybatisplus.core.conditions.AbstractWrapper;

import java.util.function.Predicate;
import java.util.function.Supplier;

import static com.baomidou.mybatisplus.core.toolkit.StringUtils.isNotEmpty;


public class AndString<Q extends AbstractWrapper> extends AndObject<String, Q> {
    public AndString(Q wrapper, String column, String property) {
        super(wrapper, column, property);
    }

    public Q eq_IfNotBlank(String value) {
        return super.eq(isNotEmpty(value), value);
    }

    public Q ne_IfNotBlank(String value) {
        return super.ne(isNotEmpty(value), value);
    }

    public Q gt_IfNotBlank(String value) {
        return super.gt(isNotEmpty(value), value);
    }

    public Q ge_IfNotBlank(String value) {
        return super.ge(isNotEmpty(value), value);
    }

    public Q lt_IfNotBlank(String value) {
        return super.lt(isNotEmpty(value), value);
    }

    public Q le_IfNotBlank(String value) {
        return super.le(isNotEmpty(value), value);
    }

    //like
    public Q like(String value) {
        MybatisUtil.assertNotBlank(property, value);
        return (Q) wrapper.like(column, value);
    }

    public Q like(boolean condition, String value) {
        return condition ? this.like(value) : wrapper;
    }

    public Q like(boolean condition, Supplier<String> supplier) {
        return condition ? this.like(supplier.get()) : wrapper;
    }

    public Q like_IfNotBlank(String value) {
        return this.like(isNotEmpty(value), value);
    }

    public Q like(Predicate<String> predicate, String value) {
        return this.like(predicate.test(value), value);
    }

    public Q like(Predicate<String> predicate, Supplier<String> supplier) {
        return like(predicate, supplier.get());
    }

    //not like
    public Q notLike(String value) {
        MybatisUtil.assertNotBlank(property, value);
        return (Q) wrapper.notLike(column, value);
    }

    public Q notLike(boolean condition, String value) {
        return condition ? this.notLike(value) : wrapper;
    }

    public Q notLike(boolean condition, Supplier<String> supplier) {
        return condition ? this.notLike(supplier.get()) : wrapper;
    }

    public Q notLike_IfNotBlank(String value) {
        return this.notLike(isNotEmpty(value), value);
    }

    public Q notLike(Predicate<String> predicate, String value) {
        return this.notLike(predicate.test(value), value);
    }

    public Q notLike(Predicate<String> predicate, Supplier<String> supplier) {
        return notLike(predicate, supplier.get());
    }
    //like left
    public Q likeLeft(String value) {
        MybatisUtil.assertNotBlank(property, value);
        return (Q) wrapper.likeLeft(column, value);
    }

    public Q likeLeft(boolean condition, String value) {
        return condition ? this.likeLeft(value) : wrapper;
    }

    public Q likeLeft(boolean condition, Supplier<String> supplier) {
        return condition ? this.likeLeft(supplier.get()) : wrapper;
    }

    public Q likeLeft_IfNotBlank(String value) {
        return this.likeLeft(isNotEmpty(value), value);
    }

    public Q likeLeft(Predicate<String> predicate, String value) {
        return this.likeLeft(predicate.test(value), value);
    }

    public Q likeLeft(Predicate<String> predicate, Supplier<String> supplier) {
        return likeLeft(predicate, supplier.get());
    }

    //like right
    public Q likeRight(String value) {
        MybatisUtil.assertNotBlank(property, value);
        return (Q) wrapper.likeRight(column, value);
    }

    public Q likeRight(boolean condition, String value) {
        return condition ? this.likeRight(value) : wrapper;
    }

    public Q likeRight(boolean condition, Supplier<String> supplier) {
        return condition ? this.likeRight(supplier.get()) : wrapper;
    }

    public Q likeRight_IfNotBlank(String value) {
        return this.likeRight(isNotEmpty(value), value);
    }

    public Q likeRight(Predicate<String> predicate, String value) {
        return this.likeRight(predicate.test(value), value);
    }

    public Q likeRight(Predicate<String> predicate, Supplier<String> supplier) {
        return likeRight(predicate, supplier.get());
    }
}
