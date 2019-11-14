package cn.org.atool.fluent.mybatis.demo.generate.helper;

import cn.org.atool.fluent.mybatis.demo.generate.entity.NoPrimaryEntity;
import cn.org.atool.fluent.mybatis.demo.generate.mapping.NoPrimaryMP;

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
public class NoPrimaryEntityHelper implements NoPrimaryMP{

    public static Map<String, Object> map(NoPrimaryEntity entity){
        Map<String, Object> map = new HashMap<>();
        {
            map.put(Property.column1, entity.getColumn1());
            map.put(Property.column2, entity.getColumn2());
        }
        return map;
    }

    public static NoPrimaryEntity entity(Map<String, Object> map){
        NoPrimaryEntity entity = new NoPrimaryEntity();
        {
            entity.setColumn1((Integer)map.get(Property.column1));
            entity.setColumn2((String)map.get(Property.column2));
        }
        return entity;
    }

    public static NoPrimaryEntity copy(NoPrimaryEntity entity) {
        NoPrimaryEntity copy = new NoPrimaryEntity();
        {
            copy.setColumn1(entity.getColumn1());
            copy.setColumn2(entity.getColumn2());
        }
        return copy;
    }
}