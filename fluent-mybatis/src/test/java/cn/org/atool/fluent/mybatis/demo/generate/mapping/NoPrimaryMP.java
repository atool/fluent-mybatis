package cn.org.atool.fluent.mybatis.demo.generate.mapping;

import java.util.HashMap;
import java.util.Map;

/**
 * @ClassName NoPrimaryMP
 * @Description NoPrimaryMP
 *
 * @author generate code
 */
public interface NoPrimaryMP {
    /**
     * 实例属性和数据库字段对应表
     */
    Map<String, String> Property2Column = new HashMap<String,String>(){
        {
            this.put(Property.column1, Column.column_1);
            this.put(Property.column2, Column.column_2);
        }
    };

    /**
     * 表名称
     */
    String Table_Name = "no_primary";
    /**
    * 实体名称
    */
    String Entity_NAME = "NoPrimaryEntity";

    /**
     * 表no_primary字段定义
     */
    interface Column{
        /**
         * 
         */
        String column_1 = "column_1";
        /**
         * 
         */
        String column_2 = "column_2";
    }

    /**
     * 对象NoPrimaryEntity属性字段
     */
    interface Property{
        /**
         * 
         */
        String column1 = "column1";
        /**
         * 
         */
        String column2 = "column2";
    }
}