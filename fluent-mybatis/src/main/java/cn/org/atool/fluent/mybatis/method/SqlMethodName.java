package cn.org.atool.fluent.mybatis.method;

public interface SqlMethodName {
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