package cn.org.atool.fluent.processor.mybatis.base;

/**
 * MethodName
 *
 * @author wudarui
 */
public interface MethodName {

    /**
     * emptyQuery
     */
    String M_EMPTY_QUERY = "emptyQuery";

    /**
     * query
     */
    String M_DEFAULT_QUERY = "query";

    /**
     * emptyUpdater
     */
    String M_EMPTY_UPDATER = "emptyUpdater";

    /**
     * updater
     */
    String M_DEFAULT_UPDATER = "updater";

    /**
     * JavaDoc_Alias_Query_1
     */
    String JavaDoc_Alias_Query_1 = "显式指定表别名(join查询的时候需要定义表别名)\n";

    /**
     * notFluentMybatisException
     */
    String M_NOT_FLUENT_MYBATIS_EXCEPTION = "notFluentMybatisException";
}