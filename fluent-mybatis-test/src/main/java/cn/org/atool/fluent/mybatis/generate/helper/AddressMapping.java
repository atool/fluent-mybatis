package cn.org.atool.fluent.mybatis.generate.helper;

import cn.org.atool.fluent.mybatis.base.model.FieldMapping;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * @ClassName AddressMapping
 * @Description address映射定义
 *
 * @author generate code
 */
public interface AddressMapping {
    /**
     * 表名称
     */
    String Table_Name = "address";
    /**
    * 实体名称
    */
    String Entity_NAME = "AddressEntity";

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
     * address字段映射
     * <p></p>
     */
    FieldMapping address = new FieldMapping("address", "address");
    /**
     * userId字段映射
     * <p></p>
     */
    FieldMapping userId = new FieldMapping("userId", "user_id");

    /**
     * 实例属性和数据库字段对应表
     */
    Map<String, String> Property2Column = new HashMap<String,String>(){
        {
            this.put(id.name, id.column);
            this.put(gmtCreated.name, gmtCreated.column);
            this.put(gmtModified.name, gmtModified.column);
            this.put(isDeleted.name, isDeleted.column);
            this.put(address.name, address.column);
            this.put(userId.name, userId.column);
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
            this.add(address.column);
            this.add(userId.column);
        }
    };
}