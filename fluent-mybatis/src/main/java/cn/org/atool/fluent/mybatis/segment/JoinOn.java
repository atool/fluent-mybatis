package cn.org.atool.fluent.mybatis.segment;

import cn.org.atool.fluent.mybatis.base.crud.BaseQuery;
import cn.org.atool.fluent.mybatis.functions.OnConsumer;
import cn.org.atool.fluent.mybatis.metadata.JoinType;
import cn.org.atool.fluent.mybatis.segment.where.BaseWhere;

import java.lang.reflect.Constructor;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

public class JoinOn<QL extends BaseQuery<?, QL>, QR extends BaseQuery<?, QR>, JB> {
    private JoinQuery<QL> joinQuery;

    private QL onLeft;

    private QR onRight;

    private JoinOnBuilder onBuilder;

    public JoinOn(JoinQuery<QL> joinQuery, Class<QL> qLeftClass, QL qLeft, JoinType joinType, Class<QR> qRightClass, QR qRight) {
        this.joinQuery = joinQuery;
        this.onBuilder = new JoinOnBuilder(qLeft, joinType, qRight);
        this.onLeft = newEmptyQuery(qLeftClass);
        this.onRight = newEmptyQuery(qRightClass);
    }

    /**
     * 关联关系设置
     *
     * @param join
     * @return
     */
    public JB on(OnConsumer<QL, QR> join) {
        join.accept(this.onBuilder, this.onLeft, this.onRight);
        this.joinQuery.getWrapperData().addTable(onBuilder.table());
        return (JB) this.joinQuery;
    }

    /**
     * 关联关系设置
     *
     * @param l 左查询条件
     * @param r 右查询条件
     * @return
     */
    public JoinOn<QL, QR, JB> on(Function<QL, BaseWhere> l, Function<QR, BaseWhere> r) {
        this.onBuilder.on(l.apply(this.onLeft), r.apply(this.onRight));
        return this;
    }

    public JB endJoin() {
        this.joinQuery.getWrapperData().addTable(onBuilder.table());
        return (JB) this.joinQuery;
    }

    private static Map<Class, Constructor> QueryNoArgConstructors = new HashMap<>(128);

    /**
     * 执行on条件时, 新创建查询对象, 避免对原有对象的造成干扰
     *
     * @param klass
     * @param <Q>
     * @return
     */
    private static <Q extends BaseQuery<?, Q>> Q newEmptyQuery(Class<Q> klass) {
        try {
            if (!QueryNoArgConstructors.containsKey(klass)) {
                QueryNoArgConstructors.put(klass, klass.getConstructor());
            }
            return (Q) QueryNoArgConstructors.get(klass).newInstance();
        } catch (Exception e) {
            throw new RuntimeException(String.format("new %s() error: %s",
                klass.getSimpleName(), e.getMessage()), e);
        }
    }
}