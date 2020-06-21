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
public class AddressEntityHelper implements AddressMP{
    /**
     * AddressEntity对象转换为HashMap，key值是对象属性
     *
     * @param entity
     * @return
     */
    public static Map<String, Object> map(AddressEntity entity){
        Map<String, Object> map = new HashMap<>();
        if(entity.getId() != null){
            map.put(Property.id,entity.getId());
        }
        if(entity.getGmtCreated() != null){
            map.put(Property.gmtCreated,entity.getGmtCreated());
        }
        if(entity.getGmtModified() != null){
            map.put(Property.gmtModified,entity.getGmtModified());
        }
        if(entity.getIsDeleted() != null){
            map.put(Property.isDeleted,entity.getIsDeleted());
        }
        if(entity.getAddress() != null){
            map.put(Property.address,entity.getAddress());
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
        if(entity.getId() != null){
            map.put(Column.id,entity.getId());
        }
        if(entity.getGmtCreated() != null){
            map.put(Column.gmt_created,entity.getGmtCreated());
        }
        if(entity.getGmtModified() != null){
            map.put(Column.gmt_modified,entity.getGmtModified());
        }
        if(entity.getIsDeleted() != null){
            map.put(Column.is_deleted,entity.getIsDeleted());
        }
        if(entity.getAddress() != null){
            map.put(Column.address,entity.getAddress());
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
        if(map.containsKey(Property.id)){
            entity.setId((Long)map.get(Property.id));
        }
        if(map.containsKey(Property.gmtCreated)){
            entity.setGmtCreated((Date)map.get(Property.gmtCreated));
        }
        if(map.containsKey(Property.gmtModified)){
            entity.setGmtModified((Date)map.get(Property.gmtModified));
        }
        if(map.containsKey(Property.isDeleted)){
            entity.setIsDeleted((Boolean)map.get(Property.isDeleted));
        }
        if(map.containsKey(Property.address)){
            entity.setAddress((String)map.get(Property.address));
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