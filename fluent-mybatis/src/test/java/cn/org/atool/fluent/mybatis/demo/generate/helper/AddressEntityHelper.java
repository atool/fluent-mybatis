package cn.org.atool.fluent.mybatis.demo.generate.helper;

import cn.org.atool.fluent.mybatis.demo.generate.entity.AddressEntity;
import cn.org.atool.fluent.mybatis.demo.generate.helper.AddressMapping;

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
public class AddressEntityHelper {
    /**
     * AddressEntity对象转换为HashMap，key值是对象属性
     *
     * @param entity
     * @return
     */
    public static Map<String, Object> map(AddressEntity entity){
        Map<String, Object> map = new HashMap<>();
        if (entity.getId() != null) {
            map.put(AddressMapping.id.name, entity.getId());
        }
        if (entity.getGmtCreated() != null) {
            map.put(AddressMapping.gmtCreated.name, entity.getGmtCreated());
        }
        if (entity.getGmtModified() != null) {
            map.put(AddressMapping.gmtModified.name, entity.getGmtModified());
        }
        if (entity.getIsDeleted() != null) {
            map.put(AddressMapping.isDeleted.name, entity.getIsDeleted());
        }
        if (entity.getAddress() != null) {
            map.put(AddressMapping.address.name, entity.getAddress());
        }
        if (entity.getUserId() != null) {
            map.put(AddressMapping.userId.name, entity.getUserId());
        }
        return map;
    }

    /**
     * AddressEntity对象转换为HashMap，key值是数据库字段
     *
     * @param entity
     * @return
     */
    public static Map<String, Object> columnMap(AddressEntity entity){
        Map<String, Object> map = new HashMap<>();
        if (entity.getId() != null) {
            map.put(AddressMapping.id.column, entity.getId());
        }
        if (entity.getGmtCreated() != null) {
            map.put(AddressMapping.gmtCreated.column, entity.getGmtCreated());
        }
        if (entity.getGmtModified() != null) {
            map.put(AddressMapping.gmtModified.column, entity.getGmtModified());
        }
        if (entity.getIsDeleted() != null) {
            map.put(AddressMapping.isDeleted.column, entity.getIsDeleted());
        }
        if (entity.getAddress() != null) {
            map.put(AddressMapping.address.column, entity.getAddress());
        }
        if (entity.getUserId() != null) {
            map.put(AddressMapping.userId.column, entity.getUserId());
        }
        return map;
    }

    /**
     * map对应属性值设置到AddressEntity对象中
     *
     * @param map
     * @return
     */
    public static AddressEntity entity(Map<String, Object> map){
        AddressEntity entity = new AddressEntity();
        if (map.containsKey(AddressMapping.id.name)) {
            entity.setId((Long) map.get(AddressMapping.id.name));
        }
        if (map.containsKey(AddressMapping.gmtCreated.name)) {
            entity.setGmtCreated((Date) map.get(AddressMapping.gmtCreated.name));
        }
        if (map.containsKey(AddressMapping.gmtModified.name)) {
            entity.setGmtModified((Date) map.get(AddressMapping.gmtModified.name));
        }
        if (map.containsKey(AddressMapping.isDeleted.name)) {
            entity.setIsDeleted((Boolean) map.get(AddressMapping.isDeleted.name));
        }
        if (map.containsKey(AddressMapping.address.name)) {
            entity.setAddress((String) map.get(AddressMapping.address.name));
        }
        if (map.containsKey(AddressMapping.userId.name)) {
            entity.setUserId((Long) map.get(AddressMapping.userId.name));
        }
        return entity;
    }

    public static AddressEntity copy(AddressEntity entity) {
        AddressEntity copy = new AddressEntity();
        {
            copy.setId(entity.getId());
            copy.setGmtCreated(entity.getGmtCreated());
            copy.setGmtModified(entity.getGmtModified());
            copy.setIsDeleted(entity.getIsDeleted());
            copy.setAddress(entity.getAddress());
            copy.setUserId(entity.getUserId());
        }
        return copy;
    }
}