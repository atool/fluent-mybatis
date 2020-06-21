package cn.org.atool.fluent.mybatis.method.model;

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

    String Method_SelectById = "selectById";

    String Method_SelectByIds = "selectByIds";

    String Method_SelectByMap = "selectByMap";

    String Method_SelectCount = "selectCount";

    String Method_Count_NoLimit = "countNoLimit";

    String Method_SelectList = "selectList";

    String Method_SelectMaps = "selectMaps";

    String Method_SelectOne = "selectOne";

    String Method_SelectObjs = "selectObjs";

    String Method_UpdateById = "updateById";

    String Method_UpdateByQuery = "updateBy";
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