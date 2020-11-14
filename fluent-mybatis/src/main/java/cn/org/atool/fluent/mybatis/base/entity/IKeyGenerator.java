package cn.org.atool.fluent.mybatis.base.entity;


/**
 * 表主键生成器接口 (sql)
 *
 * @author darui.wu
 */
public interface IKeyGenerator {

    /**
     * 执行 key 生成 SQL
     *
     * @param incrementerName 序列名称
     * @return sql
     */
    String executeSql(String incrementerName);

    IKeyGenerator DB2_KEY_GENERATOR = (incrementerName) -> "values nextval for " + incrementerName;

    IKeyGenerator H2_KEY_GENERATOR = (incrementerName) -> "select " + incrementerName + ".nextval";

    IKeyGenerator ORACLE_KEY_GENERATOR = (incrementerName) -> "SELECT " + incrementerName + ".NEXTVAL FROM DUAL";

    IKeyGenerator POSTGRE_KEY_GENERATOR = (incrementerName) -> "select nextval('" + incrementerName + "')";
}