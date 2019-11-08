package cn.org.atool.fluent.mybatis.demo.datamap.table;

import cn.org.atool.fluent.mybatis.annotation.ColumnDef;
import com.baomidou.mybatisplus.annotation.TableName;
import org.test4j.module.ICore.DataGenerator;
import org.test4j.module.ICore.DataMap;
import org.test4j.tools.datagen.KeyValue;

import java.util.Date;
import java.util.function.Consumer;

import cn.org.atool.fluent.mybatis.demo.mapping.AddressMP;
import cn.org.atool.fluent.mybatis.demo.mapping.AddressMP.Column;

/**
 * @ClassName AddressTableMap
 * @Description AddressTableMap
 *
 * @author generate code
 */
@TableName(AddressMP.Table_Name)
public class AddressTableMap extends DataMap<AddressTableMap> {
    /**
     * 设置address对象id字段值
     */
    @ColumnDef(type = "bigint(21) unsigned", primary = true)
    public transient final KeyValue<AddressTableMap> id = new KeyValue(this, Column.id);
    /**
     * 设置address对象gmt_created字段值
     */
    @ColumnDef(type = "datetime")
    public transient final KeyValue<AddressTableMap> gmt_created = new KeyValue(this, Column.gmt_created);
    /**
     * 设置address对象gmt_modified字段值
     */
    @ColumnDef(type = "datetime")
    public transient final KeyValue<AddressTableMap> gmt_modified = new KeyValue(this, Column.gmt_modified);
    /**
     * 设置address对象is_deleted字段值
     */
    @ColumnDef(type = "tinyint(2)")
    public transient final KeyValue<AddressTableMap> is_deleted = new KeyValue(this, Column.is_deleted);
    /**
     * 设置address对象address字段值
     */
    @ColumnDef(type = "varchar(45)")
    public transient final KeyValue<AddressTableMap> address = new KeyValue(this, Column.address);

    public AddressTableMap() {
        super();
    }

    public AddressTableMap(int size) {
        super(size);
    }

    /**
     * 创建AddressTableMap
     * 并初始化主键和gmtCreate, gmtModified, isDeleted等特殊值
     */
    public static AddressTableMap init() {
        return init(1);
    }

    /**
     * 创建AddressTableMap
     * 并初始化主键和gmtCreate, gmtModified, isDeleted等特殊值
     *
     * @param size
     */
    public static AddressTableMap init(int size) {
        return new AddressTableMap(size)
                    .id.values(DataGenerator.increase(1,1))
                .gmt_created.values(new Date())
                .gmt_modified.values(new Date())
                .is_deleted.values(false)
                ;
        }

    public AddressTableMap with(Consumer<AddressTableMap> init) {
        init.accept(this);
        return this;
    }

    public static AddressTableMap create() {
        return new AddressTableMap();
    }

    public static AddressTableMap create(int size) {
        return new AddressTableMap(size);
    }
}
