package cn.org.atool.fluent.mybatis.demo.generate.mapping;

import java.util.HashMap;
import java.util.Map;

/**
 * @ClassName UserMP
 * @Description t_user映射定义
 *
 * @author generate code
 */
public interface UserMP {
    /**
     * 实例属性和数据库字段对应表
     */
    Map<String, String> Property2Column = new HashMap<String,String>(){
        {
            this.put(Property.id, Column.id);
            this.put(Property.gmtCreated, Column.gmt_created);
            this.put(Property.gmtModified, Column.gmt_modified);
            this.put(Property.isDeleted, Column.is_deleted);
            this.put(Property.addressId, Column.address_id);
            this.put(Property.age, Column.age);
            this.put(Property.userName, Column.user_name);
            this.put(Property.version, Column.version);
        }
    };

    /**
     * 表名称
     */
    String Table_Name = "t_user";
    /**
    * 实体名称
    */
    String Entity_NAME = "UserEntity";

    /**
     * 表t_user字段定义
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
        String address_id = "address_id";
        /**
         * 
         */
        String age = "age";
        /**
         * 
         */
        String user_name = "user_name";
        /**
         * 
         */
        String version = "version";
    }

    /**
     * 对象UserEntity属性字段
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
        String addressId = "addressId";
        /**
         * 
         */
        String age = "age";
        /**
         * 
         */
        String userName = "userName";
        /**
         * 
         */
        String version = "version";
    }
}