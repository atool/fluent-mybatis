package cn.org.atool.fluent.mybatis.demo.generate.helper;

import cn.org.atool.fluent.mybatis.demo.generate.entity.NoAutoIdEntity;
import cn.org.atool.fluent.mybatis.demo.generate.mapping.NoAutoIdMP;

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
public class NoAutoIdEntityHelper implements NoAutoIdMP{

    public static Map<String, Object> map(NoAutoIdEntity entity){
        Map<String, Object> map = new HashMap<>();
        {
            map.put(Property.column1, entity.getColumn1());
            map.put(Property.id, entity.getId());
        }
        return map;
    }

    public static NoAutoIdEntity entity(Map<String, Object> map){
        NoAutoIdEntity entity = new NoAutoIdEntity();
        {
            entity.setColumn1((String)map.get(Property.column1));
            entity.setId((String)map.get(Property.id));
        }
        return entity;
    }

    public static NoAutoIdEntity copy(NoAutoIdEntity entity) {
        NoAutoIdEntity copy = new NoAutoIdEntity();
        {
            copy.setColumn1(entity.getColumn1());
            copy.setId(entity.getId());
        }
        return copy;
    }
}