package cn.org.atool.fluent.mybatis.method.partition;

import cn.org.atool.fluent.mybatis.method.UpdateByQuery;
import cn.org.atool.fluent.mybatis.method.model.MapperParam;
import cn.org.atool.fluent.mybatis.method.model.SqlBuilder;
import cn.org.atool.fluent.mybatis.util.MybatisUtil;
import com.baomidou.mybatisplus.core.metadata.TableInfo;
import org.apache.ibatis.mapping.MappedStatement;

/**
 * UpdateInPartition
 *
 * @author darui.wu
 */
public class UpdateInPartition extends UpdateByQuery {
    private final static String MAPPER_METHOD_ID = "updateInPartition";

    @Override
    public MappedStatement injectMappedStatement(Class<?> mapperClass, Class<?> modelClass, TableInfo tableInfo) {
        MapperParam mapper = MapperParam.updateMapperParam(mapperClass, MAPPER_METHOD_ID)
            .setParameterType(modelClass)
            .setSql(this.getMethodSql(tableInfo));

        return super.addMappedStatement(mapper);
    }

    @Override
    protected String getMethodSql(TableInfo table) {
        SqlBuilder builder = SqlBuilder.instance();
        return builder.beginScript()
            .append(MybatisUtil.getPartitionDatabase())
            .update(MybatisUtil.getPartitionTable(table.getTableName()))
            .set(() -> update(table, builder))
            .where(() -> whereEntity(table, builder))
            .ifThen("ew != null and ew.sqlComment != null", "${ew.sqlComment}")
            .endScript();
    }
}