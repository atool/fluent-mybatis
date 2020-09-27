package cn.org.atool.fluent.mybatis.generate.datamap.entity;

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
    public transient final KeyValue<UserEntityMap> id = new KeyValue(this, "id");
    /**
     * 设置UserEntity对象gmtCreated字段值
     */
    public transient final KeyValue<UserEntityMap> gmtCreated = new KeyValue(this, "gmtCreated");
    /**
     * 设置UserEntity对象gmtModified字段值
     */
    public transient final KeyValue<UserEntityMap> gmtModified = new KeyValue(this, "gmtModified");
    /**
     * 设置UserEntity对象isDeleted字段值
     */
    public transient final KeyValue<UserEntityMap> isDeleted = new KeyValue(this, "isDeleted");
    /**
     * 设置UserEntity对象addressId字段值
     */
    public transient final KeyValue<UserEntityMap> addressId = new KeyValue(this, "addressId");
    /**
     * 设置UserEntity对象age字段值
     */
    public transient final KeyValue<UserEntityMap> age = new KeyValue(this, "age");
    /**
     * 设置UserEntity对象grade字段值
     */
    public transient final KeyValue<UserEntityMap> grade = new KeyValue(this, "grade");
    /**
     * 设置UserEntity对象userName字段值
     */
    public transient final KeyValue<UserEntityMap> userName = new KeyValue(this, "userName");
    /**
     * 设置UserEntity对象version字段值
     */
    public transient final KeyValue<UserEntityMap> version = new KeyValue(this, "version");

    public UserEntityMap() {
        super();
    }

    public UserEntityMap(int size) {
        super(size);
    }

    public static UserEntityMap create() {
        return new UserEntityMap();
    }

    public static UserEntityMap create(int size) {
        return new UserEntityMap(size);
    }

    public static class Factory {
        public UserEntityMap create() {
            return UserEntityMap.create();
        }

        public UserEntityMap create(int size) {
            return UserEntityMap.create(size);
        }
    }
}