package cn.org.atool.fluent.mybatis.annotation;

/**
 * 自定义接口泛型参数类型
 *
 * @author darui.wu
 */
public enum ParaType {
    /**
     * 对应的实体类
     */
    Entity,
    /**
     * 对应的查询器
     */
    Query,
    /**
     * 对应的更新器
     */
    Updater;
}