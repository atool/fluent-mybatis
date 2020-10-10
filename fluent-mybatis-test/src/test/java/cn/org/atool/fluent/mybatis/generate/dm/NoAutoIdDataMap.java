package cn.org.atool.fluent.mybatis.generate.dm;

import org.test4j.module.database.annotations.ColumnDef;
import org.test4j.module.database.annotations.ScriptTable;
import org.test4j.module.ICore.DataMap;
import org.test4j.tools.datagen.KeyValue;

import java.util.Date;
import java.util.function.Consumer;
import java.util.function.Supplier;

/**
 * @ClassName NoAutoIdDataMap
 * @Description NoAutoIdDataMap
 *
 * @author generate code
 */
@ScriptTable("no_auto_id")
public class NoAutoIdDataMap extends DataMap<NoAutoIdDataMap> {
    private boolean isTable;

    private Supplier<Boolean> supplier = () -> this.isTable;

    @ColumnDef(value = "id", type = "varchar(50)", primary = true)
    public transient final KeyValue<NoAutoIdDataMap> id = new KeyValue(this, "id", "id", supplier);

    @ColumnDef(value = "column_1", type = "varchar(20)")
    public transient final KeyValue<NoAutoIdDataMap> column1 = new KeyValue(this, "column_1", "column1", supplier);

    public NoAutoIdDataMap(boolean isTable) {
        super();
        this.isTable = isTable;
    }

    public NoAutoIdDataMap(boolean isTable, int size) {
        super(size);
        this.isTable = isTable;
    }

    /**
     * 创建NoAutoIdDataMap
     * 并初始化主键和gmtCreate, gmtModified, isDeleted等特殊值
     *
     */
    public NoAutoIdDataMap init() {
        return this;
    }

    public NoAutoIdDataMap with(Consumer<NoAutoIdDataMap> init) {
        init.accept(this);
        return this;
    }

    public static NoAutoIdDataMap table() {
        return new NoAutoIdDataMap(true, 1);
    }

    public static NoAutoIdDataMap table(int size) {
        return new NoAutoIdDataMap(true, size);
    }

    public static NoAutoIdDataMap entity() {
        return new NoAutoIdDataMap(false);
    }

    public static NoAutoIdDataMap entity(int size) {
        return new NoAutoIdDataMap(false, size);
    }

    public static class Factory {
        public NoAutoIdDataMap table() {
            return NoAutoIdDataMap.table();
        }

        public NoAutoIdDataMap table(int size) {
            return NoAutoIdDataMap.table(size);
        }

        public NoAutoIdDataMap initTable() {
            return NoAutoIdDataMap.table(1).init();
        }

        public NoAutoIdDataMap initTable(int size) {
            return NoAutoIdDataMap.table(size).init();
        }

        public NoAutoIdDataMap entity() {
            return NoAutoIdDataMap.entity();
        }

        public NoAutoIdDataMap entity(int size) {
            return NoAutoIdDataMap.entity(size);
        }
    }
}
