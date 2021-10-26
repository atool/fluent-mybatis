package cn.org.atool.fluent.form.annotation;

/**
 * API Method Type
 *
 * @author darui.wu
 */
public enum MethodType {
    /**
     * 根据入参和返回值自动判断
     * update, findOne, listEntity, stdPaged, tagPaged
     */
    Auto,
    /**
     * 保存数据
     */
    Save
}