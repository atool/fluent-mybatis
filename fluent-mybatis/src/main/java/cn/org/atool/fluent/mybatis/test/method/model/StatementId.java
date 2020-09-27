package cn.org.atool.fluent.mybatis.test.method.model;

/**
 * MethodId: mapper方法Id
 *
 * @author:darui.wu Created by darui.wu on 2020/5/18.
 */
public interface StatementId {
    String Method_Insert = "insert";

    String Method_InsertBatch = "insertBatch";

    String Method_Delete = "delete";

    String Method_DeleteById = "deleteById";

    String Method_DeleteByIds = "deleteByIds";

    String Method_DeleteByMap = "deleteByMap";

    String Method_UpdateById = "updateById";

    String Method_UpdateByQuery = "updateBy";

    String Method_SelectById = "findById";

    String Method_SelectOne = "findOne";

    String Method_SelectByIds = "listByIds";

    String Method_SelectByMap = "listByMap";

    String Method_SelectList = "listEntity";

    String Method_SelectMaps = "listMaps";

    String Method_SelectObjs = "listObjs";

    String Method_SelectCount = "count";

    String Method_Count_NoLimit = "countNoLimit";
    /**
     * 更新指定表（分表）
     */
    String Method_UpdateSpecByQuery = "updateSpecByQuery";
    /**
     * 删除知道不表(分表)
     */
    String Method_DeleteSpec = "deleteSpec";
    /**
     * 从指定表（分表）查询
     */
    String Method_SelectListFromSpec = "selectSpecList";
}