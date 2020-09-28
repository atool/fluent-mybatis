package cn.org.atool.fluent.mybatis.entity.base;

import com.squareup.javapoet.ClassName;

public interface ClassNameConst {
    ClassName Qualifier = ClassName.get("org.springframework.beans.factory.annotation", "Qualifier");

    ClassName Mapper = ClassName.get("org.apache.ibatis.annotations", "Mapper");

    ClassName Autowired = ClassName.get("org.springframework.beans.factory.annotation", "Autowired");

    String Suffix_Mapper = "Mapper";

    String Suffix_BaseDao = "BaseDao";

    String Suffix_EntityHelper = "EntityHelper";

    String Suffix_Mapping = "Mapping";

    String Suffix_Query = "Query";

    String Suffix_Update = "Update";

    String Suffix_QueryWhere = "QueryWhere";

    String Suffix_UpdateWhere = "UpdateWhere";

    String Suffix_Selector = "Selector";

    String Suffix_GroupBy = "GroupBy";

    String Suffix_Having = "Having";

    String Suffix_QueryOrderBy = "QueryOrderBy";

    String Suffix_UpdateOrderBy = "UpdateOrderBy";

    String Suffix_UpdateSetter = "UpdateSetter";

    String Suffix_ISegment = "ISegment";

    String Suffix_WrapperHelper = "WrapperHelper";

    String Suffix_SqlProvider = "SqlProvider";

    String Pack_Helper = "helper";

    String Pack_BaseDao = "dao.base";

    String Pack_Mapper = "mapper";

    String Pack_Wrapper = "wrapper";
}