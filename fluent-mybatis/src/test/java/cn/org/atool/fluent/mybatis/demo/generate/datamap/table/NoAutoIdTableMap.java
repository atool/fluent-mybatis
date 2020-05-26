package cn.org.atool.fluent.mybatis.demo.generate.datamap.table;

import org.test4j.module.database.annotations.ColumnDef;
import org.test4j.module.database.annotations.ScriptTable;
import org.test4j.module.ICore.DataMap;
import org.test4j.tools.datagen.KeyValue;

import java.util.Date;
import java.util.function.Consumer;

/**
 * @ClassName NoAutoIdTableMap
 * @Description NoAutoIdTableMap
 *
 * @author generate code
 */
@ScriptTable("no_auto_id")
public class NoAutoIdTableMap extends DataMap<NoAutoIdTableMap> {
    /**
     * 设置no_auto_id对象id字段值
     */
    @ColumnDef(type = "varchar(50)", primary = true)
    public transient final KeyValue<NoAutoIdTableMap> id = new KeyValue(this, "id");
    /**
     * 设置no_auto_id对象column_1字段值
     */
    @ColumnDef(type = "varchar(20)")
    public transient final KeyValue<NoAutoIdTableMap> column_1 = new KeyValue(this, "column_1");

    public NoAutoIdTableMap() {
        super();
    }

    public NoAutoIdTableMap(int size) {
        super(size);
    }

    /**
     * 创建NoAutoIdTableMap
     * 并初始化主键和gmtCreate, gmtModified, isDeleted等特殊值
     *
     */
    public NoAutoIdTableMap init() {
        return this;
    }

    public NoAutoIdTableMap with(Consumer<NoAutoIdTableMap> init) {
        init.accept(this);
        return this;
    }

    public static NoAutoIdTableMap create() {
        return new NoAutoIdTableMap(1);
    }

    public static NoAutoIdTableMap create(int size) {
        return new NoAutoIdTableMap(size);
    }

    public static class Factory {
        public NoAutoIdTableMap create() {
            return NoAutoIdTableMap.create();
        }

        public NoAutoIdTableMap create(int size) {
            return NoAutoIdTableMap.create(size);
        }

        public NoAutoIdTableMap createWithInit() {
            return NoAutoIdTableMap.create(1).init();
        }

        public NoAutoIdTableMap createWithInit(int size) {
            return NoAutoIdTableMap.create(size).init();
        }
    }
}