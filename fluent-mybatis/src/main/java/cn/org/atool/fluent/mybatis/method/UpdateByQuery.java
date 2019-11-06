package cn.org.atool.fluent.mybatis.method;

import cn.org.atool.fluent.mybatis.util.MybatisInsertUtil;
import com.baomidou.mybatisplus.core.enums.SqlMethod;
import com.baomidou.mybatisplus.core.injector.AbstractMethod;
import com.baomidou.mybatisplus.core.metadata.TableFieldInfo;
import com.baomidou.mybatisplus.core.metadata.TableInfo;
import com.baomidou.mybatisplus.core.toolkit.sql.SqlScriptUtils;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.SqlSource;

import static java.util.stream.Collectors.joining;

public class UpdateByQuery extends AbstractMethod {
    private final static String WRAPPER_PROPERTIES = "ew.updates";

    private final static String MAPPER_METHOD_ID = "updateBy";

    private final static String EW_PROPERTIES_NOT_NULL = String.format("%s != null and %s != null", WRAPPER, WRAPPER_PROPERTIES);

    private final static String EW_SQLSET_NOT_NULL = String.format("%s != null and %s != null", WRAPPER, U_WRAPPER_SQL_SET);

    @Override
    public MappedStatement injectMappedStatement(Class<?> mapperClass, Class<?> modelClass, TableInfo tableInfo) {
        SqlMethod sqlMethod = SqlMethod.UPDATE;
        String setSql = this.updateSetSql(tableInfo);
        String whereSql = sqlWhereEntityWrapper(true, tableInfo);
        String sql = String.format(sqlMethod.getSql(), tableInfo.getTableName(), setSql, whereSql,sqlComment());
        SqlSource sqlSource = languageDriver.createSqlSource(configuration, sql, mapperClass);
        return this.addUpdateMappedStatement(mapperClass, modelClass, MAPPER_METHOD_ID, sqlSource);
    }

    protected String updateSetSql(TableInfo tableInfo) {
        String setSql = getUpdateFiledSql(tableInfo, WRAPPER_PROPERTIES);
        String sqlScript = SqlScriptUtils.convertIf(setSql, EW_PROPERTIES_NOT_NULL, true)
                + NEWLINE
                + SqlScriptUtils.convertIf(SqlScriptUtils.unSafeParam(U_WRAPPER_SQL_SET), EW_SQLSET_NOT_NULL, false);
        return SqlScriptUtils.convertSet(sqlScript);
    }

    private String getUpdateFiledSql(TableInfo tableInfo, String prefix) {
        return tableInfo.getFieldList().stream()
                .map(field -> this.getUpdateValueSql(field, prefix))
                .collect(joining(NEWLINE));
    }

    private String getUpdateValueSql(TableFieldInfo field, String prefix) {
        String setValue = field.getColumn() + "=" + field.getInsertSqlProperty(prefix + DOT);

        if (!MybatisInsertUtil.isUpdateDefaultField(field)) {
            String test = String.format("%s.containsKey('%s')", prefix, field.getProperty());
            return SqlScriptUtils.convertIf(setValue, test, false);
        } else {
            return field.getColumn() + "=" + field.getUpdate() + COMMA;
        }
    }
}
