package cn.org.atool.mybatis.fluent.demo.dm.entity;

import cn.org.atool.mybatis.fluent.demo.mapping.AddressMP;
import org.test4j.module.ICore.DataMap;
import org.test4j.tools.datagen.KeyValue;

/**
 * @ClassName AddressEntityMap
 * @Description AddressEntityMap
 *
 * @author generate code
 */
public class AddressEntityMap extends DataMap<AddressEntityMap> {
    /**
     * 设置AddressEntity对象id字段值
     */
    public transient final KeyValue<AddressEntityMap> id = new KeyValue(this, AddressMP.Property.id);
    /**
     * 设置AddressEntity对象address字段值
     */
    public transient final KeyValue<AddressEntityMap> address = new KeyValue(this, AddressMP.Property.address);
    /**
     * 设置AddressEntity对象isDeleted字段值
     */
    public transient final KeyValue<AddressEntityMap> isDeleted = new KeyValue(this, AddressMP.Property.isDeleted);
    /**
     * 设置AddressEntity对象gmtCreated字段值
     */
    public transient final KeyValue<AddressEntityMap> gmtCreated = new KeyValue(this, AddressMP.Property.gmtCreated);
    /**
     * 设置AddressEntity对象gmtModified字段值
     */
    public transient final KeyValue<AddressEntityMap> gmtModified = new KeyValue(this, AddressMP.Property.gmtModified);

    public AddressEntityMap(){
        super();
    }

    public AddressEntityMap(int size){
        super(size);
    }

    public static AddressEntityMap create(){
        return new AddressEntityMap();
    }

    public static AddressEntityMap create(int size){
        return new AddressEntityMap(size);
    }
}
