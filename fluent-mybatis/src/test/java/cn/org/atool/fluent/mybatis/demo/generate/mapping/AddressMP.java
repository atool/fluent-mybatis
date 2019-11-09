package cn.org.atool.fluent.mybatis.demo.generate.mapping;

import java.util.HashMap;
import java.util.Map;

/**
 * @ClassName AddressMP
 * @Description AddressMP
 *
 * @author generate code
 */
public interface AddressMP {
    /**
     * 实例属性和数据库字段对应表
     */
    Map<String, String> Property2Column = new HashMap<String,String>(){
        {
            this.put(Property.id, Column.id);
            this.put(Property.gmtCreated, Column.gmt_created);
            this.put(Property.gmtModified, Column.gmt_modified);
            this.put(Property.isDeleted, Column.is_deleted);
            this.put(Property.address, Column.address);
        }
    };

    /**
     * 表名称
     */
    String Table_Name = "address";
    /**
    * 实体名称
    */
    String Entity_NAME = "AddressEntity";

    /**
     * 表address字段定义
     */
    interface Column{
        /**
         * 
         */
        String id = "id";
        /**
         * 
         */
        String gmt_created = "gmt_created";
        /**
         * 
         */
        String gmt_modified = "gmt_modified";
        /**
         * 
         */
        String is_deleted = "is_deleted";
        /**
         * 
         */
        String address = "address";
    }

    /**
     * 对象AddressEntity属性字段
     */
    interface Property{
        /**
         * 
         */
        String id = "id";
        /**
         * 
         */
        String gmtCreated = "gmtCreated";
        /**
         * 
         */
        String gmtModified = "gmtModified";
        /**
         * 
         */
        String isDeleted = "isDeleted";
        /**
         * 
         */
        String address = "address";
    }
}