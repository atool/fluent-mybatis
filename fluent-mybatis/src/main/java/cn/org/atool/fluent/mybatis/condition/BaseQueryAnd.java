package cn.org.atool.fluent.mybatis.condition;

import java.util.Map;

public class BaseQueryAnd<W extends AbstractWrapper & IProperty2Column> {
    private final W wrapper;

    protected BaseQueryAnd(W wrapper) {
        this.wrapper = wrapper;
    }

    /**
     * 根据map对象设置查询条件
     *
     * @param where
     * @return
     */
    public W byMap(Map<String, Object> where) {
        if (where != null) {
            where.forEach((k, v) -> {
                String column = wrapper.getProperty2Column().get(k);
                if (column == null) {
                    column = k;
                }
                if (v == null) {
                    wrapper.isNull(column);
                } else {
                    wrapper.eq(column, v);
                }
            });
        }
        return wrapper;
    }
}