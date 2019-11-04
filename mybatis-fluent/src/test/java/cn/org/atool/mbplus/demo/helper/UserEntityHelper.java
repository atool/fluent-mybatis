package cn.org.atool.mbplus.demo.helper;

import cn.org.atool.mbplus.demo.entity.UserEntity;
import cn.org.atool.mbplus.demo.mapping.UserMP;

import java.util.Date;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * <p>
 * 
 * </p>
 *
 * @author generate code
 */
public class UserEntityHelper implements UserMP{

    public static Map<String, Object> map(UserEntity entity){
        Map<String, Object> map = new HashMap<>();
        {
            map.put(Property.id, entity.getId());
            map.put(Property.userName, entity.getUserName());
            map.put(Property.addressId, entity.getAddressId());
            map.put(Property.gmtCreated, entity.getGmtCreated());
            map.put(Property.gmtModified, entity.getGmtModified());
            map.put(Property.isDeleted, entity.getIsDeleted());
            map.put(Property.age, entity.getAge());
            map.put(Property.version, entity.getVersion());
        }
        return map;
    }

    public static UserEntity entity(Map<String, Object> map){
        UserEntity entity = new UserEntity();
        {
            entity.setId((Long)map.get(Property.id));
            entity.setUserName((String)map.get(Property.userName));
            entity.setAddressId((Long)map.get(Property.addressId));
            entity.setGmtCreated((Date)map.get(Property.gmtCreated));
            entity.setGmtModified((Date)map.get(Property.gmtModified));
            entity.setIsDeleted((Boolean)map.get(Property.isDeleted));
            entity.setAge((Integer)map.get(Property.age));
            entity.setVersion((String)map.get(Property.version));
        }
        return entity;
    }

    public static UserEntity copy(UserEntity entity) {
        UserEntity copy = new UserEntity();
        {
            copy.setId(entity.getId());
            copy.setUserName(entity.getUserName());
            copy.setAddressId(entity.getAddressId());
            copy.setGmtCreated(entity.getGmtCreated());
            copy.setGmtModified(entity.getGmtModified());
            copy.setIsDeleted(entity.getIsDeleted());
            copy.setAge(entity.getAge());
            copy.setVersion(entity.getVersion());
        }
        return copy;
    }
}