package cn.org.atool.fluent.mybatis.annotation;

import lombok.Getter;

/**
 * 自定义接口泛型参数类型
 *
 * @author darui.wu
 */
public enum ParaType {
    /**
     * 对应的实体类
     */
    Entity("${entity}"),
    /**
     * 对应的查询器
     */
    Query("${query}"),
    /**
     * 对应的更新器
     */
    Updater("${update}");

    @Getter
    private String var;

    ParaType(String var) {
        this.var = var;
    }
}