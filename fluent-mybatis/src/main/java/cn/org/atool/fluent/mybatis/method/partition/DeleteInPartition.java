package cn.org.atool.fluent.mybatis.method.partition;

import cn.org.atool.fluent.mybatis.util.MybatisUtil;
import com.baomidou.mybatisplus.core.injector.AbstractMethod;
import com.baomidou.mybatisplus.core.metadata.TableInfo;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.SqlSource;

public class DeleteInPartition extends AbstractMethod {
    private final static String MAPPER_METHOD_ID = "deleteInPartition";

    private final static String SQL_FORMAT = "<script>\n%s DELETE FROM %s %s\n</script>";

    @Override
    public MappedStatement injectMappedStatement(Class<?> mapperClass, Class<?> modelClass, TableInfo tableInfo) {
        String sql = String.format(SQL_FORMAT,
                MybatisUtil.getPartitionDatabase(),
                MybatisUtil.getPartitionTable(tableInfo.getTableName()),
                this.sqlWhereEntityWrapper(true, tableInfo)
        );
        SqlSource sqlSource = languageDriver.createSqlSource(configuration, sql, modelClass);
        return this.addDeleteMappedStatement(mapperClass, MAPPER_METHOD_ID, sqlSource);
    }
}