package cn.org.atool.fluent.mybatis.processor.base;

public interface MethodName {
    String M_SET_ENTITY_BY_DEFAULT = "setEntityByDefault";

    String M_DEFAULT_QUERY = "defaultQuery";

    String M_NEW_QUERY = "query";

    String M_NEW_UPDATER = "updater";

    String M_DEFAULT_UPDATER = "defaultUpdater";

    String M_ALIAS_QUERY = "aliasQuery";

    String JavaDoc_Alias_Query_0 = "自动分配表别名查询构造器(join查询的时候需要定义表别名)\n" +
        "如果要自定义别名, 使用方法 {@link #aliasQuery(String)}";

    String JavaDoc_Alias_Query_1 = "显式指定表别名(join查询的时候需要定义表别名)\n";

    String M_ALIAS_WITH = "aliasWith";

    String JavaDoc_Alias_With_1 = "关联查询, 根据fromQuery自动设置别名和关联?参数\n" +
        "如果要自定义别名, 使用方法 {@link #aliasWith(String, BaseQuery)}";

    String JavaDoc_Alias_With_2 = "关联查询, 显式设置别名, 根据fromQuery自动关联?参数";

    String M_NOT_FLUENT_MYBATIS_EXCEPTION = "notFluentMybatisException";
}