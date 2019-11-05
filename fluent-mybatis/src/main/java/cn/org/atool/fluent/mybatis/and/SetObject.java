package cn.org.atool.fluent.mybatis.and;

import cn.org.atool.fluent.mybatis.base.IEntityUpdate;
import cn.org.atool.fluent.mybatis.util.MybatisUtil;
import cn.org.atool.fluent.mybatis.util.SqlInject;

import java.util.function.Predicate;
import java.util.function.Supplier;

public class SetObject<T, U extends IEntityUpdate> {
    protected final String column;

    protected final String property;

    protected final U wrapper;

    public SetObject(U wrapper, String column, String property) {
        this.column = column;
        this.property = property;
        this.wrapper = wrapper;
    }

    /**
     * <p>
     * #{column}=value
     * value可以为null
     * </p>
     *
     * @param value
     * @return
     */
    public U is(T value) {
        return (U) wrapper.set(property, value);
    }

    /**
     * 更新 #{column} = null
     *
     * @return
     */
    public U isNull() {
        return this.is(null);
    }

    /**
     * 当condition为真时，更新 #{column}=value
     *
     * @param condition 条件
     * @param value
     * @return
     */
    public U is_If(boolean condition, T value) {
        return condition ? this.is(value) : wrapper;
    }

    public U is_IfNotNull(T value) {
        return this.is_If(value != null, value);
    }

    public U is_If(Predicate<T> predicate, T value) {
        return this.is_If(predicate.test(value), value);
    }

    public U by(Supplier<T> supplier) {
        return (U) wrapper.set(property, supplier.get());
    }

    public U by_If(boolean condition, Supplier<T> supplier) {
        return condition ? this.by(supplier) : wrapper;
    }

    public U by_If(Predicate<T> predicate, Supplier<T> supplier) {
        T value = supplier.get();
        return this.is_If(predicate, value);
    }

    //function
    public U function(String function, Object... args) {
        MybatisUtil.assertNotBlank("function", function);
        if (args == null || args.length == 0) {
            return (U) this.wrapper.setSql(column + "=" + function);
        } else {
            for (int index = 0; index < args.length; index++) {
                SqlInject.assertSimpleNoInject("args[" + index + "]", String.valueOf(args[index]));
            }
            return (U) this.wrapper.setSql(column + "=" + String.format(function, args));
        }
    }

    public U function_If(boolean condition, String function, Object... args) {
        return condition ? this.function(function, args) : wrapper;
    }
}