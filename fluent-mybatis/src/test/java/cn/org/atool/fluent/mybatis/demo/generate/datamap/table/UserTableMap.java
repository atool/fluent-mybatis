package cn.org.atool.fluent.mybatis.demo.generate.datamap.table;

import org.test4j.module.database.annotations.ColumnDef;
import org.test4j.module.database.annotations.ScriptTable;
import org.test4j.module.ICore.DataMap;
import org.test4j.tools.datagen.KeyValue;

import java.util.Date;
import java.util.function.Consumer;

/**
 * @ClassName UserTableMap
 * @Description UserTableMap
 *
 * @author generate code
 */
@ScriptTable("t_user")
public class UserTableMap extends DataMap<UserTableMap> {
    /**
     * 设置t_user对象id字段值
     */
    @ColumnDef(type = "bigint(21) unsigned", primary = true, autoIncrease = true)
    public transient final KeyValue<UserTableMap> id = new KeyValue(this, "id");
    /**
     * 设置t_user对象gmt_created字段值
     */
    @ColumnDef(type = "datetime")
    public transient final KeyValue<UserTableMap> gmt_created = new KeyValue(this, "gmt_created");
    /**
     * 设置t_user对象gmt_modified字段值
     */
    @ColumnDef(type = "datetime")
    public transient final KeyValue<UserTableMap> gmt_modified = new KeyValue(this, "gmt_modified");
    /**
     * 设置t_user对象is_deleted字段值
     */
    @ColumnDef(type = "tinyint(2)")
    public transient final KeyValue<UserTableMap> is_deleted = new KeyValue(this, "is_deleted");
    /**
     * 设置t_user对象address_id字段值
     */
    @ColumnDef(type = "bigint(21)")
    public transient final KeyValue<UserTableMap> address_id = new KeyValue(this, "address_id");
    /**
     * 设置t_user对象age字段值
     */
    @ColumnDef(type = "int(11)")
    public transient final KeyValue<UserTableMap> age = new KeyValue(this, "age");
    /**
     * 设置t_user对象user_name字段值
     */
    @ColumnDef(type = "varchar(45)")
    public transient final KeyValue<UserTableMap> user_name = new KeyValue(this, "user_name");
    /**
     * 设置t_user对象version字段值
     */
    @ColumnDef(type = "varchar(45)")
    public transient final KeyValue<UserTableMap> version = new KeyValue(this, "version");

    public UserTableMap() {
        super();
    }

    public UserTableMap(int size) {
        super(size);
    }

    /**
     * 创建UserTableMap
     * 并初始化主键和gmtCreate, gmtModified, isDeleted等特殊值
     *
     */
    public UserTableMap init() {
        this.id.autoIncrease();
        this.gmt_created.values(new Date());
        this.gmt_modified.values(new Date());
        this.is_deleted.values(false);
        return this;
    }

    public UserTableMap with(Consumer<UserTableMap> init) {
        init.accept(this);
        return this;
    }

    public static UserTableMap create() {
        return new UserTableMap(1);
    }

    public static UserTableMap create(int size) {
        return new UserTableMap(size);
    }

    public static class Factory {
        public UserTableMap create() {
            return UserTableMap.create();
        }

        public UserTableMap create(int size) {
            return UserTableMap.create(size);
        }

        public UserTableMap createWithInit() {
            return UserTableMap.create(1).init();
        }

        public UserTableMap createWithInit(int size) {
            return UserTableMap.create(size).init();
        }
    }
}