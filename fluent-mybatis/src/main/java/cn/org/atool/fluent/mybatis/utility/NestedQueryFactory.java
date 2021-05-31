package cn.org.atool.fluent.mybatis.utility;

import cn.org.atool.fluent.mybatis.base.crud.IBaseQuery;
import cn.org.atool.fluent.mybatis.base.splice.FreeQuery;
import cn.org.atool.fluent.mybatis.exception.FluentMybatisException;
import cn.org.atool.fluent.mybatis.segment.BaseWrapper;

import java.lang.reflect.Constructor;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * NestedQueryFactory
 *
 * @author darui.wu
 * @create 2020/6/19 8:34 下午
 */
public class NestedQueryFactory {
    final static Map<Class<? extends IBaseQuery>, Constructor> Query_Constructor = new ConcurrentHashMap<>();

    /**
     * 构造查询对象
     *
     * @param klass 嵌套查询对象类
     * @return 嵌套查询对象
     */
    public static <Q extends IBaseQuery> Q nested(Class klass, BaseWrapper wrapper) {
        if (FreeQuery.class.isAssignableFrom(klass)) {
            return (Q) new FreeQuery(wrapper.getTable(), wrapper.getTableAlias());
        }
        if (!Query_Constructor.containsKey(klass)) {
            try {
                Constructor constructor = klass.getConstructor();
                Query_Constructor.put(klass, constructor);
            } catch (Exception e) {
                throw new FluentMybatisException("create nested Query[" + klass.getName() + "] error.", e);
            }
        }
        try {
            Q query = (Q) Query_Constructor.get(klass).newInstance();
            return query;
        } catch (Exception e) {
            throw new FluentMybatisException("create nested Query[" + klass.getName() + "] error.", e);
        }
    }
}