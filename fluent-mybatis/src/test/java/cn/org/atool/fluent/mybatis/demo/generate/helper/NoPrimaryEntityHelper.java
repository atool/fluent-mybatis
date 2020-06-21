package cn.org.atool.fluent.mybatis.demo.generate.helper;

import cn.org.atool.fluent.mybatis.demo.generate.entity.NoPrimaryEntity;
import cn.org.atool.fluent.mybatis.demo.generate.mapping.NoPrimaryMP;

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
    /**
     * NoPrimaryEntity对象转换为HashMap，key值是对象属性
     *
     * @param entity
     * @return
     */
    public static Map<String, Object> map(NoPrimaryEntity entity){
        Map<String, Object> map = new HashMap<>();
        if(entity.getColumn1() != null){
            map.put(Property.column1,entity.getColumn1());
        }
        if(entity.getColumn2() != null){
            map.put(Property.column2,entity.getColumn2());
        }
        return map;
    }

    /**
     * NoPrimaryEntity对象转换为HashMap，key值是数据库字段
     *
     * @param entity
     * @return
     */
    public static Map<String, Object> column(NoPrimaryEntity entity){
        Map<String, Object> map = new HashMap<>();
        if(entity.getColumn1() != null){
            map.put(Column.column_1,entity.getColumn1());
        }
        if(entity.getColumn2() != null){
            map.put(Column.column_2,entity.getColumn2());
        }
        return map;
    }

    /**
     * map对应属性值设置到NoPrimaryEntity对象中
     *
     * @param map
     * @return
     */
    public static NoPrimaryEntity entity(Map<String, Object> map){
        NoPrimaryEntity entity = new NoPrimaryEntity();
        if(map.containsKey(Property.column1)){
            entity.setColumn1((Integer)map.get(Property.column1));
        }
        if(map.containsKey(Property.column2)){
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