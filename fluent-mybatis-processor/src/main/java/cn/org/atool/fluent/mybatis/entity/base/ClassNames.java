package cn.org.atool.fluent.mybatis.entity.base;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.ParameterizedTypeName;

import java.util.Map;

public class ClassNames {
    public static final ClassName CN_Qualifier = ClassName.get("org.springframework.beans.factory.annotation", "Qualifier");

    public static final ClassName CN_Component = ClassName.get("org.springframework.stereotype", "Component");

    public static final ClassName CN_Mapper = ClassName.get("org.apache.ibatis.annotations", "Mapper");

    public static final ClassName CN_Autowired = ClassName.get("org.springframework.beans.factory.annotation", "Autowired");

    public static final ParameterizedTypeName CN_Map_StrObj = ParameterizedTypeName.get(Map.class, String.class, Object.class);

    public static final String Suffix_Mapper = "Mapper";

    public static final String Suffix_BaseDao = "BaseDao";

    public static final String Suffix_EntityHelper = "EntityHelper";

    public static final String Suffix_Mapping = "Mapping";

    public static final String Suffix_Query = "Query";

    public static final String Suffix_Update = "Update";

    public static final String Suffix_QueryWhere = "QueryWhere";

    public static final String Suffix_UpdateWhere = "UpdateWhere";

    public static final String Suffix_Selector = "Selector";

    public static final String Suffix_GroupBy = "GroupBy";

    public static final String Suffix_Having = "Having";

    public static final String Suffix_QueryOrderBy = "QueryOrderBy";

    public static final String Suffix_UpdateOrderBy = "UpdateOrderBy";

    public static final String Suffix_UpdateSetter = "UpdateSetter";

    public static final String Suffix_ISegment = "ISegment";

    public static final String Suffix_WhereSegment = "WhereSegment";

    public static final String Suffix_WrapperHelper = "WrapperHelper";

    public static final String Suffix_SqlProvider = "SqlProvider";

    public static final String Pack_Helper = "helper";

    public static final String Pack_BaseDao = "dao";

    public static final String Pack_Mapper = "mapper";

    public static final String Pack_Wrapper = "wrapper";

    public static final ClassName getClassName(String fullClassName) {
        int index = fullClassName.lastIndexOf('.');
        String packName = index < 0 ? "" : fullClassName.substring(0, index);
        String klasName = index < 0 ? fullClassName : fullClassName.substring(index + 1);
        return ClassName.get(packName, klasName);
    }
}