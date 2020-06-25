package cn.org.atool.fluent.mybatis.tutorial.datamap.table;

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
@ScriptTable("user")
public class UserTableMap extends DataMap<UserTableMap> {
    /**
     * 设置user对象id字段值
     */
    @ColumnDef(type = "bigint(21) unsigned", primary = true, autoIncrease = true)
    public transient final KeyValue<UserTableMap> id = new KeyValue(this, "id");
    /**
     * 设置user对象gmt_modified字段值
     */
    @ColumnDef(type = "datetime")
    public transient final KeyValue<UserTableMap> gmt_modified = new KeyValue(this, "gmt_modified");
    /**
     * 设置user对象is_deleted字段值
     */
    @ColumnDef(type = "tinyint(2)")
    public transient final KeyValue<UserTableMap> is_deleted = new KeyValue(this, "is_deleted");
    /**
     * 设置user对象account字段值
     */
    @ColumnDef(type = "varchar(45)")
    public transient final KeyValue<UserTableMap> account = new KeyValue(this, "account");
    /**
     * 设置user对象avatar字段值
     */
    @ColumnDef(type = "varchar(255)")
    public transient final KeyValue<UserTableMap> avatar = new KeyValue(this, "avatar");
    /**
     * 设置user对象birthday字段值
     */
    @ColumnDef(type = "datetime")
    public transient final KeyValue<UserTableMap> birthday = new KeyValue(this, "birthday");
    /**
     * 设置user对象e_mail字段值
     */
    @ColumnDef(type = "varchar(45)")
    public transient final KeyValue<UserTableMap> e_mail = new KeyValue(this, "e_mail");
    /**
     * 设置user对象gmt_create字段值
     */
    @ColumnDef(type = "datetime")
    public transient final KeyValue<UserTableMap> gmt_create = new KeyValue(this, "gmt_create");
    /**
     * 设置user对象password字段值
     */
    @ColumnDef(type = "varchar(45)")
    public transient final KeyValue<UserTableMap> password = new KeyValue(this, "password");
    /**
     * 设置user对象phone字段值
     */
    @ColumnDef(type = "varchar(20)")
    public transient final KeyValue<UserTableMap> phone = new KeyValue(this, "phone");
    /**
     * 设置user对象status字段值
     */
    @ColumnDef(type = "varchar(32)")
    public transient final KeyValue<UserTableMap> status = new KeyValue(this, "status");
    /**
     * 设置user对象user_name字段值
     */
    @ColumnDef(type = "varchar(45)")
    public transient final KeyValue<UserTableMap> user_name = new KeyValue(this, "user_name");

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