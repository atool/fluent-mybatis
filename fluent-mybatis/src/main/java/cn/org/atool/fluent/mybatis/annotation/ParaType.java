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
    Entity("entity.name"),
    /**
     * 对应的查询器
     */
    Query("entityQuery.name"),
    /**
     * 对应的更新器
     */
    Updater("entityUpdate.name");

    @Getter
    private String var;

    ParaType(String var) {
        this.var = var;
    }
}