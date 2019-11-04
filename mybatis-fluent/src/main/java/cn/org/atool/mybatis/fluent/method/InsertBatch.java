package cn.org.atool.mybatis.fluent.method;

import cn.org.atool.mybatis.fluent.util.MybatisInsertUtil;
import com.baomidou.mybatisplus.core.injector.AbstractMethod;
import com.baomidou.mybatisplus.core.metadata.TableInfo;
import org.apache.ibatis.executor.keygen.NoKeyGenerator;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.SqlCommandType;
import org.apache.ibatis.mapping.SqlSource;

import static java.util.stream.Collectors.joining;

public class InsertBatch extends AbstractMethod {
    @Override
    public MappedStatement injectMappedStatement(Class<?> mapperClass, Class<?> modelClass, TableInfo tableInfo) {
        String columns = tableInfo.getAllSqlSelect();
        String values = this.buildValueScript(tableInfo);

        String sql = String.format(this.getMethodSql(), tableInfo.getTableName(), columns, values);
        SqlSource sqlSource = languageDriver.createSqlSource(configuration, sql, modelClass);
        return this.addInsertMappedStatement(mapperClass, modelClass, this.getMethodName(), sqlSource);
    }

    private MappedStatement addInsertMappedStatement(Class<?> mapperClass, Class<?> modelClass, String id, SqlSource sqlSource) {
        return super.addMappedStatement(mapperClass, id, sqlSource, SqlCommandType.INSERT, modelClass, null, Integer.class, new NoKeyGenerator(), null, null);
    }


    private String buildValueScript(TableInfo tableInfo) {
        String valuesScript = tableInfo.getFieldList().stream()
                .map(field -> MybatisInsertUtil.insertBatchValue(field, "item."))
                .collect(joining(NEWLINE));
        return new StringBuilder(NEWLINE)
                .append("<foreach collection=\"list\" item=\"item\" index=\"index\" separator=\",\">")
                .append(NEWLINE)
                .append("<trim prefix=\"(\" suffix=\")\" suffixOverrides=\",\">")
                .append("#{item.").append(tableInfo.getKeyColumn()).append("}, ").append(valuesScript)
                .append("</trim>")
                .append(NEWLINE)
                .append("</foreach>")
                .toString();
    }

    private String getMethodSql() {
        return "<script>INSERT INTO %s (%s) VALUES %s</script>";
    }

    private String getMethodName() {
        return "insertBatch";
    }
}
