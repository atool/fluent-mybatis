package cn.org.atool.fluent.mybatis.condition.apply;

import cn.org.atool.fluent.mybatis.condition.base.BaseSetter;
import cn.org.atool.fluent.mybatis.interfaces.IUpdate;

import static cn.org.atool.fluent.mybatis.utility.MybatisUtil.assertNotBlank;

/**
 * SetObject 更新字段值
 *
 * @param <T> 字段类型
 * @param <U> 更新器
 * @author darui.wu
 */
public class SetObject<T, U extends IUpdate> {
    protected final BaseSetter setter;

    protected final String column;

    protected final String property;

    public SetObject(BaseSetter setter, String column, String property) {
        this.setter = setter;
        this.column = column;
        this.property = property;
    }

    /**
     * <p>
     * 更新字段值: #{column}=value
     * value可以为null
     * </p>
     *
     * @param value 更新值
     * @return 更新器
     */
    public U is(T value) {
        return (U) this.updater().set(property, value);
    }

    /**
     * 值更新为null: #{column} = null
     *
     * @return 更新器
     */
    public U isNull() {
        return this.is(null);
    }

    /**
     * 当condition为真时，更新 #{column}=value
     *
     * @param condition 条件为真时执行
     * @param value     更新值
     * @return 更新器
     */
    public U is_If(boolean condition, T value) {
        return condition ? this.is(value) : this.updater();
    }

    /**
     * value不为null时更新
     *
     * @param value 更新值
     * @return 更新器
     */
    public U is_IfNotNull(T value) {
        return this.is_If(value != null, value);
    }

    //function

    /**
     * 按函数function更新, 示例
     * apply( "concat('abc', ?)", "xyz"): 将字段赋值为 abc连接value("xyz")
     *
     * @param function 函数
     * @param args     函数参数列表
     * @return 更新器
     */
    public U apply(String function, Object... args) {
        assertNotBlank("function", function);
        if (args == null || args.length == 0) {
            return (U) this.updater().setSql(column, function, args);
        } else {
            return (U) this.updater().setSql(column, function, args);
        }
    }

    /**
     * 按函数function更新, 示例
     * apply( "concat('abc', ?)", "xyz"): 将字段赋值为 abc连接value("xyz")
     *
     * @param condition 条件为真时, 更新才被执行
     * @param function  函数
     * @param args      函数参数列表
     * @return 更新器
     */
    public U apply_If(boolean condition, String function, Object... args) {
        return condition ? this.apply(function, args) : this.updater();
    }

    /**
     * 返回更新器
     *
     * @return 更新器
     */
    protected U updater() {
        return (U) this.setter.getUpdater();
    }
}