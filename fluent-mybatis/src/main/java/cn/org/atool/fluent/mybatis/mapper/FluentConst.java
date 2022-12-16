package cn.org.atool.fluent.mybatis.mapper;

/**
 * fluent mybatis用到的常量定义
 *
 * @author wudarui
 */
public interface FluentConst {

    String Suffix_Mapper = "Mapper";

    String Suffix_BaseDao = "BaseDao";

    String Suffix_EntityMapping = "Mapping";

    String Suffix_Ref = "Ref";

    String Suffix_mapping = "mapping";

    String Suffix_MAPPING = "MAPPING";

    String Suffix_Query = "Query";

    String Suffix_Update = "Update";

    String Suffix_EntityWhere = "EntityWhere";

    String Suffix_QueryWhere = "QueryWhere";

    String Suffix_UpdateWhere = "UpdateWhere";

    String Suffix_Selector = "Selector";

    String Suffix_GroupBy = "GroupBy";

    String Suffix_Having = "Having";

    String Suffix_QueryOrderBy = "QueryOrderBy";

    String Suffix_UpdateOrderBy = "UpdateOrderBy";

    String Suffix_UpdateSetter = "UpdateSetter";

    String Suffix_EntityFormSetter = "FormSetter";

    String Suffix_ISegment = "ASegment";

    String Suffix_Segment = "Segment";

    String Pack_Helper = "helper";

    String Pack_BaseDao = "dao.base";

    String Pack_Mapper = "mapper";

    String Pack_Wrapper = "wrapper";

    String Param_List = "list";

    String Param_EW = "ew";

    String Param_Procedure = "procedure";

    String Param_P = "p";

    String Param_Fields = "fields";

    String Param_Entity = "entity";

    String M_Insert = "insert";

    String M_InsertWithPk = "insertWithPk";

    String M_InsertBatch = "insertBatch";

    String M_InsertBatchWithPk = "insertBatchWithPk";

    String M_ListEntity = "listEntity";

    String M_internalListEntity = "internalListEntity";

    String M_InsertSelect = "insertSelect";

    String M_UpdateBy = "updateBy";

    String M_ListObjs = "listObjs";

    String M_ListMaps = "listMaps";

    String M_Count = "count";

    String M_CountNoLimit = "countNoLimit";

    String M_Delete = "delete";

    String M_BatchCrud = "batchCrud";

    /**
     * RichEntity save 方法
     */
    String RE_Save = "save";
    /**
     * RichEntity updateById 方法
     */
    String RE_UpdateById = "updateById";
    /**
     * RichEntity findById 方法
     */
    String RE_FindById = "findById";
    /**
     * RichEntity deleteById 方法
     */
    String RE_DeleteById = "deleteById";
    /**
     * RichEntity logicDeleteById 方法
     */
    String RE_LogicDeleteById = "logicDeleteById";
    /**
     * RichEntity listByNotNull 方法
     */
    String RE_ListByNotNull = "listByNotNull";

    /**
     * RichEntity firstByNotNull 方法
     */
    String RE_FirstByNotNull = "firstByNotNull";

    String F_Entity_Class = "entityClass";
}