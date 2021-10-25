package cn.org.atool.fluent.form.annotation;

/**
 * API Method Type
 *
 * @author darui.wu
 */
public enum MethodType {
    /**
     * 插入数据
     */
    Insert,
    /**
     * 返回列表(包括2种分页处理)
     */
    ListEntity,
    /**
     * 返回单条数据
     */
    FindOne,
    /**
     * 更新数据
     */
    Update
}