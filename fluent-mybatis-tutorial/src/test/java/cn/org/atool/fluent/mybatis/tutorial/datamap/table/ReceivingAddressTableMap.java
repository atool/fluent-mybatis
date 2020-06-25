package cn.org.atool.fluent.mybatis.tutorial.datamap.table;

import org.test4j.module.database.annotations.ColumnDef;
import org.test4j.module.database.annotations.ScriptTable;
import org.test4j.module.ICore.DataMap;
import org.test4j.tools.datagen.KeyValue;

import java.util.Date;
import java.util.function.Consumer;

/**
 * @ClassName ReceivingAddressTableMap
 * @Description ReceivingAddressTableMap
 *
 * @author generate code
 */
@ScriptTable("receiving_address")
public class ReceivingAddressTableMap extends DataMap<ReceivingAddressTableMap> {
    /**
     * 设置receiving_address对象id字段值
     */
    @ColumnDef(type = "bigint(21) unsigned", primary = true, autoIncrease = true)
    public transient final KeyValue<ReceivingAddressTableMap> id = new KeyValue(this, "id");
    /**
     * 设置receiving_address对象gmt_modified字段值
     */
    @ColumnDef(type = "datetime")
    public transient final KeyValue<ReceivingAddressTableMap> gmt_modified = new KeyValue(this, "gmt_modified");
    /**
     * 设置receiving_address对象is_deleted字段值
     */
    @ColumnDef(type = "tinyint(2)")
    public transient final KeyValue<ReceivingAddressTableMap> is_deleted = new KeyValue(this, "is_deleted");
    /**
     * 设置receiving_address对象city字段值
     */
    @ColumnDef(type = "varchar(50)")
    public transient final KeyValue<ReceivingAddressTableMap> city = new KeyValue(this, "city");
    /**
     * 设置receiving_address对象detail_address字段值
     */
    @ColumnDef(type = "varchar(100)")
    public transient final KeyValue<ReceivingAddressTableMap> detail_address = new KeyValue(this, "detail_address");
    /**
     * 设置receiving_address对象district字段值
     */
    @ColumnDef(type = "varchar(50)")
    public transient final KeyValue<ReceivingAddressTableMap> district = new KeyValue(this, "district");
    /**
     * 设置receiving_address对象gmt_create字段值
     */
    @ColumnDef(type = "datetime")
    public transient final KeyValue<ReceivingAddressTableMap> gmt_create = new KeyValue(this, "gmt_create");
    /**
     * 设置receiving_address对象province字段值
     */
    @ColumnDef(type = "varchar(50)")
    public transient final KeyValue<ReceivingAddressTableMap> province = new KeyValue(this, "province");
    /**
     * 设置receiving_address对象user_id字段值
     */
    @ColumnDef(type = "bigint(21)")
    public transient final KeyValue<ReceivingAddressTableMap> user_id = new KeyValue(this, "user_id");

    public ReceivingAddressTableMap() {
        super();
    }

    public ReceivingAddressTableMap(int size) {
        super(size);
    }

    /**
     * 创建ReceivingAddressTableMap
     * 并初始化主键和gmtCreate, gmtModified, isDeleted等特殊值
     *
     */
    public ReceivingAddressTableMap init() {
        this.id.autoIncrease();
        this.gmt_modified.values(new Date());
        this.is_deleted.values(false);
        return this;
    }

    public ReceivingAddressTableMap with(Consumer<ReceivingAddressTableMap> init) {
        init.accept(this);
        return this;
    }

    public static ReceivingAddressTableMap create() {
        return new ReceivingAddressTableMap(1);
    }

    public static ReceivingAddressTableMap create(int size) {
        return new ReceivingAddressTableMap(size);
    }

    public static class Factory {
        public ReceivingAddressTableMap create() {
            return ReceivingAddressTableMap.create();
        }

        public ReceivingAddressTableMap create(int size) {
            return ReceivingAddressTableMap.create(size);
        }

        public ReceivingAddressTableMap createWithInit() {
            return ReceivingAddressTableMap.create(1).init();
        }

        public ReceivingAddressTableMap createWithInit(int size) {
            return ReceivingAddressTableMap.create(size).init();
        }
    }
}