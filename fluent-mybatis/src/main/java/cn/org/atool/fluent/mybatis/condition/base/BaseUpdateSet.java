package cn.org.atool.fluent.mybatis.condition.base;

import cn.org.atool.fluent.mybatis.condition.interfaces.Update;

import java.util.Map;

public class BaseUpdateSet<U extends Update> {
    private U wrapper;

    protected BaseUpdateSet(U wrapper) {
        this.wrapper = wrapper;
    }

    /**
     * 按照values中设置的字段和值更新记录
     *
     * @param values
     * @return
     */
    public U byMap(Map<String, Object> values) {
        if (values != null) {
            values.forEach((column, value) -> wrapper.set(column, value));
        }
        return wrapper;
    }
}