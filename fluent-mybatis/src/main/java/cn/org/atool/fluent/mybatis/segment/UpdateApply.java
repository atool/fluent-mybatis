package cn.org.atool.fluent.mybatis.segment;

import cn.org.atool.fluent.mybatis.base.IUpdate;

import java.util.function.Predicate;

import static cn.org.atool.fluent.mybatis.utility.MybatisUtil.assertNotBlank;

/**
 * SetObject 更新字段值
 *
 * @param <S> 更新器
 * @author darui.wu
 */
public class UpdateApply<
    S extends UpdateBase<S, U>,
    U extends IUpdate<?, U, ?>
    > extends BaseApply<S, U> {

    public UpdateApply(S setter) {
        super(setter);
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
    public S is(Object value) {
        this.segment.wrapperData().updateSet(this.current.column, value);
        return segment;
    }

    /**
     * 值更新为null: #{column} = null
     *
     * @return 更新器
     */
    public S isNull() {
        return this.is(null);
    }

    /**
     * value不为null时更新
     *
     * @param value 更新值
     * @param when  当条件为真时设置更新
     * @return 更新器
     */
    public <O> S is(O value, Predicate<O> when) {
        return when.test(value) ? this.is(value) : this.segment;
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
    public S apply(String function, Object... args) {
        assertNotBlank("function", function);
        if (args == null || args.length == 0) {
            this.segment.wrapperData().updateSql(this.current.column, function, args);
        } else {
            this.segment.wrapperData().updateSql(this.current.column, function, args);
        }
        return this.segment;
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
    public S apply_If(boolean condition, String function, Object... args) {
        if (condition) {
            this.apply(function, args);
        }
        return this.segment;
    }
}