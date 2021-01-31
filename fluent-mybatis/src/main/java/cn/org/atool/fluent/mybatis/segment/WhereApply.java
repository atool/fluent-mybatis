package cn.org.atool.fluent.mybatis.segment;

import cn.org.atool.fluent.mybatis.Ifs;
import cn.org.atool.fluent.mybatis.base.crud.IBaseQuery;
import cn.org.atool.fluent.mybatis.base.model.SqlOp;
import cn.org.atool.fluent.mybatis.exception.FluentMybatisException;
import cn.org.atool.fluent.mybatis.functions.QFunction;
import cn.org.atool.fluent.mybatis.model.IfsPredicate;
import cn.org.atool.fluent.mybatis.segment.where.BooleanWhere;
import cn.org.atool.fluent.mybatis.segment.where.NumericWhere;
import cn.org.atool.fluent.mybatis.segment.where.ObjectWhere;
import cn.org.atool.fluent.mybatis.segment.where.StringWhere;
import cn.org.atool.fluent.mybatis.utility.NestedQueryFactory;

import java.util.Collection;
import java.util.function.Predicate;
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
    NQ extends IBaseQuery<?, NQ>
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
            assertNotEmpty(this.current().name, args);
            if (args.length != op.getArgSize()) {
                throw new FluentMybatisException(op.getArgSize() + " parameters are required, but " + args.length + " is passed in");
            }
            Stream.of(args).forEach(arg -> assertNotNull(this.current().name, arg));
        }
        if (op.getArgSize() == -1) {
            assertNotEmpty(this.current().name, args);
        }
        if (op == IN && args.length == 1) {
            Object value = args[0];
            if (value instanceof Collection) {
                Object[] arr = ((Collection) value).toArray();
                assertNotEmpty(this.current().name, arr);
                if (arr.length > 1) {
                    return this.segment.apply(this.current(), IN, arr);
                } else {
                    value = arr[0];
                }
            }
            return this.segment.apply(this.current(), EQ, value);
        } else {
            return this.segment.apply(this.current(), op, args);
        }
    }

    @Override
    public <T> WHERE apply(SqlOp op, Ifs<T> ifs) {
        /** 重载（实际入参为null）时兼容处理 **/
        if (ifs == null) {
            return this.apply(op, (Object) null);
        }
        if (op.getArgSize() > 1) {
            throw new IllegalArgumentException("Ifs condition does not apply to the operation:" + op.name());
        }
        for (IfsPredicate<Predicate, Object> predicate : ifs.predicates) {
            Object value = predicate.value(op);
            if (predicate.predicate.test(value)) {
                return this.apply(op, value);
            }
        }
        return segment;
    }

    @Override
    public <O> WHERE apply(boolean condition, SqlOp op, O... args) {
        return condition ? this.apply(op, args) : segment;
    }

    /**
     * where column IN (select ... )
     *
     * @param select 子查询语句
     * @param args   子查询语句参数，对应select语句里面的 "?" 占位符
     * @return 查询器或更新器
     */
    @Override
    public <O> WHERE in(String select, O... args) {
        if (isCollection(args)) {
            return this.segment.apply(this.current(), IN, select, ((Collection) args[0]).toArray());
        } else {
            return this.segment.apply(this.current(), IN, select, args);
        }
    }

    @Override
    public <O> WHERE in(boolean condition, String select, O... args) {
        return condition ? this.in(select, args) : this.segment;
    }

    /**
     * in (select ... )
     *
     * @param query 嵌套查询
     * @return 查询器或更新器
     */
    @Override
    public WHERE in(QFunction<NQ> query) {
        return (WHERE) this.in(this.segment.queryClass(), query);
    }

    @Override
    public WHERE in(boolean condition, QFunction<NQ> query) {
        return condition ? this.in(query) : this.segment;
    }

    /**
     * in (select ... )
     *
     * @param klass 嵌套查询类
     * @param query 嵌套查询
     * @param <NQ>  嵌套查询类
     * @return 查询器或更新器
     */
    @Override
    public <NQ extends IBaseQuery> WHERE in(Class<NQ> klass, QFunction<NQ> query) {
        NQ nested = NestedQueryFactory.nested(klass, this.segment.getParameters());
        query.apply(nested);
        return this.segment.apply(this.current(), IN, nested.getWrapperData().getQuerySql());
    }

    @Override
    public <NQ extends IBaseQuery> WHERE in(boolean condition, Class<NQ> klass, QFunction<NQ> query) {
        return condition ? this.in(klass, query) : this.segment;
    }

    /**
     * not in (select ... )
     *
     * @param query 嵌套查询
     * @return 查询器或更新器
     */
    @Override
    public WHERE notIn(QFunction<NQ> query) {
        return (WHERE) this.notIn(this.segment.queryClass(), query);
    }

    @Override
    public WHERE notIn(boolean condition, QFunction<NQ> query) {
        return condition ? this.notIn(query) : this.segment;
    }

    /**
     * not in (select ... )
     *
     * @param queryClass 嵌套查询类
     * @param query      嵌套查询
     * @param <NQ>       嵌套查询类
     * @return 查询器或更新器
     */
    @Override
    public <NQ extends IBaseQuery<?, NQ>> WHERE notIn(Class<NQ> queryClass, QFunction<NQ> query) {
        NQ nested = NestedQueryFactory.nested(queryClass, this.segment.getParameters());
        query.apply(nested);
        return this.segment.apply(this.current(), NOT_IN, nested.getWrapperData().getQuerySql());
    }

    @Override
    public <NQ extends IBaseQuery<?, NQ>> WHERE notIn(
        boolean condition, Class<NQ> queryClass, QFunction<NQ> query
    ) {
        return condition ? this.notIn(queryClass, query) : this.segment;
    }

    @Override
    public WHERE apply(String opArgs) {
        return this.segment.apply(this.current(), RETAIN, opArgs);
    }

    @Override
    public WHERE applyFunc(SqlOp op, String expression, Object... args) {
        return this.segment.apply(this.current(), op, expression, args);
    }

    @Override
    public WHERE applyFunc(boolean condition, SqlOp op, String expression, Object... args) {
        if (condition) {
            this.apply(op, expression, args);
        }
        return this.segment.and;
    }
}