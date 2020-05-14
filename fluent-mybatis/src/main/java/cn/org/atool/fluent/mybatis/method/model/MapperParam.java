package cn.org.atool.fluent.mybatis.method.model;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.apache.ibatis.executor.keygen.KeyGenerator;
import org.apache.ibatis.executor.keygen.NoKeyGenerator;
import org.apache.ibatis.mapping.ResultSetType;
import org.apache.ibatis.mapping.SqlCommandType;

/**
 * MapperBuilder：MapperBuilderAssistant#addMappedStatement 要素
 *
 * @author darui.wu
 * @create 2020/5/14 1:01 下午
 */
@Getter
@Setter
@Accessors(chain = true)
public class MapperParam {
    /**
     * mybatis sql method id
     */
    private final String statementId;
    /**
     * sql xml source
     */
    private String sql;
    /**
     * statement sql type: UNKNOWN, INSERT, UPDATE, DELETE, SELECT, FLUSH
     */
    @Setter(AccessLevel.NONE)
    private SqlCommandType sqlCommandType;

    /**
     * parameter map
     */
    private String parameterMap;
    /**
     * parameter type
     */
    private Class<?> parameterType;
    /**
     * result map
     */
    private String resultMap;
    /**
     * result type
     */
    private Class<?> resultType;
    /**
     * result set type
     */
    private ResultSetType resultSetType;
    /**
     * flush cache
     */
    private boolean flushCache;
    /**
     * use cache
     */
    private boolean useCache = false;
    /**
     * primary key generator
     */
    private KeyGenerator keyGenerator = new NoKeyGenerator();
    /**
     * entity property name of primary key
     */
    private String keyProperty;
    /**
     * table column name of primary key
     */
    private String keyColumn;


    public MapperParam(Class mapperClass, String methodId) {
        this.statementId = mapperClass.getName() + "." + methodId;
    }

    public MapperParam(String statementId) {
        this.statementId = statementId;
    }


    public MapperParam setSqlCommandType(SqlCommandType commandType) {
        this.sqlCommandType = commandType;
        if (sqlCommandType == SqlCommandType.SELECT) {
            this.flushCache = false;
            this.useCache = true;
        } else {
            this.flushCache = true;
            this.useCache = false;
        }
        return this;
    }

    public static MapperParam queryMapperParam(Class mapperClass, String methodId) {
        return new MapperParam(mapperClass, methodId)
            .setSqlCommandType(SqlCommandType.SELECT)
            ;
    }


    public static MapperParam insertMapperParam(Class mapperClass, String methodId) {
        return new MapperParam(mapperClass, methodId)
            .setSqlCommandType(SqlCommandType.INSERT)
            .setResultType(Integer.class)
            ;
    }

    public static MapperParam deleteMapperParam(Class mapperClass, String methodId) {
        return new MapperParam(mapperClass, methodId)
            .setSqlCommandType(SqlCommandType.DELETE)
            .setResultType(Integer.class)
            ;
    }

    public static MapperParam updateMapperParam(Class mapperClass, String methodId) {
        return new MapperParam(mapperClass, methodId)
            .setSqlCommandType(SqlCommandType.UPDATE)
            .setResultType(Integer.class)
            ;
    }
}