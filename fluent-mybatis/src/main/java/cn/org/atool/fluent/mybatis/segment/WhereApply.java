package cn.org.atool.fluent.mybatis.segment;

import cn.org.atool.fluent.mybatis.base.crud.BaseQuery;
import cn.org.atool.fluent.mybatis.base.crud.IBaseQuery;
import cn.org.atool.fluent.mybatis.base.crud.IQuery;
import cn.org.atool.fluent.mybatis.base.model.ISqlOp;
import cn.org.atool.fluent.mybatis.exception.FluentMybatisException;
import cn.org.atool.fluent.mybatis.functions.QFunction;
import cn.org.atool.fluent.mybatis.ifs.Ifs;
import cn.org.atool.fluent.mybatis.ifs.IfsPredicate;
import cn.org.atool.fluent.mybatis.mapper.SqlLike;
import cn.org.atool.fluent.mybatis.segment.fragment.AppendFlag;
import cn.org.atool.fluent.mybatis.segment.fragment.IFragment;
import cn.org.atool.fluent.mybatis.segment.where.BooleanWhere;
import cn.org.atool.fluent.mybatis.segment.where.NumericWhere;
import cn.org.atool.fluent.mybatis.segment.where.ObjectWhere;
import cn.org.atool.fluent.mybatis.segment.where.StringWhere;
import cn.org.atool.fluent.mybatis.utility.NestedQueryFactory;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.function.Predicate;
import java.util.function.Supplier;
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
@SuppressWarnings({"unchecked", "rawtypes"})
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
    public <O> WHERE apply(ISqlOp op, O... args) {
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
                    return this.segment.apply(this.column(), IN, arr);
                } else {
                    value = arr[0];
                }
            }
            return this.segment.apply(this.column(), EQ, value);
        } else {
            return this.segment.apply(this.column(), op, (Object[]) args);
        }
    }

    @Override
    public <T> WHERE apply(ISqlOp op, Ifs<T> ifs) {
        /* 重载（实际入参为null）时兼容处理 **/
        if (ifs == null) {
            return this.apply(op, (Object) null);
        }
        if (op.getArgSize() > 1) {
            throw new IllegalArgumentException("Ifs condition does not apply to the operation:" + op.name());
        }
        for (IfsPredicate predicate : ifs.predicates) {
            if (this.doIfs(op, predicate)) {
                break;
            }
        }
        return segment;
    }

    private boolean doIfs(ISqlOp op, IfsPredicate predicate) {
        Object value = predicate.value(op);
        if (!predicate.predicate.test(value)) {
            return false;
        } else {
            this.apply(op, value);
            return true;
        }
    }

    @Override
    public <O> WHERE apply(Predicate<Object[]> predicate, ISqlOp op, O... args) {
        return predicate.test(args) ? this.apply(op, args) : segment;
    }

    @Override
    public WHERE like(final String value, final Predicate<String> when) {
        if (when.test(value)) {
            return this.apply(LIKE, SqlLike.like(value));
        } else {
            return this.segment;
        }
    }

    @Override
    public WHERE notLike(String value, Predicate<String> when) {
        if (when.test(value)) {
            return this.apply(NOT_LIKE, SqlLike.like(value));
        } else {
            return this.segment;
        }
    }

    @Override
    public WHERE likeLeft(String value, Predicate<String> when) {
        if (when.test(value)) {
            return this.apply(LIKE, SqlLike.left(value));
        } else {
            return this.segment;
        }
    }

    @Override
    public WHERE likeRight(String value, Predicate<String> when) {
        if (when.test(value)) {
            return this.apply(LIKE, SqlLike.right(value));
        } else {
            return this.segment;
        }
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
            return this.segment.apply(this.column(), IN, select, ((Collection) args[0]).toArray());
        } else {
            return this.segment.apply(this.column(), IN, select, (Object[]) args);
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
        NQ nested = NestedQueryFactory.nested(this.segment.wrapper, false);
        query.apply(nested);
        return this.in(nested);
    }

    @Override
    public WHERE in(IQuery query) {
        ((BaseWrapper) query).sharedParameter(this.segment.getParameters());
        return this.segment.apply(this.column(), IN, query.data().sql(false));
    }

    @Override
    public WHERE in(boolean condition, QFunction<NQ> query) {
        return condition ? this.in(query) : this.segment;
    }

    @Override
    public WHERE in(boolean condition, IQuery query) {
        return condition ? this.in(query) : this.segment;
    }

    /**
     * not in (select ... )
     *
     * @param query 嵌套查询
     * @return 查询器或更新器
     */
    @Override
    public WHERE notIn(QFunction<NQ> query) {
        NQ nested = NestedQueryFactory.nested(this.segment.wrapper, false);
        query.apply(nested);
        return this.notIn(nested);
    }

    @Override
    public WHERE notIn(IQuery query) {
        ((BaseWrapper) query).sharedParameter(this.segment.getParameters());
        return this.segment.apply(this.column(), NOT_IN, query.data().sql(false));
    }

    @Override
    public WHERE notIn(boolean condition, QFunction<NQ> query) {
        return condition ? this.notIn(query) : this.segment;
    }

    @Override
    public WHERE notIn(boolean condition, IQuery query) {
        return condition ? this.notIn(query) : this.segment;
    }

    @Override
    public WHERE apply(String opArgs) {
        return this.segment.apply(this.column(), RETAIN, opArgs);
    }

    @Override
    public WHERE applyFunc(ISqlOp op, String expression, Object... args) {
        int prev = 0;
        AppendFlag buff = new AppendFlag();
        List<Object> list = new ArrayList<>();
        for (Object arg : args) {
            int next = expression.indexOf('?', prev);
            if (next < 0) {
                throw new IllegalArgumentException("The number of placeholders and parameters are not equal.");
            }
            buff.append(expression, prev, next);
            if (arg instanceof IQuery) {
                IFragment nest = ((IQuery) arg).data().sql(false);
                buff.append(nest);
                ((BaseQuery) arg).sharedParameter(this.segment.wrapper);
            } else {
                buff.append('?');
                list.add(arg);
            }
            prev = next + 1;
        }
        buff.append(expression, prev, expression.length());
        return this.segment.apply(this.column(), op, buff, list.toArray());
    }

    @Override
    public WHERE applyFunc(Predicate<Object[]> predicate, ISqlOp op, String expression, Object... args) {
        if (predicate.test(args)) {
            this.apply(op, expression, args);
        }
        return this.segment.and;
    }
}