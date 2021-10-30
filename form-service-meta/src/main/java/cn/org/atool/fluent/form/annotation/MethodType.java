package cn.org.atool.fluent.form.annotation;

/**
 * API Method Type
 *
 * @author darui.wu
 */
public enum MethodType {
    /**
     * 根据入参和返回值自动判断
     * findOne, listEntity, stdPaged, tagPaged
     */
    Query,
    /**
     * 更新数据
     */
    Update,
    /**
     * 保存数据
     */
    Save
}