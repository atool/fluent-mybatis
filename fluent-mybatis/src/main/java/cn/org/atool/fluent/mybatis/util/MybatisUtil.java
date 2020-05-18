package cn.org.atool.fluent.mybatis.util;

import com.baomidou.mybatisplus.core.metadata.TableFieldInfo;
import com.baomidou.mybatisplus.core.metadata.TableInfoHelper;
import com.baomidou.mybatisplus.core.toolkit.StringPool;

import java.util.function.Predicate;

public class MybatisUtil {
    public final static String DISTINCT = " distinct ";

    public static String distinct(String... columns) {
        return DISTINCT + String.join(StringPool.COMMA, columns);
    }

    public static String distinct(Class entityClass, Predicate<TableFieldInfo> predicate) {
        return DISTINCT + TableInfoHelper.getTableInfo(entityClass).chooseSelect(predicate);
    }
}
