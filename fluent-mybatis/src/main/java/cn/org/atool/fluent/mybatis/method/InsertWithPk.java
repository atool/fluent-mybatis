package cn.org.atool.fluent.mybatis.method;

import cn.org.atool.fluent.mybatis.util.MybatisInsertUtil;
import com.baomidou.mybatisplus.core.enums.SqlMethod;
import com.baomidou.mybatisplus.core.injector.AbstractMethod;
import com.baomidou.mybatisplus.core.metadata.TableInfo;
import com.baomidou.mybatisplus.core.toolkit.StringPool;
import com.baomidou.mybatisplus.core.toolkit.sql.SqlScriptUtils;
import org.apache.ibatis.executor.keygen.KeyGenerator;
import org.apache.ibatis.executor.keygen.NoKeyGenerator;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.SqlSource;


public class InsertWithPk extends AbstractMethod {
    @Override
    public MappedStatement injectMappedStatement(Class<?> mapperClass, Class<?> modelClass, TableInfo tableInfo) {
        KeyGenerator keyGenerator = new NoKeyGenerator();
        SqlMethod sqlMethod = SqlMethod.INSERT_ONE;
        String columns = MybatisInsertUtil.getAllInsertSqlColumn(tableInfo, true);
        String columnScript = SqlScriptUtils.convertTrim(columns, LEFT_BRACKET, RIGHT_BRACKET, null, COMMA);
        String values = MybatisInsertUtil.getAllInsertValueSql(tableInfo, true);
        String valuesScript = SqlScriptUtils.convertTrim(values, StringPool.LEFT_BRACKET, RIGHT_BRACKET, null, COMMA);
        String keyProperty = tableInfo.getKeyProperty();
        String keyColumn = tableInfo.getKeyColumn();

        String sql = String.format(sqlMethod.getSql(), tableInfo.getTableName(), columnScript, valuesScript);
        SqlSource sqlSource = languageDriver.createSqlSource(configuration, sql, modelClass);
        return this.addInsertMappedStatement(mapperClass, modelClass, getMethodName(), sqlSource, keyGenerator, keyProperty, keyColumn);
    }

    private String getMethodName() {
        return "insertWithPk";
    }
}