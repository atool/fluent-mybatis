package cn.org.atool.fluent.mybatis.demo.generate.helper;

import cn.org.atool.fluent.mybatis.demo.generate.entity.UserEntity;
import cn.org.atool.fluent.mybatis.demo.generate.helper.UserMapping;

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
            map.put(UserMapping.id.name, entity.getId());
        }
        if (entity.getGmtCreated() != null) {
            map.put(UserMapping.gmtCreated.name, entity.getGmtCreated());
        }
        if (entity.getGmtModified() != null) {
            map.put(UserMapping.gmtModified.name, entity.getGmtModified());
        }
        if (entity.getIsDeleted() != null) {
            map.put(UserMapping.isDeleted.name, entity.getIsDeleted());
        }
        if (entity.getAddressId() != null) {
            map.put(UserMapping.addressId.name, entity.getAddressId());
        }
        if (entity.getAge() != null) {
            map.put(UserMapping.age.name, entity.getAge());
        }
        if (entity.getGrade() != null) {
            map.put(UserMapping.grade.name, entity.getGrade());
        }
        if (entity.getUserName() != null) {
            map.put(UserMapping.userName.name, entity.getUserName());
        }
        if (entity.getVersion() != null) {
            map.put(UserMapping.version.name, entity.getVersion());
        }
        return map;
    }

    /**
     * UserEntity对象转换为HashMap，key值是数据库字段
     *
     * @param entity
     * @return
     */
    public static Map<String, Object> columnMap(UserEntity entity){
        Map<String, Object> map = new HashMap<>();
        if (entity.getId() != null) {
            map.put(UserMapping.id.column, entity.getId());
        }
        if (entity.getGmtCreated() != null) {
            map.put(UserMapping.gmtCreated.column, entity.getGmtCreated());
        }
        if (entity.getGmtModified() != null) {
            map.put(UserMapping.gmtModified.column, entity.getGmtModified());
        }
        if (entity.getIsDeleted() != null) {
            map.put(UserMapping.isDeleted.column, entity.getIsDeleted());
        }
        if (entity.getAddressId() != null) {
            map.put(UserMapping.addressId.column, entity.getAddressId());
        }
        if (entity.getAge() != null) {
            map.put(UserMapping.age.column, entity.getAge());
        }
        if (entity.getGrade() != null) {
            map.put(UserMapping.grade.column, entity.getGrade());
        }
        if (entity.getUserName() != null) {
            map.put(UserMapping.userName.column, entity.getUserName());
        }
        if (entity.getVersion() != null) {
            map.put(UserMapping.version.column, entity.getVersion());
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
        if (map.containsKey(UserMapping.id.name)) {
            entity.setId((Long) map.get(UserMapping.id.name));
        }
        if (map.containsKey(UserMapping.gmtCreated.name)) {
            entity.setGmtCreated((Date) map.get(UserMapping.gmtCreated.name));
        }
        if (map.containsKey(UserMapping.gmtModified.name)) {
            entity.setGmtModified((Date) map.get(UserMapping.gmtModified.name));
        }
        if (map.containsKey(UserMapping.isDeleted.name)) {
            entity.setIsDeleted((Boolean) map.get(UserMapping.isDeleted.name));
        }
        if (map.containsKey(UserMapping.addressId.name)) {
            entity.setAddressId((Long) map.get(UserMapping.addressId.name));
        }
        if (map.containsKey(UserMapping.age.name)) {
            entity.setAge((Integer) map.get(UserMapping.age.name));
        }
        if (map.containsKey(UserMapping.grade.name)) {
            entity.setGrade((Integer) map.get(UserMapping.grade.name));
        }
        if (map.containsKey(UserMapping.userName.name)) {
            entity.setUserName((String) map.get(UserMapping.userName.name));
        }
        if (map.containsKey(UserMapping.version.name)) {
            entity.setVersion((String) map.get(UserMapping.version.name));
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
            copy.setGrade(entity.getGrade());
            copy.setUserName(entity.getUserName());
            copy.setVersion(entity.getVersion());
        }
        return copy;
    }
}