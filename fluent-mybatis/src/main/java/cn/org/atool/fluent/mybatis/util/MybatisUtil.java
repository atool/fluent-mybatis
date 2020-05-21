package cn.org.atool.fluent.mybatis.util;

import cn.org.atool.fluent.mybatis.metadata.FieldInfo;
import cn.org.atool.fluent.mybatis.metadata.TableHelper;

import java.util.function.Predicate;

public class MybatisUtil {
    public final static String DISTINCT = " distinct ";

    public static String distinct(String... columns) {
        return DISTINCT + String.join(Constants.COMMA, columns);
    }

    public static String distinct(Class entityClass, Predicate<FieldInfo> predicate) {
        return DISTINCT + TableHelper.getTableInfo(entityClass).chooseSelect(predicate);
    }
}