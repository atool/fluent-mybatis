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
public class UserEntityHelper implements UserMP{
    /**
     * UserEntity对象转换为HashMap，key值是对象属性
     *
     * @param entity
     * @return
     */
    public static Map<String, Object> map(UserEntity entity){
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
        if(entity.getAddressId() != null){
            map.put(Property.addressId,entity.getAddressId());
        }
        if(entity.getAge() != null){
            map.put(Property.age,entity.getAge());
        }
        if(entity.getUserName() != null){
            map.put(Property.userName,entity.getUserName());
        }
        if(entity.getVersion() != null){
            map.put(Property.version,entity.getVersion());
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
        if(entity.getAddressId() != null){
            map.put(Column.address_id,entity.getAddressId());
        }
        if(entity.getAge() != null){
            map.put(Column.age,entity.getAge());
        }
        if(entity.getUserName() != null){
            map.put(Column.user_name,entity.getUserName());
        }
        if(entity.getVersion() != null){
            map.put(Column.version,entity.getVersion());
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
        if(map.containsKey(Property.addressId)){
            entity.setAddressId((Long)map.get(Property.addressId));
        }
        if(map.containsKey(Property.age)){
            entity.setAge((Integer)map.get(Property.age));
        }
        if(map.containsKey(Property.userName)){
            entity.setUserName((String)map.get(Property.userName));
        }
        if(map.containsKey(Property.version)){
            entity.setVersion((String)map.get(Property.version));
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