package cn.org.atool.fluent.mybatis.utility;

import cn.org.atool.fluent.mybatis.base.crud.IBaseQuery;
import cn.org.atool.fluent.mybatis.base.entity.IMapping;
import cn.org.atool.fluent.mybatis.base.free.FreeQuery;
import cn.org.atool.fluent.mybatis.exception.FluentMybatisException;
import cn.org.atool.fluent.mybatis.segment.BaseWrapper;

import static cn.org.atool.fluent.mybatis.base.free.FreeKit.newFreeQuery;

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
     * @param wrapper 嵌套查询对象类
     * @return 嵌套查询对象
     */
    public static <Q extends IBaseQuery> Q nested(BaseWrapper wrapper, boolean sameAlias) {
        if (wrapper instanceof FreeQuery) {
            FreeQuery query = newFreeQuery(wrapper);
            return (Q) query;
        }
        IMapping mapping = RefKit.byEntity(wrapper.getEntityClass());
        if (mapping == null) {
            throw new FluentMybatisException("create nested Query[" + wrapper.getClass().getName() + "] error.");
        }
        if (sameAlias) {
            return mapping.emptyQuery(wrapper::getTableAlias);
        } else {
            return mapping.emptyQuery();
        }
    }
}