package cn.org.atool.fluent.mybatis.utility;

import cn.org.atool.fluent.mybatis.base.crud.IWrapper;
import cn.org.atool.fluent.mybatis.metadata.DbType;
import cn.org.atool.fluent.mybatis.segment.model.WrapperData;

import java.util.Map;

/**
 * SqlProvider帮助类
 *
 * @author wudarui
 */
@SuppressWarnings({"unchecked", "rawtypes"})
public class SqlProviderUtils {
    public static WrapperData getWrapperData(Map map, String paraName) {
        IWrapper wrapper = getWrapper(map, paraName);
        if (wrapper.getWrapperData() == null) {
            throw new RuntimeException("no query condition found.");
        }
        return wrapper.getWrapperData();
    }

    public static IWrapper getWrapper(Map map, String paraName) {
        IWrapper wrapper = (IWrapper) map.get(paraName);
        if (wrapper == null) {
            throw new RuntimeException("param[" + paraName + "] not found.");
        }
        return wrapper;
    }

    public static <O> O getParas(Map map, String paraName) {
        Object obj = map.get(paraName);
        if (obj == null) {
            throw new RuntimeException("param[" + paraName + "] not found.");
        }
        return (O) obj;
    }

    /**
     * 不同数据库分页查询
     *
     * @param dbType 数据库类型
     * @param sql    非分页查询sql
     * @return sql segment
     */
    public static String byPaged(DbType dbType, WrapperData data, String sql) {
        if (data.getPaged() == null) {
            return sql;
        } else {
            String pagedOffset = data.getParameters().putParameter(null, data.getPaged().getOffset());
            String pagedEndOffset = data.getParameters().putParameter(null, data.getPaged().getEndOffset());
            String pagedSize = data.getParameters().putParameter(null, data.getPaged().getLimit());
            return dbType.paged(sql, pagedOffset, pagedSize, pagedEndOffset);
        }
    }
}