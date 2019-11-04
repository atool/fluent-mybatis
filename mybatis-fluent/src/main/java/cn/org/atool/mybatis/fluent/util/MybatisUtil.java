package cn.org.atool.mybatis.fluent.util;

import cn.org.atool.mybatis.fluent.exception.NullParameterException;
import com.baomidou.mybatisplus.core.metadata.TableFieldInfo;
import com.baomidou.mybatisplus.core.metadata.TableInfo;
import com.baomidou.mybatisplus.core.toolkit.*;
import com.baomidou.mybatisplus.core.toolkit.sql.SqlScriptUtils;
import com.baomidou.mybatisplus.core.toolkit.support.ColumnCache;
import com.baomidou.mybatisplus.core.toolkit.support.SFunction;
import com.baomidou.mybatisplus.core.toolkit.support.SerializedLambda;

import java.io.Serializable;
import java.util.*;
import java.util.function.Function;
import java.util.function.Predicate;

import static com.baomidou.mybatisplus.core.toolkit.StringPool.NEWLINE;
import static java.util.stream.Collectors.joining;
import static java.util.stream.Collectors.toList;

public class MybatisUtil {
    public final static String DISTINCT = " distinct ";

    private final static String AVAILABLE_UPT = new StringBuilder()
            .append("<if test=\"%s.containsKey('%s')\">")
            .append("%s = #{%s.%s},")
            .append("</if>")
            .toString();

    private final static String AVAILABLE_WHERE = new StringBuilder()
            .append("<if test=\"%s.containsKey('%s')\">")
            .append("<choose>")
            .append("<when test\"%s.%s==null\">and %s is null</when>")
            .append("<otherwise>and %s = #{%s.%s}</otherwise>")
            .append("</choose>")
            .append("</if>")
            .toString();

    private final static String VERSION_EQUAL = new StringBuilder()
            .append("<if test=\"et instanceof java.util.Map\">")
            .append("<if test=\"et.MP_OPTLOCK_VERSION_ORIGINAL != null\">")
            .append(" AND ${et.MP_OPTLOCK_VERSION_COLUMN} = #{et.MP_OPTLOCK_VERSION_ORIGINAL}")
            .append("</if></if>")
            .toString();

    private final static String IF_MAP_NOT_EMPTY = "%s != null and !%s.isEmpty";

    public static String distinct(String... columns) {
        return DISTINCT + String.join(StringPool.COMMA, columns);
    }

    public static String distinct(Class entityClass, Predicate<TableFieldInfo> predicate) {
        return DISTINCT + TableInfoHelper.getTableInfo(entityClass).chooseSelect(predicate);
    }

    private static String getAvailableSetField(String entity, String property, String column) {
        return String.format(AVAILABLE_UPT,
                entity, property,
                column, entity, property);
    }

    private static String availableWhereField(String entity, String property, String column) {
        return String.format(AVAILABLE_WHERE,
                entity, property,
                entity, property, column,
                column, entity, property
        );
    }

    /**
     * SQL 更新set语句
     *
     * @param table 表信息
     * @param alias
     * @return
     */
    public static String getSetByMap(TableInfo table, String alias) {
        String setSQL = table.getFieldList().stream()
                .filter(field -> !field.getColumn().equals(table.getKeyColumn()))
                .map(field -> getAvailableSetField(alias, field.getProperty(), field.getColumn()))
                .collect(joining(NEWLINE));
        return String.format("<set><if test=\"%s\">%s</if></set>", ifMapNotEmptyCond(alias), setSQL);
    }

    /**
     * map不为空判断条件
     *
     * @param alias
     * @return
     */
    private static String ifMapNotEmptyCond(String alias) {
        return String.format(IF_MAP_NOT_EMPTY, alias, alias);
    }

    /**
     * SQL map查询条件
     *
     * @param table
     * @param alias
     * @return
     */
    public static String sqlWhereByMap(TableInfo table, String alias) {
        String whereSQL = table.getFieldList().stream()
                .map(field -> availableWhereField(alias, field.getProperty(), field.getColumn()))
                .collect(joining(NEWLINE));
        String whereId = availableWhereField(alias, table.getKeyProperty(), table.getKeyColumn());
        return String.format("<where><if test=\"%s\">%s\n%s</if></where>", ifMapNotEmptyCond(alias), whereId, whereSQL);
    }

