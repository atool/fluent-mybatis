package cn.org.atool.fluent.mybatis.demo.generate.helper;

import cn.org.atool.fluent.mybatis.demo.generate.entity.AddressEntity;
import cn.org.atool.fluent.mybatis.demo.generate.mapping.AddressMP;

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
            map.put(AddressMP.id.name, entity.getId());
        }
        if (entity.getGmtCreated() != null) {
            map.put(AddressMP.gmtCreated.name, entity.getGmtCreated());
        }
        if (entity.getGmtModified() != null) {
            map.put(AddressMP.gmtModified.name, entity.getGmtModified());
        }
        if (entity.getIsDeleted() != null) {
            map.put(AddressMP.isDeleted.name, entity.getIsDeleted());
        }
        if (entity.getAddress() != null) {
            map.put(AddressMP.address.name, entity.getAddress());
        }
        return map;
    }

    /**
     * AddressEntity对象转换为HashMap，key值是数据库字段
     *
     * @param entity
     * @return
     */
    public static Map<String, Object> column(AddressEntity entity){
        Map<String, Object> map = new HashMap<>();
        if (entity.getId() != null) {
            map.put(AddressMP.id.column, entity.getId());
        }
        if (entity.getGmtCreated() != null) {
            map.put(AddressMP.gmtCreated.column, entity.getGmtCreated());
        }
        if (entity.getGmtModified() != null) {
            map.put(AddressMP.gmtModified.column, entity.getGmtModified());
        }
        if (entity.getIsDeleted() != null) {
            map.put(AddressMP.isDeleted.column, entity.getIsDeleted());
        }
        if (entity.getAddress() != null) {
            map.put(AddressMP.address.column, entity.getAddress());
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
        if (map.containsKey(AddressMP.id.name)) {
            entity.setId((Long) map.get(AddressMP.id.name));
        }
        if (map.containsKey(AddressMP.gmtCreated.name)) {
            entity.setGmtCreated((Date) map.get(AddressMP.gmtCreated.name));
        }
        if (map.containsKey(AddressMP.gmtModified.name)) {
            entity.setGmtModified((Date) map.get(AddressMP.gmtModified.name));
        }
        if (map.containsKey(AddressMP.isDeleted.name)) {
            entity.setIsDeleted((Boolean) map.get(AddressMP.isDeleted.name));
        }
        if (map.containsKey(AddressMP.address.name)) {
            entity.setAddress((String) map.get(AddressMP.address.name));
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
        }
        return copy;
    }
}