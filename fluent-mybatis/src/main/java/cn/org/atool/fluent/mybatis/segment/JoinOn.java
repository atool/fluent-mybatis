package cn.org.atool.fluent.mybatis.segment;

import cn.org.atool.fluent.mybatis.base.IEntity;
import cn.org.atool.fluent.mybatis.base.crud.BaseQuery;
import cn.org.atool.fluent.mybatis.base.model.FieldMapping;
import cn.org.atool.fluent.mybatis.base.splice.FreeQuery;
import cn.org.atool.fluent.mybatis.functions.GetterFunc;
import cn.org.atool.fluent.mybatis.functions.OnConsumer;
import cn.org.atool.fluent.mybatis.metadata.JoinType;
import cn.org.atool.fluent.mybatis.segment.where.BaseWhere;
import cn.org.atool.fluent.mybatis.utility.MappingKits;

import java.lang.reflect.Constructor;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import static cn.org.atool.fluent.mybatis.utility.MybatisUtil.assertNotNull;

/**
 * 关联查询on条件设置
 *
 * @param <QL> 关联左查询类型
 * @param <QR> 关联右查询类型
 * @param <JB> JoinBuilder
 * @author darui.wu
 */
public class JoinOn<QL extends BaseQuery<?, QL>, QR extends BaseQuery<?, QR>, JB> {
    private final JoinQuery<QL> joinQuery;

    private final QL onLeft;

    private final QR onRight;

    private final JoinOnBuilder<QL, QR> onBuilder;

    public JoinOn(JoinQuery<QL> joinQuery, Class<QL> qLeftClass, QL qLeft, JoinType joinType, Class<QR> qRightClass, QR qRight) {
        this.joinQuery = joinQuery;
        this.onBuilder = new JoinOnBuilder<>(qLeft, joinType, qRight);
        if (qLeft instanceof FreeQuery) {
            this.onLeft = (QL) ((FreeQuery) qLeft).emptyQuery();
        } else {
            this.onLeft = newEmptyQuery(qLeftClass);
        }
        if (qRight instanceof FreeQuery) {
            this.onRight = (QR) ((FreeQuery) qRight).emptyQuery();
        } else {
            this.onRight = newEmptyQuery(qRightClass);
        }
        this.onLeft.tableAlias = qLeft.tableAlias;
        this.onRight.tableAlias = qRight.tableAlias;
    }

    /**
     * 关联关系设置
     *
     * @param join
     * @return JoinOn
     */
    @Deprecated
    public JB on(OnConsumer<QL, QR> join) {
        join.accept(this.onBuilder, this.onLeft, this.onRight);
        this.joinQuery.getWrapperData().addTable(this.onBuilder.table());
        return (JB) this.joinQuery;
    }

    /**
     * 自由设置连接关系, 设置时需要加上表别名
     * 比如: t1.id = t2.id AND t1.is_deleted = t2.is_deleted
     *
     * @param condition
     * @return JoinOn
     */
    public JB on(String condition) {
        this.joinQuery.getWrapperData().addTable(this.onBuilder.table() + " ON " + condition);
        return (JB) this.joinQuery;
    }

    /**
     * 关联关系设置
     *
     * @param l 左查询条件
     * @param r 右查询条件
     * @return JoinOn
     */
    public JoinOn<QL, QR, JB> on(Function<QL, BaseWhere> l, Function<QR, BaseWhere> r) {
        this.onBuilder.on(l.apply(this.onLeft), r.apply(this.onRight));
        return this;
    }

    /**
     * 关联关系设置
     *
     * @param l 左查询条件
     * @param r 右查询条件
     * @return JoinOn
     */
    public <LE extends IEntity, RE extends IEntity> JoinOn<QL, QR, JB> onGetter(GetterFunc<LE> l, GetterFunc<RE> r) {
        Class lKlass = this.onLeft.wrapperData.getEntityClass();
        Class rKlass = this.onRight.wrapperData.getEntityClass();
        assertNotNull("left query entity class", lKlass);
        assertNotNull("right query entity class", rKlass);
        String lField = MappingKits.toColumn(lKlass, l);
        String rField = MappingKits.toColumn(rKlass, r);
        return this.on(lField, rField);
    }

    /**
     * 关联关系设置
     *
     * @param l 左关联字段
     * @param r 右关联字段
     * @return JoinOn
     */
    public JoinOn<QL, QR, JB> on(String l, String r) {
        this.onBuilder.on(l, r);
        return this;
    }

    /**
     * 左表固定关联关系
     *
     * @param l 左查询条件
     * @return JoinOn
     */
    public JoinOn<QL, QR, JB> onLeft(Function<QL, BaseSegment<?, QL>> l) {
        this.onBuilder.on(l.apply(this.onLeft).end().getWrapperData().getWhereSql());
        return this;
    }

    /**
     * 右表固定关联关系
     *
     * @param r 右查询条件
     * @return JoinOn
     */
    public JoinOn<QL, QR, JB> onRight(Function<QR, BaseSegment<?, QR>> r) {
        this.onBuilder.on(r.apply(this.onRight).end().getWrapperData().getWhereSql());
        return this;
    }

    /**
     * 添加关联关系
     *
     * @return JoinOn
     */
    public JoinOn<QL, QR, JB> onApply(String condition) {
        this.onBuilder.on(condition);
        return this;
    }

    /**
     * 关联关系设置
     *
     * @param l 左关联字段
     * @param r 右关联字段
     * @return JoinOn
     */
    public JoinOn<QL, QR, JB> on(FieldMapping l, FieldMapping r) {
        this.onBuilder.on(l.column, r.column);
        return this;
    }

    /**
     * 结束关联设置
     *
     * @return JoinBuilder
     */
    public JB endJoin() {
        this.joinQuery.getWrapperData().addTable(this.onBuilder.table());
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