package cn.org.atool.fluent.mybatis.interfaces;

import cn.org.atool.fluent.mybatis.condition.model.KeyWordSegment;
import cn.org.atool.fluent.mybatis.condition.model.ParameterPair;
import cn.org.atool.fluent.mybatis.condition.model.SqlOp;

import java.io.Serializable;
import java.util.Map;
import java.util.function.Consumer;

/**
 * IWrapper: 查询和更新都用到的接口
 *
 * @param <E> 对应的实体类
 * @param <W> 最终查询器或更新器
 * @param <Q> 对应的嵌套查询器
 * @author darui.wu
 */
public interface IWrapper<E extends IEntity, W, Q extends IQuery<E, Q>> extends Serializable {

    /**
     * map 所有非空属性等于 =
     * key: column字段名称
     * value: 设置值, 忽略null值
     *
     * @param params map 类型的参数, key 是字段名, value 是字段值
     * @param <V>
     * @return self
     */
    default <V> W eqByNotNull(Map<String, V> params) {
        return eqByMap(params, true);
    }

    /**
     * map 所有非空属性等于 =
     * key: column字段名称
     * value: 设置值
     *
     * @param params     map 类型的参数, key 是字段名, value 是字段值
     * @param ignoreNull value为null时,是否忽略。如果ignoreNull = false, 且value=null, 会执行 column is null判断
     * @return self
     */
    <V> W eqByMap(Map<String, V> params, boolean ignoreNull);

    /**
     * where条件设置为entity对象非空属性
     *
     * @param entity
     * @return 查询器UserQuery
     */
    W eqByNotNull(E entity);

    /**
     * 增加条件设置
     *
     * @param keyWord  or and
     * @param column   设置条件的字段
     * @param operator 条件操作
     * @param paras    条件参数（填充 operator 中占位符?)
     * @return self
     */
    default W apply(KeyWordSegment keyWord, String column, SqlOp operator, Object... paras) {
        return this.apply(keyWord, column, null, operator, paras);
    }

    /**
     * 增加条件设置
     *
     * @param keyWord  or and
     * @param column   设置条件的字段
     * @param format   格式化sql语句
     * @param operator 条件操作
     * @param paras    条件参数（填充 operator 中占位符?)
     * @return self
     */
    W apply(KeyWordSegment keyWord, String column, String format, SqlOp operator, Object... paras);

    /**
     * 拼接 sql
     *
     * <p>例1: and apply("id = 1")</p>
     * <p>例2: and apply("date_format(dateColumn,'%Y-%m-%d') = '2008-08-08'")</p>
     * <p>例3: and apply("date_format(dateColumn,'%Y-%m-%d') = ?", LocalDate.now())</p>
     *
     * @param applySql 拼接的sql语句
     * @param values   对应sql语句的 "?" 参数
     * @return children
     */
    W and(String applySql, Object... values);

    /**
     * 拼接 sql
     *
     * <p>例1: or apply("id = 1")</p>
     * <p>例2: or apply("date_format(dateColumn,'%Y-%m-%d') = '2008-08-08'")</p>
     * <p>例3: or apply("date_format(dateColumn,'%Y-%m-%d') = ?", LocalDate.now())</p>
     *
     * @param applySql 拼接的sql语句
     * @param values   对应sql语句的 "?" 参数
     * @return children
     */
    W or(String applySql, Object... values);

    /**
     * AND 嵌套
     * <p>
     * 例: and(i -&gt; i.eq("name", "value1").ne("status", "status1"))
     * </p>
     *
     * @param query 消费函数
     * @return children
     */
    W and(Consumer<Q> query);

    /**
     * <pre>
     * OR 嵌套
     * 例: or(i -&gt; i.eq("name", "value1").ne("status", "status1"))
     * </pre>
     *
     * @param consumer 消费函数
     * @return self
     */
    W or(Consumer<Q> consumer);

    /**
     * 追加在sql语句的末尾
     * !!!慎用!!!
     * 有sql注入风险
     *
     * @param lastSql
     * @return
     */
    W last(String lastSql);

    /**
     * 返回实体对象class
     *
     * @return 实体对象class
     */
    Class<? extends IEntity> getEntityClass();

    /**
     * 返回参数列表
     *
     * @return 参数列表
     */
    ParameterPair getParameters();
}