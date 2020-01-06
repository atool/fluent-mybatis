package cn.org.atool.fluent.mybatis.method;

import cn.org.atool.fluent.mybatis.util.MybatisInsertUtil;
import com.baomidou.mybatisplus.annotation.FieldStrategy;
import com.baomidou.mybatisplus.core.enums.SqlMethod;
import com.baomidou.mybatisplus.core.injector.AbstractMethod;
import com.baomidou.mybatisplus.core.metadata.TableFieldInfo;
import com.baomidou.mybatisplus.core.metadata.TableInfo;
import com.baomidou.mybatisplus.core.toolkit.sql.SqlScriptUtils;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.SqlSource;
import java.util.Objects;
import static java.util.stream.Collectors.joining;

/**
 * 根据 ID 更新有值字段
 *
 * @author darui.wu
 * @create 2020/1/2 5:30 下午
 */
public class UpdateById extends AbstractMethod {
    @Override
    public MappedStatement injectMappedStatement(Class<?> mapperClass, Class<?> modelClass, TableInfo tableInfo) {
        SqlMethod sqlMethod = SqlMethod.UPDATE_BY_ID;
        String sqlSet = this.setTableSql(tableInfo);
        String sql = String.format(sqlMethod.getSql(),
                tableInfo.getTableName(),
                sqlSet,
                tableInfo.getKeyColumn(),
                ENTITY_DOT + tableInfo.getKeyProperty(),
                ""
        );

        SqlSource sqlSource = languageDriver.createSqlSource(configuration, sql, modelClass);
        return addUpdateMappedStatement(mapperClass, modelClass, sqlMethod.getMethod(), sqlSource);
    }

    private String setTableSql(TableInfo table) {
        String sqlScript = table.getFieldList().stream()
                .map(this::setFieldSql)
                .filter(Objects::nonNull)
                .collect(joining(NEWLINE));
        return SqlScriptUtils.convertSet(sqlScript);
    }


    /**
     * 获取 set sql 片段
     *
     * @param field 字段
     * @return sql 脚本片段
     */
    public String setFieldSql(final TableFieldInfo field) {
        if (!MybatisInsertUtil.isUpdateDefaultField(field)) {
            if (field.getUpdateStrategy() == FieldStrategy.NEVER) {
                return null;
            }

            String sqlScript = field.getColumn() + EQUALS + SqlScriptUtils.safeParam(ENTITY_DOT + field.getEl()) + COMMA;
            if (field.getUpdateStrategy() == FieldStrategy.IGNORED) {
                return sqlScript;
            }
            return SqlScriptUtils.convertIf(sqlScript, getIfProperty(field, ENTITY_DOT + field.getProperty()), false);
        } else {
            return field.getColumn() + "=" + field.getUpdate() + COMMA;
        }
    }

    private String getIfProperty(TableFieldInfo field, String property) {
        if (field.getUpdateStrategy() == FieldStrategy.NOT_EMPTY && field.isCharSequence()) {
            return String.format("%s != null and %s != ''", property, property);
        } else {
            return String.format("%s != null", property);
        }
    }
}