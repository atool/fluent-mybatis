package cn.org.atool.fluent.mybatis.method;

import cn.org.atool.fluent.mybatis.method.model.MapperParam;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.core.incrementer.IKeyGenerator;
import com.baomidou.mybatisplus.core.injector.AbstractMethod;
import com.baomidou.mybatisplus.core.metadata.TableInfo;
import com.baomidou.mybatisplus.core.toolkit.GlobalConfigUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.executor.keygen.Jdbc3KeyGenerator;
import org.apache.ibatis.executor.keygen.KeyGenerator;
import org.apache.ibatis.executor.keygen.NoKeyGenerator;
import org.apache.ibatis.executor.keygen.SelectKeyGenerator;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.SqlCommandType;
import org.apache.ibatis.mapping.SqlSource;
import org.apache.ibatis.mapping.StatementType;

import java.util.ArrayList;
import java.util.List;

import static java.util.stream.Collectors.joining;

/**
 * BaseMethod, 后续替换掉 AbstractMethod
 *
 * @author darui.wu
 * @create 2020/5/14 1:35 下午
 */
@Slf4j
public abstract class BaseMethod extends AbstractMethod {
    /**
     * 添加 MappedStatement 到 Mybatis 容器
     *
     * @param param
     * @return
     */
    protected MappedStatement addMappedStatement(MapperParam param) {
        if (hasMappedStatement(param.getStatementId())) {
            log.warn(LEFT_SQ_BRACKET + param.getStatementId() + "] Has been loaded, so ignoring this injection for [" + getClass() + RIGHT_SQ_BRACKET);
            return null;
        } else {
            SqlSource sqlSource = languageDriver.createSqlSource(configuration, param.getSql(), param.getParameterType());
            return builderAssistant.addMappedStatement(
                param.getStatementId(),
                sqlSource,
                StatementType.PREPARED,
                param.getSqlCommandType(),
                null,
                null,
                null,
                param.getParameterType(),
                param.getResultMap(),
                param.getResultType(),
                null,
                param.isFlushCache(),
                param.isUseCache(),
                false,
                param.getKeyGenerator(),
                param.getKeyProperty(),
                param.getKeyColumn(),
                configuration.getDatabaseId(),
                languageDriver,
                null);
        }
    }

    /**
     * 是否已经存在MappedStatement
     *
     * @param mappedStatement MappedStatement
     * @return true or false
     */
    private boolean hasMappedStatement(String mappedStatement) {
        return configuration.hasStatement(mappedStatement, false);
    }


    /**
     * 设置主键和主键生产器
     *
     * @param mapper
     * @param table
     */
    protected void setPrimaryKey(MapperParam mapper, TableInfo table) {
        String keyProperty = table.getKeyProperty();
        if (keyProperty == null || "".equals(keyProperty.trim())) {
            return;
        }
        mapper.setKeyProperty(keyProperty).setKeyColumn(table.getKeyColumn());
        if (table.getIdType() == IdType.AUTO) {
            mapper.setKeyGenerator(new Jdbc3KeyGenerator());
        } else if (table.getKeySequence() != null) {
            mapper.setKeyGenerator(genKeyGenerator(table, mapper));
        }
    }

    /**
     * 自定义 KEY 生成器
     *
     * @param table
     * @param mapper
     * @return
     */
    public KeyGenerator genKeyGenerator(TableInfo table, MapperParam mapper) {
        IKeyGenerator keyGenerator = GlobalConfigUtils.getKeyGenerator(this.configuration);
        if (null == keyGenerator) {
            throw new IllegalArgumentException("not configure IKeyGenerator implementation class.");
        }
        String sql = keyGenerator.executeSql(table.getKeySequence().value());
        MapperParam keyMapper = new MapperParam(mapper.getStatementId() + "!selectKey")
            .setSqlCommandType(SqlCommandType.SELECT)
            .setResultType(table.getKeySequence().clazz())
            .setFlushCache(false)
            .setUseCache(false)
            .setKeyGenerator(new NoKeyGenerator())
            .setKeyProperty(table.getKeyProperty())
            .setKeyColumn(table.getKeyColumn())
            .setSql(sql)
            .setParameterType(null);

        this.addMappedStatement(keyMapper);

        String statementId = builderAssistant.applyCurrentNamespace(keyMapper.getStatementId(), false);
        MappedStatement keyStatement = this.configuration.getMappedStatement(statementId, false);
        SelectKeyGenerator selectKeyGenerator = new SelectKeyGenerator(keyStatement, true);
        this.configuration.addKeyGenerator(statementId, selectKeyGenerator);
        return selectKeyGenerator;
    }

    /**
     * 返回不包含主键的其他字段拼接
     *
     * @param table
     * @return
     */
    protected String getColumnsWithoutPrimary(TableInfo table) {
        return table.getFieldList().stream()
            .map(field -> field.getColumn())
            .collect(joining(", "));
    }

    /**
     * 返回包含主键的字段拼接
     *
     * @param table
     * @return
     */
    protected String getColumnsWithPrimary(TableInfo table) {
        List<String> list = new ArrayList<>();
        if (table.getKeyColumn() != null) {
            list.add(table.getKeyColumn());
        }
        table.getFieldList().forEach(field -> list.add(field.getColumn()));
        return list.stream().collect(joining(", "));
    }

    /**
     * 替换单引号为双引号
     *
     * @param withSingleQuotation
     * @return
     */
    protected String replace(String withSingleQuotation) {
        return withSingleQuotation.replace('\'', '"');
    }

    /**
     * 返回方法具体的sql语句
     *
     * @param table 表结构信息
     * @return
     */
    protected abstract String getMethodSql(TableInfo table);
}