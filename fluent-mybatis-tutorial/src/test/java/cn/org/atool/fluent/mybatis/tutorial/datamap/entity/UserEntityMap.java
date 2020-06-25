package cn.org.atool.fluent.mybatis.tutorial.datamap.entity;

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
     * 设置UserEntity对象gmtModified字段值
     */
    public transient final KeyValue<UserEntityMap> gmtModified = new KeyValue(this, "gmtModified");
    /**
     * 设置UserEntity对象isDeleted字段值
     */
    public transient final KeyValue<UserEntityMap> isDeleted = new KeyValue(this, "isDeleted");
    /**
     * 设置UserEntity对象account字段值
     */
    public transient final KeyValue<UserEntityMap> account = new KeyValue(this, "account");
    /**
     * 设置UserEntity对象avatar字段值
     */
    public transient final KeyValue<UserEntityMap> avatar = new KeyValue(this, "avatar");
    /**
     * 设置UserEntity对象birthday字段值
     */
    public transient final KeyValue<UserEntityMap> birthday = new KeyValue(this, "birthday");
    /**
     * 设置UserEntity对象eMail字段值
     */
    public transient final KeyValue<UserEntityMap> eMail = new KeyValue(this, "eMail");
    /**
     * 设置UserEntity对象gmtCreate字段值
     */
    public transient final KeyValue<UserEntityMap> gmtCreate = new KeyValue(this, "gmtCreate");
    /**
     * 设置UserEntity对象password字段值
     */
    public transient final KeyValue<UserEntityMap> password = new KeyValue(this, "password");
    /**
     * 设置UserEntity对象phone字段值
     */
    public transient final KeyValue<UserEntityMap> phone = new KeyValue(this, "phone");
    /**
     * 设置UserEntity对象status字段值
     */
    public transient final KeyValue<UserEntityMap> status = new KeyValue(this, "status");
    /**
     * 设置UserEntity对象userName字段值
     */
    public transient final KeyValue<UserEntityMap> userName = new KeyValue(this, "userName");

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