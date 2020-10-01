package cn.org.atool.fluent.mybatis.mapper;

import cn.org.atool.fluent.mybatis.base.IWrapper;
import cn.org.atool.fluent.mybatis.method.metadata.DbType;
import cn.org.atool.fluent.mybatis.segment.model.WrapperData;

import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import static cn.org.atool.fluent.mybatis.method.model.XmlConstant.*;
import static java.util.stream.Collectors.joining;

public class MapperUtils {

    public static final String Double_Quota_Str = String.valueOf('"');

    public static String Param_Map = "map";

    public static String Param_Id = "id";

    public static String Param_Ids = "ids";

    public static String Param_Coll = "coll";

    public static String Param_List = "list";

    public static String Param_CM = "cm";

    public static String Param_EW = "ew";

    public static String Param_ET = "et";

    public static String Param_Query = "query";

    public static String Param_Entity = "entity";

    public static WrapperData getWrapperData(Map map, String paraName) {
        IWrapper wrapper = (IWrapper) map.get(paraName);
        if (wrapper == null) {
            throw new RuntimeException("param[" + paraName + "] not found.");
        }
        if (wrapper == null || wrapper.getWrapperData() == null) {
            throw new RuntimeException("no query condition found.");
        }
        return wrapper.getWrapperData();
    }

    public static WrapperData getWrapperData(IWrapper wrapper) {
        if (wrapper == null || wrapper.getWrapperData() == null) {
            throw new RuntimeException("no query condition found.");
        }
        return wrapper.getWrapperData();
    }

    public static <O> O getParas(Map map, String paraName) {
        Object obj = map.get(paraName);
        if (obj == null) {
            throw new RuntimeException("param[" + paraName + "] not found.");
        }
        return (O) obj;
    }

    /**
     * 表达式 #{prefix[index].property} 构建
     * "#{" + prefix + "[" + index+ "]." + property + "}"
     *
     * @param prefix
     * @param property
     * @param index
     * @return
     */
    public static String listIndexEl(String prefix, String property, String index) {
        return new StringBuilder()
            .append(Double_Quota_Str)
            .append("#{")
            .append(prefix).append("[")
            .append(Double_Quota_Str)
            .append(" + ").append(index).append(" + ")
            .append(Double_Quota_Str)
            .append("].").append(property)
            .append("}")
            .append(Double_Quota_Str).toString();
    }

    /**
     * 不同数据库分页查询
     *
     * @param dbType 数据库类型
     * @param sql    非分页查询sql
     * @return
     */
    public static String byPaged(DbType dbType, WrapperData data, String sql) {
        if (data.getPaged() == null) {
            return sql;
        }
        switch (dbType) {
            case ORACLE:
                return new StringBuilder(sql.length() + 200)
                    .append("SELECT * FROM ( ")
                    .append(" SELECT TMP_PAGE.*, ROWNUM ROW_ID FROM ( ")
                    .append(sql)
                    .append(" ) TMP_PAGE)")
                    .append(String.format(" WHERE ROW_ID > %s AND ROW_ID <= %s ", Wrapper_Paged_Offset, Wrapper_Paged_End_Offset))
                    .toString();
            case DB2:
                return new StringBuilder(sql.length() + 200)
                    .append("SELECT * FROM (SELECT TMP_PAGE.*,ROWNUMBER() OVER() AS ROW_ID FROM ( ")
                    .append(sql)
                    .append(" ) AS TMP_PAGE) TMP_PAGE WHERE ROW_ID")
                    .append(String.format(" BETWEEN %s AND %s", Wrapper_Paged_Offset, Wrapper_Paged_Size))
                    .toString();
            case SQL_SERVER:
            case SQL_SERVER2005:
                throw new RuntimeException("not support");
            case HSQL:
                return sql + String.format(" LIMIT %s OFFSET %s", Wrapper_Paged_Size, Wrapper_Paged_Offset);
            case MYSQL:
            case MARIADB:
            case SQLITE:
            case POSTGRE_SQL:
            case H2:
            default:
                return sql + String.format(" LIMIT %s, %s ", Wrapper_Paged_Offset, Wrapper_Paged_Size);
        }
    }
}