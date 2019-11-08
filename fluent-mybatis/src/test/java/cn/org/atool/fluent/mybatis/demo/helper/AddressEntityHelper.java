package cn.org.atool.fluent.mybatis.demo.helper;

import cn.org.atool.fluent.mybatis.demo.entity.AddressEntity;
import cn.org.atool.fluent.mybatis.demo.mapping.AddressMP;

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
public class AddressEntityHelper implements AddressMP{

    public static Map<String, Object> map(AddressEntity entity){
        Map<String, Object> map = new HashMap<>();
        {
            map.put(Property.id, entity.getId());
            map.put(Property.gmtCreated, entity.getGmtCreated());
            map.put(Property.gmtModified, entity.getGmtModified());
            map.put(Property.isDeleted, entity.getIsDeleted());
            map.put(Property.address, entity.getAddress());
        }
        return map;
    }

    public static AddressEntity entity(Map<String, Object> map){
        AddressEntity entity = new AddressEntity();
        {
            entity.setId((Long)map.get(Property.id));
            entity.setGmtCreated((Date)map.get(Property.gmtCreated));
            entity.setGmtModified((Date)map.get(Property.gmtModified));
            entity.setIsDeleted((Boolean)map.get(Property.isDeleted));
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