    public static String getPartitionDatabase() {
        return SqlScriptUtils.convertIf("/*TDDL:node='${pdb}'*/", "pdb!=null and pdb!=''", false);
    }

    public static String getPartitionTable(String table) {
        return SqlScriptUtils.convertChoose("ptb != null and ptb != ''", "${ptb}", table);
    }

    /**
     * 将values中非null对象转换为List
     *
     * @param values
     * @return
     */
    public static List listNotNull(Object[] values) {
        if (values == null || values.length == 0) {
            return new ArrayList();
        } else {
            return Arrays.stream(values)
                    .filter(value -> value != null)
                    .collect(toList());
        }
    }

    /**
     * 返回lambda表达式对应的属性名称
     *
     * @param function
     * @param <T>
     * @return
     */
    public static <T> String findProperty(SFunction<T, ?> function) {
        if (!(function instanceof Serializable)) {
            return null;
        } else {
            SerializedLambda lambda = LambdaUtils.resolve(function);
            return StringUtils.resolveFieldName(lambda.getImplMethodName());
        }
    }

    /**
     * 返回lambda表达式对应属性所对应的数据库字段
     *
     * @param function
     * @param <T>
     * @return
     */
    public static <T> String findColumn(SFunction<T, ?> function) {
        return findColumn(function, (fieldName) -> {
            throw ExceptionUtils.mpe("you get property method name '%s' cannot find the corresponding database column name!"
                    , fieldName);
        });
    }

    private static <T> String findColumn(SFunction<T, ?> function, Function<String, String> unExistFunction) {
        if (!(function instanceof Serializable)) {
            return null;
        } else {
            SerializedLambda lambda = LambdaUtils.resolve(function);
            String fieldName = StringUtils.resolveFieldName(lambda.getImplMethodName());
            Map<String, ColumnCache> columnMap = LambdaUtils.getColumnMap((lambda.getImplClassName()));

            return Optional.ofNullable(columnMap.get(fieldName.toUpperCase(Locale.ENGLISH)))
                    .map(ColumnCache::getColumn)
                    .orElseGet(() -> unExistFunction.apply(fieldName));
        }
    }

    /**
     * 返回lambda表达式对应属性对应的数据库字段
     *
     * @param function
     * @param <T>
     * @return
     */
    public static <T> String findColumnIfExist(SFunction<T, ?> function) {
        return findColumn(function, (fieldName) -> null);
    }

    /**
     * 断言对象不能为null
     *
     * @param property
     * @param value
     */
    public static void assertNotNull(String property, Object value) {
        if (value == null) {
            throw new NullParameterException("the parameter[" + property + "] can't be null.");
        }
    }

    /**
     * 断言对象不能为null
     *
     * @param property
     * @param value1
     * @param value2
     * @param <T>
     */
    public static <T> void assertNotNull(String property, T value1, T value2) {
        if (value1 == null || value2 == null) {
            throw new NullParameterException("the parameter[" + property + "] can't be null.");
        }
    }

    /**
     * 断言字符串不能为空
     *
     * @param property
     * @param value
     */
    public static void assertNotBlank(String property, String value) {
        if (StringUtils.isEmpty(value)) {
            throw new NullParameterException("the parameter[" + property + "] can't be blank.");
        }
    }

    /**
     * 断言list参数不能为空
     *
     * @param property
     * @param list
     */
    public static void assertNotEmpty(String property, Collection list) {
        if (list == null || list.size() == 0) {
            throw new NullParameterException("the parameter[" + property + "] can't be empty.");
        }
    }

    /**
     * 断言数组array参数不能为空
     *
     * @param property
     * @param array
     */
    public static void assertNotEmpty(String property, Object[] array) {
        if (array == null || array.length == 0) {
            throw new NullParameterException("the parameter[" + property + "] can't be empty.");
        }
    }
}
