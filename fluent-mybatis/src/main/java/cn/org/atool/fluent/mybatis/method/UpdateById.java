package cn.org.atool.fluent.mybatis.method;

import cn.org.atool.fluent.mybatis.method.model.MapperParam;
import cn.org.atool.fluent.mybatis.method.model.SqlBuilder;
import cn.org.atool.fluent.mybatis.util.MybatisInsertUtil;
import com.baomidou.mybatisplus.core.metadata.TableFieldInfo;
import com.baomidou.mybatisplus.core.metadata.TableInfo;
import org.apache.ibatis.mapping.MappedStatement;

/**
 * 根据 ID 更新有值字段
 *
 * @author darui.wu
 * @create 2020/1/2 5:30 下午
 */
public class UpdateById extends BaseMethod {
    @Override
    public MappedStatement injectMappedStatement(Class<?> mapperClass, Class<?> modelClass, TableInfo tableInfo) {
        MapperParam mapper = MapperParam.updateMapperParam(mapperClass, "updateById")
            .setSql(this.getMethodSql(tableInfo));
        return super.addMappedStatement(mapper);
    }


    @Override
    protected String getMethodSql(TableInfo table) {
        return SqlBuilder.instance()
            .beginScript()
            .update(table.getTableName())
            .beginSet()
            .joinEach(table.getFieldList(), this::setFieldValue, SqlBuilder.NEWLINE)
            .endSet()
            .beginWhere()
            .andVariable(table.getKeyColumn(), ENTITY_DOT, table.getKeyProperty())
            .endWhere()
            .endScript()
            .toString();
    }


    /**
     * 获取 set sql 片段
     *
     * @param field 字段
     * @return sql 脚本片段
     */
    public String setFieldValue(final TableFieldInfo field) {
        if (MybatisInsertUtil.isUpdateDefaultField(field)) {
            return SqlBuilder.instance()
                .setValue(field.getColumn(), field.getUpdate())
                .toString();
        } else {
            return SqlBuilder.instance()
                .ifSet(ENTITY_DOT, field.getProperty(), field.getColumn())
                .toString();
        }
    }
}