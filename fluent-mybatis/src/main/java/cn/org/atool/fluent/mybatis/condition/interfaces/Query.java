package cn.org.atool.fluent.mybatis.condition.interfaces;

import cn.org.atool.fluent.mybatis.condition.base.PredicateField;

import java.io.Serializable;

/**
 * @author darui.wu
 */
public interface Query<Children, T, R> extends Serializable {

    /**
     * 设置查询字段
     *
     * @param columns 字段数组
     * @return children
     */
    Children select(R... columns);

    /**
     * <p>注意只有内部有 entity 才能使用该方法</p>
     * 过滤查询的字段信息(主键除外!)
     * <p>例1: 只要 java 字段名以 "test" 开头的             -> select(i -> i.getProperty().startsWith("test"))</p>
     * <p>例2: 只要 java 字段属性是 CharSequence 类型的     -> select(TableFieldInfo::isCharSequence)</p>
     * <p>例3: 只要 java 字段没有填充策略的                 -> select(i -> i.getFieldFill() == FieldFill.DEFAULT)</p>
     * <p>例4: 要全部字段                                   -> select(i -> true)</p>
     * <p>例5: 只要主键字段                                 -> select(i -> false)</p>
     *
     * @param predicate 过滤方式
     * @return children
     */
    Children select(PredicateField predicate);

    /**
     * 查询条件 SQL 片段
     *
     * @return 查询字段列表
     */
    String getSqlSelect();
}