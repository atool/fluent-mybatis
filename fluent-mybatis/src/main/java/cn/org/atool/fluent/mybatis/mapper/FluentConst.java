package cn.org.atool.fluent.mybatis.mapper;

/**
 * fluent mybatis用到的常量定义
 *
 * @author wudarui
 */
public interface FluentConst {

    String Suffix_Mapper = "Mapper";

    String Suffix_BaseDao = "BaseDao";

    String Suffix_EntityHelper = "Helper";

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

    String Double_Quota_Str = String.valueOf('"');

    String Param_Map = "map";

    String Param_Coll = "coll";

    String Param_List = "list";

    String Param_CM = "cm";

    String Param_EW = "ew";

    String Param_ET = "et";

    String Param_Entity = "entity";

    /**
     * 分库
     */
    String SPEC_COMMENT = "SPEC_COMMENT";
    /**
     * 分表
     */
    String SPEC_TABLE = "SPEC_TABLE";

    String M_Insert = "insert";

    String M_InsertBatch = "insertBatch";

    String M_DeleteById = "deleteById";

    String M_DeleteByMap = "deleteByMap";

    String M_Delete = "delete";

    String M_deleteByIds = "deleteByIds";

    String M_updateById = "updateById";

    String M_updateBy = "updateBy";

    String M_findById = "findById";

    String M_findOne = "findOne";

    String M_listByIds = "listByIds";

    String M_listByMap = "listByMap";

    String M_listEntity = "listEntity";

    String M_listMaps = "listMaps";

    String M_listObjs = "listObjs";

    String M_count = "count";

    String M_countNoLimit = "countNoLimit";
}
