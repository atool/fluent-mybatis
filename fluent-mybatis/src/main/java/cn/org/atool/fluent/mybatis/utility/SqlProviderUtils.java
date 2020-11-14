package cn.org.atool.fluent.mybatis.utility;

import cn.org.atool.fluent.mybatis.base.crud.IWrapper;
import cn.org.atool.fluent.mybatis.metadata.DbType;
import cn.org.atool.fluent.mybatis.segment.model.WrapperData;

import java.util.Map;

import static cn.org.atool.fluent.mybatis.mapper.FluentConst.Double_Quota_Str;
import static cn.org.atool.fluent.mybatis.mapper.FluentConst.Param_EW;
import static java.lang.String.format;

/**
 * SqlProvider帮助类
 *
 * @author wudarui
 */
public class SqlProviderUtils {
    /**
     * 变量在xml文件中的占位符全路径表达式
     * 例子: #{ew.wrapperData.parameters.variable_1}
     */
    public static final String WRAPPER_PARAM_FORMAT = "#{%s.parameters.%s}";

    public static final String Spec_Comment_Not_Null = "SPEC_COMMENT != null and SPEC_COMMENT != ''";

    public static final String Wrapper_Data = format("%s.wrapperData", Param_EW);

    public static final String Wrapper_Exists = format("%s != null", Wrapper_Data);

    public static final String Wrapper_Page_Is_Null = format("%s.paged == null", Wrapper_Data);

    public static final String Wrapper_Page_Not_Null = format("%s.paged != null", Wrapper_Data);

    public static final String Wrapper_Distinct_True = format("%s.distinct", Wrapper_Data);

    public static final String Wrapper_Select_Not_Null = format("%s.sqlSelect != null", Wrapper_Data);

    public static final String Wrapper_Select_Var = format("${%s.sqlSelect}", Wrapper_Data);

    public static final String Wrapper_UpdateStr_Not_Null = format("%s.updateStr != null", Wrapper_Data);

    public static final String Wrapper_UpdateStr_Var = format("${%s.updateStr}", Wrapper_Data);

    public static final String Wrapper_Update_Contain_Key = format("%s.updates.containsKey('@column') == false", Wrapper_Data);

    public static final String Wrapper_Where_NotNull = format("%s.whereSql != null", Wrapper_Data);

    public static final String Wrapper_Where_Var = format("${%s.whereSql}", Wrapper_Data);

    public static final String Wrapper_GroupBy_NotNull = format("%s.groupBy != null", Wrapper_Data);

    public static final String Wrapper_GroupBy_Var = format("${%s.groupBy}", Wrapper_Data);

    public static final String Wrapper_OrderBy_NotNull = format("%s.orderBy != null", Wrapper_Data);

    public static final String Wrapper_OrderBy_Var = format("${%s.orderBy}", Wrapper_Data);

    public static final String Wrapper_LastSql_NotNull = format("%s.lastSql != null", Wrapper_Data);

    public static final String Wrapper_LastSql_Var = format("${%s.lastSql}", Wrapper_Data);

    public static final String Wrapper_Paged_Offset = format("#{%s.paged.offset}", Wrapper_Data);

    public static final String Wrapper_Paged_Size = format("#{%s.paged.limit}", Wrapper_Data);

    public static final String Wrapper_Paged_End_Offset = format("#{%s.paged.endOffset}", Wrapper_Data);

    public static WrapperData getWrapperData(Map map, String paraName) {
        IWrapper wrapper = (IWrapper) map.get(paraName);
        if (wrapper == null) {
            throw new RuntimeException("param[" + paraName + "] not found.");
        }
        if (wrapper.getWrapperData() == null) {
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
