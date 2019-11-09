package cn.org.atool.fluent.mybatis.demo.datamap.table;

import cn.org.atool.fluent.mybatis.annotation.ColumnDef;
import com.baomidou.mybatisplus.annotation.TableName;
import org.test4j.module.ICore.DataGenerator;
import org.test4j.module.ICore.DataMap;
import org.test4j.tools.datagen.KeyValue;

import java.util.Date;
import java.util.function.Consumer;

import cn.org.atool.fluent.mybatis.demo.mapping.UserMP;
import cn.org.atool.fluent.mybatis.demo.mapping.UserMP.Column;

/**
 * @ClassName UserTableMap
 * @Description UserTableMap
 *
 * @author generate code
 */
@TableName(UserMP.Table_Name)
public class UserTableMap extends DataMap<UserTableMap> {
    /**
     * 设置t_user对象id字段值
     */
    @ColumnDef(type = "bigint(21) unsigned", primary = true)
    public transient final KeyValue<UserTableMap> id = new KeyValue(this, Column.id);
    /**
     * 设置t_user对象gmt_created字段值
     */
    @ColumnDef(type = "datetime")
    public transient final KeyValue<UserTableMap> gmt_created = new KeyValue(this, Column.gmt_created);
    /**
     * 设置t_user对象gmt_modified字段值
     */
    @ColumnDef(type = "datetime")
    public transient final KeyValue<UserTableMap> gmt_modified = new KeyValue(this, Column.gmt_modified);
    /**
     * 设置t_user对象is_deleted字段值
     */
    @ColumnDef(type = "tinyint(2)")
    public transient final KeyValue<UserTableMap> is_deleted = new KeyValue(this, Column.is_deleted);
    /**
     * 设置t_user对象address_id字段值
     */
    @ColumnDef(type = "bigint(21)")
    public transient final KeyValue<UserTableMap> address_id = new KeyValue(this, Column.address_id);
    /**
     * 设置t_user对象age字段值
     */
    @ColumnDef(type = "int(11)")
    public transient final KeyValue<UserTableMap> age = new KeyValue(this, Column.age);
    /**
     * 设置t_user对象user_name字段值
     */
    @ColumnDef(type = "varchar(45)")
    public transient final KeyValue<UserTableMap> user_name = new KeyValue(this, Column.user_name);
    /**
     * 设置t_user对象version字段值
     */
    @ColumnDef(type = "varchar(45)")
    public transient final KeyValue<UserTableMap> version = new KeyValue(this, Column.version);

    protected UserTableMap() {
        super();
    }

    protected UserTableMap(int size) {
        super(size);
    }

    /**
     * 创建UserTableMap
     * 并初始化主键和gmtCreate, gmtModified, isDeleted等特殊值
     */
    public static UserTableMap init() {
        return init(1);
    }

    /**
     * 创建UserTableMap
     * 并初始化主键和gmtCreate, gmtModified, isDeleted等特殊值
     *
     * @param size
     */
    public static UserTableMap init(int size) {
        return new UserTableMap(size)
                .id.values(DataGenerator.increase(1,1))
                .gmt_created.values(new Date())
                .gmt_modified.values(new Date())
                .is_deleted.values(false)
                ;
    }

    public UserTableMap with(Consumer<UserTableMap> init) {
        init.accept(this);
        return this;
    }

    public static UserTableMap create() {
        return new UserTableMap();
    }

    public static UserTableMap create(int size) {
        return new UserTableMap(size);
    }
}
