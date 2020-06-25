package cn.org.atool.fluent.mybatis.demo.generate.helper;

import cn.org.atool.fluent.mybatis.base.model.FieldMeta;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * @ClassName UserMapping
 * @Description t_user映射定义
 *
 * @author generate code
 */
public interface UserMapping {
    /**
     * 表名称
     */
    String Table_Name = "t_user";
    /**
    * 实体名称
    */
    String Entity_NAME = "UserEntity";

    /**
     * id字段映射
     * <p></p>
     */
    FieldMeta id = new FieldMeta("id", "id");
    /**
     * gmtCreated字段映射
     * <p></p>
     */
    FieldMeta gmtCreated = new FieldMeta("gmtCreated", "gmt_created");
    /**
     * gmtModified字段映射
     * <p></p>
     */
    FieldMeta gmtModified = new FieldMeta("gmtModified", "gmt_modified");
    /**
     * isDeleted字段映射
     * <p></p>
     */
    FieldMeta isDeleted = new FieldMeta("isDeleted", "is_deleted");
    /**
     * addressId字段映射
     * <p></p>
     */
    FieldMeta addressId = new FieldMeta("addressId", "address_id");
    /**
     * age字段映射
     * <p></p>
     */
    FieldMeta age = new FieldMeta("age", "age");
    /**
     * userName字段映射
     * <p></p>
     */
    FieldMeta userName = new FieldMeta("userName", "user_name");
    /**
     * version字段映射
     * <p></p>
     */
    FieldMeta version = new FieldMeta("version", "version");

    /**
     * 实例属性和数据库字段对应表
     */
    Map<String, String> Property2Column = new HashMap<String,String>(){
        {
            this.put(id.name, id.column);
            this.put(gmtCreated.name, gmtCreated.column);
            this.put(gmtModified.name, gmtModified.column);
            this.put(isDeleted.name, isDeleted.column);
            this.put(addressId.name, addressId.column);
            this.put(age.name, age.column);
            this.put(userName.name, userName.column);
            this.put(version.name, version.column);
        }
    };

    /**
     * 数据库所有字段列表
     */
    Set<String> ALL_COLUMNS = new HashSet<String>() {
        {
            this.add(id.column);
            this.add(gmtCreated.column);
            this.add(gmtModified.column);
            this.add(isDeleted.column);
            this.add(addressId.column);
            this.add(age.column);
            this.add(userName.column);
            this.add(version.column);
        }
    };
}