package cn.org.atool.fluent.mybatis.demo.generate.datamap.entity;

import cn.org.atool.fluent.mybatis.demo.generate.mapping.NoAutoIdMP.Property;
import org.test4j.module.ICore.DataMap;
import org.test4j.tools.datagen.KeyValue;

/**
 * @ClassName NoAutoIdEntityMap
 * @Description NoAutoIdEntityMap
 *
 * @author generate code
 */
public class NoAutoIdEntityMap extends DataMap<NoAutoIdEntityMap> {
    /**
     * 设置NoAutoIdEntity对象column1字段值
     */
    public transient final KeyValue<NoAutoIdEntityMap> column1 = new KeyValue(this, Property.column1);
    /**
     * 设置NoAutoIdEntity对象id字段值
     */
    public transient final KeyValue<NoAutoIdEntityMap> id = new KeyValue(this, Property.id);

    public NoAutoIdEntityMap() {
        super();
    }

    public NoAutoIdEntityMap(int size) {
        super(size);
    }

    public static NoAutoIdEntityMap create() {
        return new NoAutoIdEntityMap();
    }

    public static NoAutoIdEntityMap create(int size) {
        return new NoAutoIdEntityMap(size);
    }

    public static class Factory {
        public NoAutoIdEntityMap create() {
            return NoAutoIdEntityMap.create();
        }

        public NoAutoIdEntityMap create(int size) {
            return NoAutoIdEntityMap.create(size);
        }
    }
}
