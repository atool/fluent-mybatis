package cn.org.atool.fluent.mybatis.segment;

import cn.org.atool.fluent.mybatis.base.JoinBuilder;
import cn.org.atool.fluent.mybatis.base.impl.BaseQuery;
import cn.org.atool.fluent.mybatis.metadata.JoinType;
import cn.org.atool.fluent.mybatis.segment.where.BaseWhere;

import java.lang.reflect.Constructor;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

public class JoinOn<QL extends BaseQuery<?, QL>, QR extends BaseQuery<?, QR>> {
    private JoinQuery<QL> joinBuilder;

    private QL onLeft;

    private QR onRight;

    private JoinOnBuilder onBuilder;

    public JoinOn(JoinQuery<QL> joinBuilder, Class<QL> qLeftClass, QL qLeft, JoinType joinType, Class<QR> qRightClass, QR qRight) {
        this.joinBuilder = joinBuilder;
        this.onBuilder = new JoinOnBuilder(qLeft, joinType, qRight);
        this.onLeft = newEmptyQuery(qLeftClass);
        this.onRight = newEmptyQuery(qRightClass);
    }

    /**
     * 关联关系设置
     *
     * @param l 左查询条件
     * @param r 右查询条件
     * @return
     */
    public JoinOn<QL, QR> on(Function<QL, BaseWhere> l, Function<QR, BaseWhere> r) {
        this.onBuilder.on(l.apply(this.onLeft), r.apply(this.onRight));
        return this;
    }

    public JoinBuilder<QL> endJoin() {
        this.joinBuilder.getWrapperData().addTable(onBuilder.table());
        return this.joinBuilder;
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