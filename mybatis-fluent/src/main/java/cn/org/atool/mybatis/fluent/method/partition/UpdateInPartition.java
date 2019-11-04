package cn.org.atool.mybatis.fluent.method.partition;

import cn.org.atool.mybatis.fluent.method.UpdateByQuery;
import cn.org.atool.mybatis.fluent.util.MybatisUtil;
import com.baomidou.mybatisplus.core.metadata.TableInfo;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.SqlSource;

public class UpdateInPartition extends UpdateByQuery {
    private final static String MAPPER_METHOD_ID = "updateInPartition";

    private final static String SQL_FORMAT = "<script>\n%s UPDATE %s %s %s\n</script>";


    @Override
    public MappedStatement injectMappedStatement(Class<?> mapperClass, Class<?> modelClass, TableInfo tableInfo) {
        String setSql = this.updateSetSql(tableInfo);
        String whereSql = sqlWhereEntityWrapper(true, tableInfo);
        String sql = String.format(SQL_FORMAT,
                MybatisUtil.getPartitionDatabase(),
                MybatisUtil.getPartitionTable(tableInfo.getTableName()),
                setSql,
                whereSql
        );
        SqlSource sqlSource = languageDriver.createSqlSource(configuration, sql, mapperClass);
        return this.addUpdateMappedStatement(mapperClass, modelClass, MAPPER_METHOD_ID, sqlSource);
    }
}
