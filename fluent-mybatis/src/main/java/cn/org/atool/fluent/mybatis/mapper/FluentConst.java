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

    String Suffix_Defaults = "Defaults";

    String Suffix_QueryWhere = "QueryWhere";

    String Suffix_UpdateWhere = "UpdateWhere";

    String Suffix_Selector = "Selector";

    String Suffix_GroupBy = "GroupBy";

    String Suffix_Having = "Having";

    String Suffix_QueryOrderBy = "QueryOrderBy";

    String Suffix_UpdateOrderBy = "UpdateOrderBy";

    String Suffix_UpdateSetter = "UpdateSetter";

    String Suffix_EntityFormSetter = "FormSetter";

    String Suffix_ISegment = "ISegment";

    String Suffix_WrapperHelper = "WrapperHelper";

    String Suffix_SqlProvider = "SqlProvider";

    String Pack_Helper = "helper";

    String Pack_BaseDao = "dao.base";

    String Pack_Mapper = "mapper";

    String Pack_Wrapper = "wrapper";

    String Param_Map = "map";

    String Param_Coll = "coll";

    String Param_List = "list";

    String Param_CM = "cm";

    String Param_EW = "ew";

    String Param_ET = "et";

    String Param_Fields = "fields";

    String Param_Entity = "entity";

    String M_Insert = "insert";

    String M_Insert_With_Pk = "insertWithPk";

    String M_Insert_Entity = "insertEntity";

    String M_Insert_Batch_Entity = "insertBatchEntity";

    String M_InsertBatch = "insertBatch";

    String M_InsertBatch_With_Pk = "insertBatchWithPk";

    String M_InsertSelect = "insertSelect";

    String M_DeleteById = "deleteById";

    String M_LogicDeleteById = "logicDeleteById";

    String M_deleteByIds = "deleteByIds";

    String M_LogicDeleteByIds = "logicDeleteByIds";

    String M_DeleteByMap = "deleteByMap";

    String M_LogicDeleteByMap = "logicDeleteByMap";

    String M_Delete = "delete";

    String M_LogicDelete = "logicDelete";

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

    /**
     * RichEntity save 方法
     */
    String Rich_Entity_Save = "save";
    /**
     * RichEntity updateById 方法
     */
    String Rich_Entity_UpdateById = "updateById";
    /**
     * RichEntity findById 方法
     */
    String Rich_Entity_FindById = "findById";
    /**
     * RichEntity deleteById 方法
     */
    String Rich_Entity_DeleteById = "deleteById";
    /**
     * RichEntity logicDeleteById 方法
     */
    String Rich_Entity_LogicDeleteById = "logicDeleteById";
    /**
     * RichEntity listByNotNull 方法
     */
    String RichEntity_ListByNotNull = "listByNotNull";
}