package cn.org.atool.fluent.mybatis.interfaces;

import java.util.function.Function;

/**
 * IEntityUpdate: 更新接口
 *
 * @param <E> 对应的实体类
 * @param <U> 更新器
 * @param <Q> 对应的嵌套查询器
 * @author darui.wu
 */
public interface IUpdate<E extends IEntity, U, Q extends IQuery<E, Q>> extends IWrapper<E, U, Q> {
    /**
     * 设置limit值
     *
     * @param limit
     * @return
     */
    U limit(int limit);

    /**
     * 执行更新操作
     *
     * @param executor 具体更新操作
     * @return 返回更新的记录数
     */
    default int execute(Function<U, Integer> executor) {
        return executor.apply((U) this);
    }

    /**
     * 更新column字段值
     *
     * @param column 字段
     * @param val    更新值
     * @return self
     */
    U set(String column, Object val);

    /**
     * 设置更新（自定义SQL）
     *
     * @param column      更新的字段
     * @param functionSql set function sql
     * @param values      对应的参数
     * @return self
     */
    U setSql(String column, String functionSql, Object... values);
}