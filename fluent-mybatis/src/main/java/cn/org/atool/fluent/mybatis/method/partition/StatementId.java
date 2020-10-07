package cn.org.atool.fluent.mybatis.method.partition;

/**
 * MethodId: mapper方法Id
 *
 * @author:darui.wu Created by darui.wu on 2020/5/18.
 */
public interface StatementId {

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