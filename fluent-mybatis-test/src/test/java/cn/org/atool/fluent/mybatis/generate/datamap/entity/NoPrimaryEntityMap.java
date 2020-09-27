package cn.org.atool.fluent.mybatis.generate.datamap.entity;

import org.test4j.module.ICore.DataMap;
import org.test4j.tools.datagen.KeyValue;

/**
 * @ClassName NoPrimaryEntityMap
 * @Description NoPrimaryEntityMap
 *
 * @author generate code
 */
public class NoPrimaryEntityMap extends DataMap<NoPrimaryEntityMap> {
    /**
     * 设置NoPrimaryEntity对象column1字段值
     */
    public transient final KeyValue<NoPrimaryEntityMap> column1 = new KeyValue(this, "column1");
    /**
     * 设置NoPrimaryEntity对象column2字段值
     */
    public transient final KeyValue<NoPrimaryEntityMap> column2 = new KeyValue(this, "column2");

    public NoPrimaryEntityMap() {
        super();
    }

    public NoPrimaryEntityMap(int size) {
        super(size);
    }

    public static NoPrimaryEntityMap create() {
        return new NoPrimaryEntityMap();
    }

    public static NoPrimaryEntityMap create(int size) {
        return new NoPrimaryEntityMap(size);
    }

    public static class Factory {
        public NoPrimaryEntityMap create() {
            return NoPrimaryEntityMap.create();
        }

        public NoPrimaryEntityMap create(int size) {
            return NoPrimaryEntityMap.create(size);
        }
    }
}