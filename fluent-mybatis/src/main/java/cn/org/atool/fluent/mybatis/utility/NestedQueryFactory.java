package cn.org.atool.fluent.mybatis.utility;

import cn.org.atool.fluent.mybatis.base.IQuery;
import cn.org.atool.fluent.mybatis.exception.FluentMybatisException;
import cn.org.atool.fluent.mybatis.segment.model.Parameters;

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
    final static Map<Class<? extends IQuery>, Constructor> Query_Constructor = new ConcurrentHashMap<>();

    /**
     * 构造嵌套查询对象
     *
     * @param klass      嵌套查询对象类
     * @param parameters 查询参量
     * @return 嵌套查询对象
     */
    public static <Q extends IQuery> Q nested(Class klass, Parameters parameters) {
        if (!Query_Constructor.containsKey(klass)) {
            try {
                Constructor constructor = klass.getConstructor(Parameters.class);
                Query_Constructor.put(klass, constructor);
            } catch (Exception e) {
                throw new FluentMybatisException("create nested Query[" + klass.getName() + "] error.", e);
            }
        }
        try {
            return (Q) Query_Constructor.get(klass).newInstance(parameters);
        } catch (Exception e) {
            throw new FluentMybatisException("create nested Query[" + klass.getName() + "] error.", e);
        }
    }
}