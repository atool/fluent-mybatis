package cn.org.atool.fluent.mybatis.demo.dm.entity;

import cn.org.atool.fluent.mybatis.demo.mapping.UserMP;
import org.test4j.module.ICore.DataMap;
import org.test4j.tools.datagen.KeyValue;

/**
 * @ClassName UserEntityMap
 * @Description UserEntityMap
 *
 * @author generate code
 */
public class UserEntityMap extends DataMap<UserEntityMap> {
    /**
     * 设置UserEntity对象id字段值
     */
    public transient final KeyValue<UserEntityMap> id = new KeyValue(this, UserMP.Property.id);
    /**
     * 设置UserEntity对象userName字段值
     */
    public transient final KeyValue<UserEntityMap> userName = new KeyValue(this, UserMP.Property.userName);
    /**
     * 设置UserEntity对象addressId字段值
     */
    public transient final KeyValue<UserEntityMap> addressId = new KeyValue(this, UserMP.Property.addressId);
    /**
     * 设置UserEntity对象gmtCreated字段值
     */
    public transient final KeyValue<UserEntityMap> gmtCreated = new KeyValue(this, UserMP.Property.gmtCreated);
    /**
     * 设置UserEntity对象gmtModified字段值
     */
    public transient final KeyValue<UserEntityMap> gmtModified = new KeyValue(this, UserMP.Property.gmtModified);
    /**
     * 设置UserEntity对象isDeleted字段值
     */
    public transient final KeyValue<UserEntityMap> isDeleted = new KeyValue(this, UserMP.Property.isDeleted);
    /**
     * 设置UserEntity对象age字段值
     */
    public transient final KeyValue<UserEntityMap> age = new KeyValue(this, UserMP.Property.age);
    /**
     * 设置UserEntity对象version字段值
     */
    public transient final KeyValue<UserEntityMap> version = new KeyValue(this, UserMP.Property.version);

    public UserEntityMap(){
        super();
    }

    public UserEntityMap(int size){
        super(size);
    }

    public static UserEntityMap create(){
        return new UserEntityMap();
    }

    public static UserEntityMap create(int size){
        return new UserEntityMap(size);
    }
}
