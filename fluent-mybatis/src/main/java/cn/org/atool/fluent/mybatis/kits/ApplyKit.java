package cn.org.atool.fluent.mybatis.kits;

import cn.org.atool.fluent.mybatis.base.model.FieldMapping;

@SuppressWarnings("rawtypes")
public interface ApplyKit {
    /**
     * field = field + ?
     */
    static ApplyFunc plus(FieldMapping field, Object value) {
        return new ApplyFunc(field.column + " + ?").args(value);
    }
}
