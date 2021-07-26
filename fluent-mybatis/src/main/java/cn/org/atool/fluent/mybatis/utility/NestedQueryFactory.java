package cn.org.atool.fluent.mybatis.utility;

import cn.org.atool.fluent.mybatis.base.crud.IBaseQuery;
import cn.org.atool.fluent.mybatis.base.splice.FreeQuery;
import cn.org.atool.fluent.mybatis.exception.FluentMybatisException;
import cn.org.atool.fluent.mybatis.segment.BaseWrapper;

/**
 * NestedQueryFactory
 *
 * @author darui.wu 2020/6/19 8:34 下午
 */
@SuppressWarnings({"rawtypes", "unchecked"})
public class NestedQueryFactory {
    /**
     * 构造查询对象
     *
     * @param klass 嵌套查询对象类
     * @return 嵌套查询对象
     */
    public static <Q extends IBaseQuery> Q nested(Class klass, BaseWrapper wrapper, boolean sameAlias) {
        if (FreeQuery.class.isAssignableFrom(klass)) {
            FreeQuery query = new FreeQuery(wrapper.getTable(), sameAlias ? wrapper.getTableAlias() : null);
            query.setDbType(wrapper.dbType());
            return (Q) query;
        }
        try {
            if (sameAlias) {
                return (Q) klass.getConstructor(String.class).newInstance(wrapper.getTableAlias());
            } else {
                return (Q) klass.getConstructor().newInstance();
            }
        } catch (Exception e) {
            throw new FluentMybatisException("create nested Query[" + klass.getName() + "] error.", e);
        }
    }
}