package cn.org.atool.fluent.mybatis.demo.generate.helper;

import cn.org.atool.fluent.mybatis.demo.generate.entity.UserEntity;
import cn.org.atool.fluent.mybatis.demo.generate.mapping.UserMP;

import java.util.HashMap;
import java.util.Map;
import java.util.Date;

/**
 * <p>
 * 
 * </p>
 *
 * @author generate code
 */
public class UserEntityHelper {
    /**
     * UserEntity对象转换为HashMap，key值是对象属性
     *
     * @param entity
     * @return
     */
    public static Map<String, Object> map(UserEntity entity){
        Map<String, Object> map = new HashMap<>();
        if (entity.getId() != null) {
            map.put(UserMP.id.name, entity.getId());
        }
        if (entity.getGmtCreated() != null) {
            map.put(UserMP.gmtCreated.name, entity.getGmtCreated());
        }
        if (entity.getGmtModified() != null) {
            map.put(UserMP.gmtModified.name, entity.getGmtModified());
        }
        if (entity.getIsDeleted() != null) {
            map.put(UserMP.isDeleted.name, entity.getIsDeleted());
        }
        if (entity.getAddressId() != null) {
            map.put(UserMP.addressId.name, entity.getAddressId());
        }
        if (entity.getAge() != null) {
            map.put(UserMP.age.name, entity.getAge());
        }
        if (entity.getUserName() != null) {
            map.put(UserMP.userName.name, entity.getUserName());
        }
        if (entity.getVersion() != null) {
            map.put(UserMP.version.name, entity.getVersion());
        }
        return map;
    }

    /**
     * UserEntity对象转换为HashMap，key值是数据库字段
     *
     * @param entity
     * @return
     */
    public static Map<String, Object> column(UserEntity entity){
        Map<String, Object> map = new HashMap<>();
        if (entity.getId() != null) {
            map.put(UserMP.id.column, entity.getId());
        }
        if (entity.getGmtCreated() != null) {
            map.put(UserMP.gmtCreated.column, entity.getGmtCreated());
        }
        if (entity.getGmtModified() != null) {
            map.put(UserMP.gmtModified.column, entity.getGmtModified());
        }
        if (entity.getIsDeleted() != null) {
            map.put(UserMP.isDeleted.column, entity.getIsDeleted());
        }
        if (entity.getAddressId() != null) {
            map.put(UserMP.addressId.column, entity.getAddressId());
        }
        if (entity.getAge() != null) {
            map.put(UserMP.age.column, entity.getAge());
        }
        if (entity.getUserName() != null) {
            map.put(UserMP.userName.column, entity.getUserName());
        }
        if (entity.getVersion() != null) {
            map.put(UserMP.version.column, entity.getVersion());
        }
        return map;
    }

    /**
     * map对应属性值设置到UserEntity对象中
     *
     * @param map
     * @return
     */
    public static UserEntity entity(Map<String, Object> map){
        UserEntity entity = new UserEntity();
        if (map.containsKey(UserMP.id.name)) {
            entity.setId((Long) map.get(UserMP.id.name));
        }
        if (map.containsKey(UserMP.gmtCreated.name)) {
            entity.setGmtCreated((Date) map.get(UserMP.gmtCreated.name));
        }
        if (map.containsKey(UserMP.gmtModified.name)) {
            entity.setGmtModified((Date) map.get(UserMP.gmtModified.name));
        }
        if (map.containsKey(UserMP.isDeleted.name)) {
            entity.setIsDeleted((Boolean) map.get(UserMP.isDeleted.name));
        }
        if (map.containsKey(UserMP.addressId.name)) {
            entity.setAddressId((Long) map.get(UserMP.addressId.name));
        }
        if (map.containsKey(UserMP.age.name)) {
            entity.setAge((Integer) map.get(UserMP.age.name));
        }
        if (map.containsKey(UserMP.userName.name)) {
            entity.setUserName((String) map.get(UserMP.userName.name));
        }
        if (map.containsKey(UserMP.version.name)) {
            entity.setVersion((String) map.get(UserMP.version.name));
        }
        return entity;
    }

    public static UserEntity copy(UserEntity entity) {
        UserEntity copy = new UserEntity();
        {
            copy.setId(entity.getId());
            copy.setGmtCreated(entity.getGmtCreated());
            copy.setGmtModified(entity.getGmtModified());
            copy.setIsDeleted(entity.getIsDeleted());
            copy.setAddressId(entity.getAddressId());
            copy.setAge(entity.getAge());
            copy.setUserName(entity.getUserName());
            copy.setVersion(entity.getVersion());
        }
        return copy;
    }
}