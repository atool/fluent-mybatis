package cn.org.atool.fluent.mybatis.demo.generate.mapping;

import java.util.HashMap;
import java.util.Map;

/**
 * @ClassName NoAutoIdMP
 * @Description NoAutoIdMP
 *
 * @author generate code
 */
public interface NoAutoIdMP {
    /**
     * 实例属性和数据库字段对应表
     */
    Map<String, String> Property2Column = new HashMap<String,String>(){
        {
            this.put(Property.column1, Column.column_1);
            this.put(Property.id, Column.id);
        }
    };

    /**
     * 表名称
     */
    String Table_Name = "no_auto_id";
    /**
    * 实体名称
    */
    String Entity_NAME = "NoAutoIdEntity";

    /**
     * 表no_auto_id字段定义
     */
    interface Column{
        /**
         * 
         */
        String column_1 = "column_1";
        /**
         * 
         */
        String id = "id";
    }

    /**
     * 对象NoAutoIdEntity属性字段
     */
    interface Property{
        /**
         * 
         */
        String column1 = "column1";
        /**
         * 
         */
        String id = "id";
    }
}