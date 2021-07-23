package cn.org.atool.fluent.mybatis.segment;

import cn.org.atool.fluent.mybatis.base.IEntity;
import cn.org.atool.fluent.mybatis.base.crud.BaseQuery;
import cn.org.atool.fluent.mybatis.base.crud.IQuery;
import cn.org.atool.fluent.mybatis.base.model.FieldMapping;
import cn.org.atool.fluent.mybatis.base.splice.FreeQuery;
import cn.org.atool.fluent.mybatis.functions.GetterFunc;
import cn.org.atool.fluent.mybatis.metadata.JoinType;
import cn.org.atool.fluent.mybatis.segment.model.Parameters;
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
@SuppressWarnings({"unchecked", "rawtypes"})
public class JoinOn<QL extends BaseQuery<?, QL>, QR extends BaseQuery<?, QR>, JB> {
    private final JoinQuery<QL> joinQuery;

    private final QL onLeft;

    private final QR onRight;

    private final JoinOnBuilder<QL, QR> onBuilder;

    public JoinOn(JoinQuery<QL> joinQuery, Class<QL> qLeftClass, QL qLeft, JoinType joinType, Class<QR> qRightClass, QR qRight) {
        this.joinQuery = joinQuery;
        this.onBuilder = new JoinOnBuilder<>(qLeft, joinType, qRight);
        /* 初始化左查询关联 */
        this.onLeft = this.emptyQuery(qLeftClass, qLeft);
        /* 初始化右查询关联 */
        this.onRight = this.emptyQuery(qRightClass, qRight);
    }

    private <Q extends BaseQuery> Q emptyQuery(Class qClass, BaseQuery oldQuery) {
        BaseQuery onQuery;
        if (oldQuery instanceof FreeQuery) {
            onQuery = ((FreeQuery) oldQuery).emptyQuery();
        } else {
            onQuery = newEmptyQuery(qClass);
        }
        onQuery.tableAlias = oldQuery.tableAlias;
        onQuery.sharedParameter(this.joinQuery);
        return (Q) onQuery;
    }

    /**
     * 自由设置连接关系, 设置时需要加上表别名
     * 比如: t1.id = t2.id AND t1.is_deleted = t2.is_deleted AND t1.env = ?
     *
     * @param condition 手工设置的关联关系
     * @param args      关联关系参数列表
     * @return JoinOn
     */
    public JoinOn<QL, QR, JB> onApply(String condition, Object... args) {
        String sql = Parameters.parseSql(this.joinQuery, condition, args);
        this.onBuilder.on(sql);
        return this;
    }

    /**
     * 关联关系设置, on left = right, 各取最后一个属性
     *
     * @param l 左查询条件, 取最后一个属性
     * @param r 右查询条件, 取最后一个属性
     * @return JoinOn
     */
    public JoinOn<QL, QR, JB> on(Function<QL, BaseWhere> l, Function<QR, BaseWhere> r) {
        this.onBuilder.on(l.apply(this.onLeft), r.apply(this.onRight));
        return this;
    }

    /**
     * 关联关系设置 l(left column) = r(right column)
     *
     * @param l 左查询条件
     * @param r 右查询条件
     * @return JoinOn
     */
    public <LE extends IEntity, RE extends IEntity> JoinOn<QL, QR, JB> onEq(GetterFunc<LE> l, GetterFunc<RE> r) {
        Class lKlass = this.onLeft.wrapperData.getEntityClass();
        Class rKlass = this.onRight.wrapperData.getEntityClass();
        assertNotNull("left query entity class", lKlass);
        assertNotNull("right query entity class", rKlass);
        String lField = MappingKits.toColumn(lKlass, l);
        String rField = MappingKits.toColumn(rKlass, r);
        return this.onEq(lField, rField);
    }

    /**
     * 关联关系设置 l(left column) = r(right column)
     *
     * @param l 左关联字段
     * @param r 右关联字段
     * @return JoinOn
     */
    public JoinOn<QL, QR, JB> onEq(String l, String r) {
        this.onBuilder.on(l, r);
        return this;
    }

    /**
     * 关联关系设置 l(left column) = r(right column)
     *
     * @param l 左关联字段
     * @param r 右关联字段
     * @return JoinOn
     */
    public JoinOn<QL, QR, JB> onEq(FieldMapping l, FieldMapping r) {
        this.onBuilder.on(l.column, r.column);
        return this;
    }

    /**
     * 左表固定关联关系
     *
     * @param l 左查询条件
     * @return JoinOn
     */
    public JoinOn<QL, QR, JB> onLeft(Function<QL, BaseSegment<?, QL>> l) {
        return this.onQuery(this.onLeft, l);
    }

    /**
     * 右表固定关联关系
     *
     * @param r 右查询条件
     * @return JoinOn
     */
    public JoinOn<QL, QR, JB> onRight(Function<QR, BaseSegment<?, QR>> r) {
        return this.onQuery(this.onRight, r);
    }

    private JoinOn onQuery(IQuery query, Function func) {
        BaseQuery onQuery = this.emptyQuery(query.getClass(), (BaseQuery) query);
        String sql = ((BaseSegment) func.apply(onQuery)).end().getWrapperData().getWhereSql();
        this.onBuilder.on(sql);
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

    private static final Map<Class, Constructor> QueryNoArgConstructors = new HashMap<>(128);

    /**
     * 执行on条件时, 新创建查询对象, 避免对原有对象的造成干扰
     *
     * @param klass query class
     * @param <Q>   class type
     * @return BaseQuery
     */
    private static <Q extends BaseQuery> Q newEmptyQuery(Class<Q> klass) {
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