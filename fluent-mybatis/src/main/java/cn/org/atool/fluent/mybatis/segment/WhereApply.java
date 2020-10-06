package cn.org.atool.fluent.mybatis.segment;

import cn.org.atool.fluent.mybatis.base.IQuery;
import cn.org.atool.fluent.mybatis.base.model.SqlOp;
import cn.org.atool.fluent.mybatis.exception.FluentMybatisException;
import cn.org.atool.fluent.mybatis.segment.where.BooleanWhere;
import cn.org.atool.fluent.mybatis.segment.where.NumericWhere;
import cn.org.atool.fluent.mybatis.segment.where.ObjectWhere;
import cn.org.atool.fluent.mybatis.segment.where.StringWhere;
import cn.org.atool.fluent.mybatis.utility.NestedQueryFactory;

import java.util.Collection;
import java.util.function.Function;
import java.util.stream.Stream;

import static cn.org.atool.fluent.mybatis.base.model.SqlOp.*;
import static cn.org.atool.fluent.mybatis.utility.MybatisUtil.*;

/**
 * AndObject
 *
 * @param <WHERE> 条件设置器
 * @param <NQ>    对应的嵌套查询器
 * @author darui.wu
 */
public class WhereApply<
    WHERE extends WhereBase<WHERE, ?, NQ>,
    NQ extends IQuery<?, NQ>
    >
    extends BaseApply<WHERE, NQ>
    implements
    ObjectWhere<WHERE, NQ>,
    NumericWhere<WHERE, NQ>,
    StringWhere<WHERE, NQ>,
    BooleanWhere<WHERE, NQ> {

    public WhereApply(WHERE where) {
        super(where);
    }

    @Override
    public <O> WHERE apply(SqlOp op, O... args) {
        if (op.getArgSize() > 0) {
            assertNotEmpty(current.name, args);
            if (args.length != op.getArgSize()) {
                throw new FluentMybatisException(op.getArgSize() + " parameters are required, but " + args.length + " is passed in");
            }
            Stream.of(args).forEach(arg -> assertNotNull(current.name, arg));
        }
        if (op.getArgSize() == -1) {
            assertNotEmpty(current.name, args);
        }
        if (op == IN && args.length == 1) {
            return this.segment.apply(current.column, EQ, args[0]);
        } else {
            return this.segment.apply(current.column, op, args);
        }
    }

    public <O> WHERE apply(boolean condition, SqlOp op, O... args) {
        return condition ? this.apply(op, args) : segment;
    }

    /**
     * in (values)
     *
     * @param condition 为真时成立
     * @param values    条件值
     * @return 查询器或更新器
     */
    public WHERE in(boolean condition, Object[] values) {
        return condition ? this.in(values) : this.segment;
    }

    /**
     * in (values)
     *
     * @param condition 为真时成立
     * @param values    条件值
     * @return 查询器或更新器
     */
    public WHERE in(boolean condition, Collection values) {
        return condition ? this.in(values) : this.segment;
    }

    /**
     * where column IN (select ... )
     *
     * @param select 子查询语句
     * @param args   子查询语句参数，对应select语句里面的 "?" 占位符
     * @return 查询器或更新器
     */
    public <O> WHERE in(String select, O... args) {
        if (isCollection(args)) {
            return this.segment.apply(current.column, select, IN, ((Collection) args[0]).toArray());
        } else {
            return this.segment.apply(current.column, select, IN, args);
        }
    }

    /**
     * in (select ... )
     *
     * @param query 嵌套查询
     * @return 查询器或更新器
     */
    public WHERE in(Function<NQ, NQ> query) {
        return (WHERE) this.in(this.segment.queryClass(), query);
    }

    /**
     * in (select ... )
     *
     * @param klass 嵌套查询类
     * @param query 嵌套查询
     * @param <NQ>  嵌套查询类
     * @return 查询器或更新器
     */
    public <NQ extends IQuery> WHERE in(Class<NQ> klass, Function<NQ, NQ> query) {
        NQ nested = NestedQueryFactory.nested(klass, this.segment.getParameters());
        query.apply(nested);
        return this.segment.apply(current.column, nested.getWrapperData().getQuerySql(), IN);
    }

    /**
     * not in (values)
     *
     * @param condition 为真时成立
     * @param values    条件值
     * @return 查询器或更新器
     */
    public WHERE notIn(boolean condition, Object[] values) {
        return condition ? this.notIn(values) : this.segment;
    }

    /**
     * not in (values)
     *
     * @param condition 为真时成立
     * @param values    条件值
     * @return 查询器或更新器
     */
    public WHERE notIn(boolean condition, Collection values) {
        return condition ? this.notIn(values) : this.segment;
    }

    /**
     * not in (select ... )
     *
     * @param query 嵌套查询
     * @return 查询器或更新器
     */
    public WHERE notIn(Function<NQ, NQ> query) {
        return (WHERE) this.notIn(this.segment.queryClass(), query);
    }

    /**
     * not in (select ... )
     *
     * @param queryClass 嵌套查询类
     * @param query      嵌套查询
     * @param <NQ>       嵌套查询类
     * @return 查询器或更新器
     */
    public <NQ extends IQuery<?, NQ>> WHERE notIn(Class<NQ> queryClass, Function<NQ, NQ> query) {
        NQ nested = NestedQueryFactory.nested(queryClass, this.segment.getParameters());
        query.apply(nested);
        return this.segment.apply(current.column, nested.getWrapperData().getQuerySql(), NOT_IN);
    }

    /**
     * where 自定义条件(包括操作符在内）
     * 比如 where.age().apply("=34").end()
     * <p>
     * ！！！慎用！！！！
     * 有sql注入风险
     *
     * @param opArgs 自定义比较语句
     * @return 查询器或更新器
     */
    public WHERE apply(String opArgs) {
        return this.segment.apply(current.column, opArgs, RETAIN);
    }
}