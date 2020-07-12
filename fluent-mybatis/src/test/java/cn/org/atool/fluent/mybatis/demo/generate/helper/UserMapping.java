package cn.org.atool.fluent.mybatis.demo.generate.helper;

import cn.org.atool.fluent.mybatis.base.model.FieldMapping;

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
    FieldMapping id = new FieldMapping("id", "id");
    /**
     * gmtCreated字段映射
     * <p></p>
     */
    FieldMapping gmtCreated = new FieldMapping("gmtCreated", "gmt_created");
    /**
     * gmtModified字段映射
     * <p></p>
     */
    FieldMapping gmtModified = new FieldMapping("gmtModified", "gmt_modified");
    /**
     * isDeleted字段映射
     * <p></p>
     */
    FieldMapping isDeleted = new FieldMapping("isDeleted", "is_deleted");
    /**
     * addressId字段映射
     * <p></p>
     */
    FieldMapping addressId = new FieldMapping("addressId", "address_id");
    /**
     * age字段映射
     * <p></p>
     */
    FieldMapping age = new FieldMapping("age", "age");
    /**
     * grade字段映射
     * <p></p>
     */
    FieldMapping grade = new FieldMapping("grade", "grade");
    /**
     * userName字段映射
     * <p></p>
     */
    FieldMapping userName = new FieldMapping("userName", "user_name");
    /**
     * version字段映射
     * <p></p>
     */
    FieldMapping version = new FieldMapping("version", "version");

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
            this.put(grade.name, grade.column);
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
            this.add(grade.column);
            this.add(userName.column);
            this.add(version.column);
        }
    };
}