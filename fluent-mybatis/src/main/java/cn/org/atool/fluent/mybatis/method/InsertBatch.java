package cn.org.atool.fluent.mybatis.method;

import cn.org.atool.fluent.mybatis.method.model.MapperParam;
import cn.org.atool.fluent.mybatis.method.model.SqlBuilder;
import cn.org.atool.fluent.mybatis.util.MybatisInsertUtil;
import com.baomidou.mybatisplus.core.metadata.TableFieldInfo;
import com.baomidou.mybatisplus.core.metadata.TableInfo;
import org.apache.ibatis.mapping.MappedStatement;

import java.util.ArrayList;
import java.util.List;

import static cn.org.atool.fluent.mybatis.method.model.SqlBuilder.safeParam;
import static java.util.stream.Collectors.joining;

/**
 * InsertBatch: 批量插入实现
 *
 * @author darui.wu
 */
public class InsertBatch extends BaseMethod {
    @Override
    public MappedStatement injectMappedStatement(Class<?> mapperClass, Class<?> modelClass, TableInfo table) {
        MapperParam mapper = MapperParam.insertMapperParam(mapperClass, "insertBatch")
            .setSql(getMethodSql(table));

        return super.addMappedStatement(mapper);
    }

    @Override
    protected String getMethodSql(TableInfo table) {
        return SqlBuilder.instance()
            .beginScript()
            .insert(table.getTableName())
            .append("(").append(super.getColumnsWithPrimary(table)).append(")")
            .append("VALUES ")
            .append(this.getInsertValues(table))
            .endScript()
            .toString();
    }

    final static String prefix = "item.";

    private String getInsertValues(TableInfo table) {
        List<String> values = new ArrayList<>();
        if (table.getKeyColumn() != null) {
            values.add(safeParam(prefix + table.getKeyColumn()) + ",");
        }
        table.getFieldList().forEach(field -> values.add(fieldValue("item.", field)));
        String valuesScript = values.stream().collect(joining(NEWLINE));

        return SqlBuilder.instance()
            .beginForeach("list", "item", ",")
            .beginTrim("(", ")", ",")
            .append(valuesScript)
            .endTrim()
            .endForeach()
            .toString();
    }

    /**
     * 当个字段值, if or choose 标签
     *
     * @param field
     * @return
     */
    private String fieldValue(String prefix, TableFieldInfo field) {
        String defaultValue = MybatisInsertUtil.findInsertDefaultValue(field);
        if (defaultValue == null) {
            return safeParam(prefix + field.getEl()) + ",";
        } else {
            return SqlBuilder.instance()
                .choose(prefix, field.getProperty(), defaultValue)
                .toString();
        }
    }
}