package cn.org.atool.fluent.mybatis.demo.generate.helper;

import cn.org.atool.fluent.mybatis.demo.generate.entity.NoAutoIdEntity;
import cn.org.atool.fluent.mybatis.demo.generate.mapping.NoAutoIdMP;

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
    /**
     * NoAutoIdEntity对象转换为HashMap，key值是对象属性
     *
     * @param entity
     * @return
     */
    public static Map<String, Object> map(NoAutoIdEntity entity){
        Map<String, Object> map = new HashMap<>();
        if(entity.getId() != null){
            map.put(Property.id,entity.getId());
        }
        if(entity.getColumn1() != null){
            map.put(Property.column1,entity.getColumn1());
        }
        return map;
    }

    /**
     * NoAutoIdEntity对象转换为HashMap，key值是数据库字段
     *
     * @param entity
     * @return
     */
    public static Map<String, Object> column(NoAutoIdEntity entity){
        Map<String, Object> map = new HashMap<>();
        if(entity.getId() != null){
            map.put(Column.id,entity.getId());
        }
        if(entity.getColumn1() != null){
            map.put(Column.column_1,entity.getColumn1());
        }
        return map;
    }

    /**
     * map对应属性值设置到NoAutoIdEntity对象中
     *
     * @param map
     * @return
     */
    public static NoAutoIdEntity entity(Map<String, Object> map){
        NoAutoIdEntity entity = new NoAutoIdEntity();
        if(map.containsKey(Property.id)){
            entity.setId((String)map.get(Property.id));
        }
        if(map.containsKey(Property.column1)){
            entity.setColumn1((String)map.get(Property.column1));
        }
        return entity;
    }

    public static NoAutoIdEntity copy(NoAutoIdEntity entity) {
        NoAutoIdEntity copy = new NoAutoIdEntity();
        {
            copy.setId(entity.getId());
            copy.setColumn1(entity.getColumn1());
        }
        return copy;
    }
}