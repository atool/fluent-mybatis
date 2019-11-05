package cn.org.atool.fluent.mybatis.base;

import java.util.Map;

public interface IProperty2Column {
    /**
     * 返回entity实体属性和数据库字段的映射关系
     *
     * @return
     */
    Map<String, String> getProperty2Column();

    /**
     * 返回带更新的字段及值
     *
     * @return
     */
    default Map<String, ? extends Object> getUpdates() {
        return null;
    }
}
