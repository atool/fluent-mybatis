package cn.org.atool.fluent.mybatis.base.crud;

import cn.org.atool.fluent.mybatis.functions.QFunction;
import cn.org.atool.fluent.mybatis.segment.JoinQuery;

/**
 * 连接查询构造
 *
 * @author wudarui
 */
public interface JoinBuilder<QL extends BaseQuery<?, QL>>  {

    /**
     * 关联查询构造方式一: 使用直接传入设置好别名和参数的Query
     *
     * @param query
     * @param <QL>
     * @return
     */
    static <QL extends BaseQuery<?, QL>> IJoinBuilder1<QL> from(QL query) {
        return new JoinQuery<>(query);
    }

    /**
     * 关联查询构造方式二: 使用lambda表达式,由框架自动设置query别名和关联参数
     * <p>
     * 注: 在有些场景下, IDE对lambda表达式的代码提示不够智能
     * <p>
     *
     * @param clazz
     * @param query
     * @param <QL>
     * @return
     */
    static <QL extends BaseQuery<?, QL>> IJoinBuilder2<QL> from(Class<QL> clazz, QFunction<QL> query) {
        return new JoinQuery<>(clazz, query);
    }

    IQuery<?, QL> build();
}
