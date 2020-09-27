package cn.org.atool.fluent.mybatis.generate.datamap.table;

import org.test4j.module.database.annotations.ColumnDef;
import org.test4j.module.database.annotations.ScriptTable;
import org.test4j.module.ICore.DataMap;
import org.test4j.tools.datagen.KeyValue;

import java.util.Date;
import java.util.function.Consumer;

/**
 * @ClassName AddressTableMap
 * @Description AddressTableMap
 *
 * @author generate code
 */
@ScriptTable("address")
public class AddressTableMap extends DataMap<AddressTableMap> {
    /**
     * 设置address对象id字段值
     */
    @ColumnDef(type = "bigint(21) unsigned", primary = true, autoIncrease = true)
    public transient final KeyValue<AddressTableMap> id = new KeyValue(this, "id");
    /**
     * 设置address对象gmt_created字段值
     */
    @ColumnDef(type = "datetime")
    public transient final KeyValue<AddressTableMap> gmt_created = new KeyValue(this, "gmt_created");
    /**
     * 设置address对象gmt_modified字段值
     */
    @ColumnDef(type = "datetime")
    public transient final KeyValue<AddressTableMap> gmt_modified = new KeyValue(this, "gmt_modified");
    /**
     * 设置address对象is_deleted字段值
     */
    @ColumnDef(type = "tinyint(2)")
    public transient final KeyValue<AddressTableMap> is_deleted = new KeyValue(this, "is_deleted");
    /**
     * 设置address对象address字段值
     */
    @ColumnDef(type = "varchar(45)")
    public transient final KeyValue<AddressTableMap> address = new KeyValue(this, "address");
    /**
     * 设置address对象user_id字段值
     */
    @ColumnDef(type = "bigint(20)")
    public transient final KeyValue<AddressTableMap> user_id = new KeyValue(this, "user_id");

    public AddressTableMap() {
        super();
    }

    public AddressTableMap(int size) {
        super(size);
    }

    /**
     * 创建AddressTableMap
     * 并初始化主键和gmtCreate, gmtModified, isDeleted等特殊值
     *
     */
    public AddressTableMap init() {
        this.id.autoIncrease();
        this.gmt_created.values(new Date());
        this.gmt_modified.values(new Date());
        this.is_deleted.values(false);
        return this;
    }

    public AddressTableMap with(Consumer<AddressTableMap> init) {
        init.accept(this);
        return this;
    }

    public static AddressTableMap create() {
        return new AddressTableMap(1);
    }

    public static AddressTableMap create(int size) {
        return new AddressTableMap(size);
    }

    public static class Factory {
        public AddressTableMap create() {
            return AddressTableMap.create();
        }

        public AddressTableMap create(int size) {
            return AddressTableMap.create(size);
        }

        public AddressTableMap createWithInit() {
            return AddressTableMap.create(1).init();
        }

        public AddressTableMap createWithInit(int size) {
            return AddressTableMap.create(size).init();
        }
    }
}