package cn.org.atool.fluent.mybatis.util;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.FieldStrategy;
import com.baomidou.mybatisplus.core.metadata.TableFieldInfo;
import com.baomidou.mybatisplus.core.metadata.TableInfo;
import com.baomidou.mybatisplus.core.toolkit.sql.SqlScriptUtils;
import org.apache.commons.lang3.StringUtils;

import static com.baomidou.mybatisplus.core.toolkit.StringPool.*;
import static java.util.stream.Collectors.joining;

public class MybatisInsertUtil {

    /**
     * 获取 insert 时候字段sql脚本片段
     * <p>
     * insert into table(字段列表)
     * values (对应值列表)
     * </p>
     * <p>根据规则生成if标签</p>
     *
     * @param tableInfo
     * @param mustWithId
     * @return
     */
    public static String getAllInsertSqlColumn(TableInfo tableInfo, boolean mustWithId) {
        String idScript = getIdColumn(tableInfo, mustWithId);
        String other = tableInfo.getFieldList().stream()
                .map(TableFieldInfo::getInsertSqlColumn)
                .collect(joining(NEWLINE));
        return idScript + other;
    }

    private static String getIdColumn(TableInfo tableInfo, boolean mustWithId) {
        if (mustWithId) {
            return tableInfo.getKeyColumn() + COMMA + NEWLINE;
        } else {
            return tableInfo.getKeyInsertSqlColumn(true);
        }
    }

    /**
     * 获取 insert 时候字段sql脚本片段
     * <p>
     * insert into table(字段列表)
     * values (对应值列表)
     * </p>
     * <p>根据规则生成if标签</p>
     *
     * @param field
     * @return
     */
    public static String getInsertSqlColumnMaybeIf(TableFieldInfo field) {
        final String sqlScript = field.getInsertSqlColumn();
        if (field.getFieldStrategy() == FieldStrategy.IGNORED || isFillInsertOrLogicDelete(field)) {
            return sqlScript;
        }
        String property = field.getProperty();
        String condition = String.format("%s != null", property);
        if (field.getFieldStrategy() == FieldStrategy.NOT_EMPTY && field.isCharSequence()) {
            condition = String.format("%s != null and %s != ''", property, property);
        }
        return SqlScriptUtils.convertIf(sqlScript, condition, false);
    }

    /**
     * 获取 insert 时候字段sql脚本片段
     * <p>
     * insert into table(字段列表)
     * values (对应值列表)
     * </p>
     * <p>根据规则生成if标签</p>
     *
     * @param tableInfo
     * @param mustWithId
     * @return
     */
    public static String getAllInsertValueSql(TableInfo tableInfo, boolean mustWithId) {
        String idValue = getIdValue(tableInfo, mustWithId);
        String otherValue = tableInfo.getFieldList().stream()
                .map(MybatisInsertUtil::getInsertValueSql)
                .collect(joining(NEWLINE));
        return idValue + otherValue;
    }

    private static String getIdValue(TableInfo tableInfo, boolean mustWithId) {
        if (mustWithId) {
            return SqlScriptUtils.safeParam(tableInfo.getKeyProperty()) + COMMA + NEWLINE;
        } else {
            return tableInfo.getKeyInsertSqlProperty(EMPTY, true);
        }
    }

    private static String getInsertValueSql(TableFieldInfo field) {
        String sqlScript = field.getInsertSqlProperty(EMPTY);
        if (field.getFieldStrategy() == FieldStrategy.IGNORED) {
            return sqlScript;
        }
        String defaultValue = findDefaultInsertValue(field, EMPTY);
        if (defaultValue == null) {
            return sqlScript;
        }
        String property = field.getProperty();
        String condition = String.format("%s != null", property);
        if (field.getFieldStrategy() == FieldStrategy.NOT_EMPTY && field.isCharSequence()) {
            condition = String.format("%s != null and %s != ''", property, property);
        }
        return SqlScriptUtils.convertChoose(condition, sqlScript, defaultValue);
    }

    /**
     * 返回插入时的默认值
     *
     * @param field
     * @param prefix
     * @return
     */
    private static String findDefaultInsertValue(TableFieldInfo field, String prefix) {
        if (!isFillInsertOrLogicDelete(field)) {
            return "null, " ;
        }
        if (isInsertDefaultField(field)) {
            return field.getUpdate() + COMMA;
        } else {
            return field.getLogicNotDeleteValue() + COMMA;
        }
    }

    /**
     * 是否是insert时默认赋值字段
     *
     * @param field
     * @return
     */
    public static boolean isInsertDefaultField(TableFieldInfo field) {
        if (StringUtils.isEmpty(field.getUpdate())) {
            return false;
        } else {
            return field.getFieldFill() == FieldFill.INSERT || field.getFieldFill() == FieldFill.INSERT_UPDATE;
        }
    }

    /**
     * 字段是insert时，赋默认值，或者逻辑删除字段
     *
     * @param field
     * @return
     */
    public static boolean isFillInsertOrLogicDelete(TableFieldInfo field) {
        return isInsertDefaultField(field) || field.isLogicDelete();
    }

    /**
     * 是否是insert时默认赋值字段
     *
     * @param field
     * @return
     */
    public static boolean isUpdateDefaultField(TableFieldInfo field) {
        if (StringUtils.isEmpty(field.getUpdate())) {
            return false;
        } else {
            return field.getFieldFill() == FieldFill.UPDATE || field.getFieldFill() == FieldFill.INSERT_UPDATE;
        }
    }

    /**
     * 返回batch insert的时候， 字段赋值表达式
     *
     * @param field
     * @param prefix
     * @return
     */
    public static String insertBatchValue(TableFieldInfo field, String prefix) {
        String sqlScript = field.getInsertSqlProperty(prefix);
        if (field.getFieldStrategy() == FieldStrategy.IGNORED || !isFillInsertOrLogicDelete(field)) {
            return sqlScript;
        }
        String property = prefix + field.getProperty();
        String defaultValue = findDefaultInsertValue(field, prefix);
        String condition = String.format("%s == null", property);
        return SqlScriptUtils.convertChoose(condition, defaultValue, sqlScript);
    }
}
